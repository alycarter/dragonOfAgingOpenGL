package com.alycarter.dragonOfAging.game.object.state.level.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.LevelType;

public class Map {

	private final float DEFAULT_HEIGHT = 0.5f;
	private final float NOISE = (1.0f/8.0f);
	
	private final static Point UP = new Point(0, -1);
	private final static Point DOWN = new Point(0, 1);
	private final static Point LEFT = new Point(-1, 0);
	private final static Point RIGHT = new Point(1, 0);
	
	private Point size;
	private Tile map[];
	private TiledTexture mapTexture;
	private int shadow;
	
	private ArrayList<Point> enemySpawnLocations;
	private ArrayList<Point> itemSpawnLocations;
	
	private ArrayList<Point> playerSpawnLocations;
	private ArrayList<Point> levelExitLocations;
	
	private boolean inisialised;
	
	private LevelType levelType;

	private RoomManager roomManager;
	
	public Map(Level level, int shadow) {
		mapTexture = level.getTiledTexture("map");
		this.shadow = shadow;
		enemySpawnLocations = new ArrayList<Point>();
		itemSpawnLocations = new ArrayList<Point>();
		playerSpawnLocations = new ArrayList<Point>();
		levelExitLocations = new ArrayList<Point>();
		inisialised = false;
		roomManager = new RoomManager();
		roomManager.loadRoomLayouts();
	}
	
	public void genMap(LevelType levelType, int width, int height, int rooms, Random random){
		this.levelType = levelType;
		clearLocations();
		size = new Point(width, height);
		resetMapTiles(random);
		ArrayList<Node> openNodes = new ArrayList<Node>();
		openNodes.add(new Node(new Point(width/2, height/2), RIGHT));
		for(int i =0 ; i <rooms; i++){
			genRoom(openNodes, random, roomManager.getRoom(random));
		}
		inisialised = true;
	}
	
	private void clearLocations(){
		enemySpawnLocations.clear();
		itemSpawnLocations.clear();
		playerSpawnLocations.clear();
		levelExitLocations.clear();
	}
	
	private void resetMapTiles(Random random){
		map = new Tile[size.x* size.y];
		for(int i = 0; i < size.x*size.y; i++){
			map[i]= new Tile(levelType.getTextureOffset(), 0, 0, false);
			map[i].setHeight(DEFAULT_HEIGHT+Math.round(random.nextFloat())*NOISE);
		}
	}
	
	private void genRoom(ArrayList<Node> nodes, Random random, Room room){
		Node node = getClearNode(nodes, room, random);
		Point offSet = calculateRoomOffSet(node, room);
		for(int i = 0; i < room.getTiles().size(); i++ ){	
			TemplateTile roomTile = room.getTiles().get(i);
			setTile(roomTile.getPosition().x + offSet.x, roomTile.getPosition().y + offSet.y, roomTile, random);
		}
		if(node.direction.x == 0){ //if its up or down
			nodes.add(new Node(new Point(node.location.x + (room.getSize().x/2), node.location.y + (int)Math.ceil(room.getSize().y / 2.0f * node.direction.y)), RIGHT));
			nodes.add(new Node(new Point(node.location.x - (room.getSize().x/2)-(room.getSize().x%2), node.location.y + (int)Math.ceil(room.getSize().y / 2.0f * node.direction.y)), LEFT));
		}
		if(node.direction.y == 0){ //if its left or right
			nodes.add(new Node(new Point(node.location.x + (int)Math.ceil(room.getSize().x / 2.0f * node.direction.x),node.location.y + (room.getSize().y/2)) , DOWN));
			nodes.add(new Node(new Point(node.location.x + (int)Math.ceil(room.getSize().x / 2.0f * node.direction.x),node.location.y - (room.getSize().y/2)-(room.getSize().y%2)), UP));
		}
		node.location.x+= room.getSize().x * node.direction.x;
		node.location.y+= room.getSize().y * node.direction.y;
		addRoomLocations(room, offSet);
	}
	
	private void addRoomLocations(Room room, Point offSet){
		for(int i = 0; i < room.getEnemySpawnPositions().size(); i++){
			Point location = new Point(room.getEnemySpawnPositions().get(i));
			location.x+=offSet.x;
			location.y+=offSet.y;
			enemySpawnLocations.add(location);
		}
		for(int i = 0; i < room.getItemSpawnPositions().size(); i++){
			Point location = new Point(room.getItemSpawnPositions().get(i));
			location.x+=offSet.x;
			location.y+=offSet.y;
			itemSpawnLocations.add(location);
		}
		for(int i = 0; i < room.getPlayerSpawnPositions().size(); i++){
			Point location = new Point(room.getPlayerSpawnPositions().get(i));
			location.x+=offSet.x;
			location.y+=offSet.y;
			playerSpawnLocations.add(location);
		}
		for(int i = 0; i < room.getLevelExitPositions().size(); i++){
			Point location = new Point(room.getLevelExitPositions().get(i));
			location.x+=offSet.x;
			location.y+=offSet.y;
			levelExitLocations.add(location);
		}
	}
	
	public Node getClearNode(ArrayList<Node> nodes, Room room, Random random){
		ArrayList<Node> ingoreNodes = new ArrayList<Node>();
		Node node = nodes.get((int) (random.nextFloat()*nodes.size()));
		Point offSet = calculateRoomOffSet(node, room);
		while(!isRoomClear(room, offSet)){
			ingoreNodes.add(node);
			nodes.remove(node);
			node = nodes.get((int) (random.nextFloat()*nodes.size()));
			offSet = calculateRoomOffSet(node, room);
		}
		nodes.addAll(ingoreNodes);
		return node;
	}
	
	public boolean isRoomClear(Room room, Point offSet){
		for(int i = 0; i < room.getTiles().size(); i++){
			Point tilePos = new Point(offSet);
			tilePos.x += room.getTiles().get(i).getPosition().x;
			tilePos.y += room.getTiles().get(i).getPosition().y;			
			if(getTile(tilePos.x, tilePos.y) == null || getTile(tilePos.x, tilePos.y).isReserved()){
				return false;
			}
		}
		return true;
	}
	
	public Point calculateRoomOffSet(Node node, Room room){
		Point offSet = new Point(node.location);
		offSet.x += ((node.direction.getX() - 1.0f) / 2.0f * room.getSize().getX());
		offSet.y += ((node.direction.getY() - 1.0f) / 2.0f * room.getSize().getY());
		return offSet;
	}
	
	public float getHeight(int x, int y){
		if(x >= 0 && x < size.getX() && y >= 0 && y < size.getY()){
			return map[(y*size.y) + x].getCollisionHeight();
		}else{
			return 1.0f;
		}
	}
	
	private int getTileTexture(int x, int y){
		if(x >= 0 && x < size.getX() && y >= 0 && y < size.getY()){
			return map[(y*size.y) + x].getTexture();
		}else{
			return levelType.getTextureOffset();
		}
	}
	
	private Tile getTile(int x, int y){
		if(x >= 0 && x < size.getX() && y >= 0 && y < size.getY()){
			return map[(y*size.y) + x];
		}else{
			return null;
		}
	} 
	
	private void setTile(int x, int y, TemplateTile template, Random random){
		Tile mapTile = getTile(x, y);
		if(template.isEditedHeight()){
			float noise = 0;
			if(template.isUseNoise() && random.nextFloat()<0.5f){
				noise+=NOISE;
			}
			mapTile.setCollisionHeight(template.getCollisionHeight()+noise);
			mapTile.setRenderHeight(template.getRenderHeight()+noise);
		}
		if (template.isEditedTexture()) {			
			mapTile.setTexture(template.getTexture());
		}
		mapTile.setReserved(template.isReserved());
	}
	
	public void render(Graphics graphics, float left, float right, float top, float bottom){
		renderTexture(graphics, left, right, top, bottom);
		renderShadows(graphics, left, right, top, bottom);
	}
	
	private void renderTexture(Graphics graphics, float left, float right, float top, float bottom){
		for(int y =(int)Math.ceil(bottom); y > Math.floor(top)-1; y--){	
			for(int x = (int) Math.floor(left); x < Math.ceil(right); x++){
				float height = getHeight(x, y);
				float value = 1-(height/2);
				FloatColor color = new FloatColor(value, value, value, 1.0f);
				float xPos = x + 0.5f;
				float yPos = y + 0.5f - height;
				graphics.drawImage(mapTexture.getTileTextureID(getTileTexture(x, y)),color, xPos, yPos+0.5f, y, 1, 2, 0);
			}	
		}
	}
	
	private void renderShadows(Graphics graphics, float left, float right, float top, float bottom){
		float tilesHigh = bottom - top;
		float tilesWide = right - left;
		graphics.bindTexture(shadow);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
			GL11.glTranslatef(0, -1f, 0);
			GL11.glScalef(1.0f/(tilesWide), (1.0f/(tilesHigh))*-0.5f, 1);
			for(int y =(int)Math.ceil(bottom); y > Math.floor(top)-1; y--){	
				for(int x = (int) Math.floor(left); x < Math.ceil(right); x++){
					float height = getHeight(x, y);
					float xPos = x + 0.5f;
					float yPos = y + 0.5f - height;
					GL11.glPushMatrix();
						GL11.glTranslatef(x-left, y-top, 0);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						graphics.drawRectangle(xPos, yPos, y+0.01f, 1, 1, 0);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glTranslatef(0, 1.0f, 0);
						GL11.glScalef(1, 1.0f/(tilesHigh*192), 1);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						graphics.drawRectangle(xPos, yPos+1, y+0.01f, 1, 1, 0);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glPopMatrix();
				}	
			}
		GL11.glPopMatrix();	
		graphics.unBindTexture();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public Point getSize(){
		return size;
	}
	
	private class Node{
		
		
		public Point location;
		public Point direction;
		
		public Node(Point location, Point direction){
			this.location = location;
			this.direction = direction;
		}
	}
	
	public ArrayList<Point> getEnemySpawnLocations(){
		return enemySpawnLocations;
	}
	
	public ArrayList<Point> getPickupSpawnLocations(){
		return itemSpawnLocations;
	}
	
	public Vector3 getPlayerSpawnLocation(){
		Vector3 center = new Vector3(size.x/2, size.y/2, getHeight(size.x/2, size.y/2)); 
		if(playerSpawnLocations.size()==0){
			return center;
		}else{
			Vector3 pos = null;
			for(int i = 0; i < playerSpawnLocations.size(); i++){
				Vector3 newPos = new Vector3(playerSpawnLocations.get(i).x+0.5f, playerSpawnLocations.get(i).y+0.5f, 
						getHeight(playerSpawnLocations.get(i).x, playerSpawnLocations.get(i).y));
				if(pos == null || newPos.distanceTo(center) < pos.distanceTo(center)){
					pos = newPos;
				}
			}
			return pos;
		}
	}
	
	public Vector3 getLevelExitLocation(){
		if(levelExitLocations.size() > 0){
			Vector3 player = getPlayerSpawnLocation();
			Vector3 pos = null;
			for(int i = 0; i < levelExitLocations.size(); i++){
				Vector3 newPos = new Vector3(levelExitLocations.get(i).x+0.5f, levelExitLocations.get(i).y+0.5f, 
						getHeight(levelExitLocations.get(i).x, levelExitLocations.get(i).y));
				if(pos == null || newPos.distanceTo(player) > pos.distanceTo(player)){
					pos = newPos;
				}
			}
			return pos;
		}else{
			return new Vector3(0, 0, 1);
		}
	}
	
	public Vector3 getNextEnemySpawnPosition(Random random){
		if(enemySpawnLocations.size() != 0){
			Point tile = enemySpawnLocations.remove((int) (random.nextFloat()*enemySpawnLocations.size()));
			Vector3 location = new Vector3(tile.x+0.5f, tile.y+0.5f, getHeight(tile.x, tile.y));
			return location;
		}else{
			return null;
		}
	}
	
	public Vector3 getNextPickupSpawnPosition(Random random){
		if(itemSpawnLocations.size() != 0){
			Point tile = itemSpawnLocations.remove((int) (random.nextFloat()*itemSpawnLocations.size()));
			Vector3 location = new Vector3(tile.x+0.5f, tile.y+0.5f, getHeight(tile.x, tile.y));
			return location;
		}else{
			return null;
		}
	}
	
	public boolean isInisialised(){
		return inisialised;
	}
	
	public LevelType getLevelType(){
		return levelType;
	}
	
}
