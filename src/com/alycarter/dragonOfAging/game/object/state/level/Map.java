package com.alycarter.dragonOfAging.game.object.state.level;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.math.Vector3;

public class Map {

	private final float DEFAULT_HEIGHT = 0.5f;
	private final float NOISE = (1.0f/8.0f);
	
	private final static Point UP = new Point(0, -1);
	private final static Point DOWN = new Point(0, 1);
	private final static Point LEFT = new Point(-1, 0);
	private final static Point RIGHT = new Point(1, 0);
	
	private final static int NUM_ROOMS = 8;
	
	private Point size;
	private Tile map[];
	private TiledTexture mapTexture;
	private int shadow;
	private long seed = 0;
	
	private ArrayList<Vector3> enemySpawnLocations;
	private ArrayList<Vector3> pickupSpawnLocations;
	
	private Vector3 playerSpawnLocation;
	private Vector3 levelExitLocation;
	
	private boolean inisialised;
	
	private LevelType levelType;
	
	private ArrayList<Room> roomLayouts;
	
	public Map(Level level, int shadow) {
		mapTexture = level.getTiledTexture("map");
		this.shadow = shadow;
		enemySpawnLocations = new ArrayList<Vector3>();
		pickupSpawnLocations = new ArrayList<Vector3>();
		inisialised = false;
		roomLayouts = new ArrayList<Room>();
		loadMapLayouts();
	}
	
	private void loadMapLayouts(){
		for(int i =0 ; i < NUM_ROOMS; i++){
			Room room = new Room();
			room.loadRoom("/maps/"+i+".map");
			roomLayouts.add(room);
		}
	}
	
	public void genMap(LevelType levelType, int width, int height, int rooms, long seed){
		this.levelType = levelType;
		enemySpawnLocations.clear();
		pickupSpawnLocations.clear();
		this.seed = seed;
		Random random = new Random(seed);
		size = new Point(width, height);
		map = new Tile[width * height];
		for(int i = 0; i < width*height; i++){
			map[i]= new Tile(0,0,levelType.getTextureOffset(), 0, 0);
			map[i].setHeight(DEFAULT_HEIGHT+Math.round(random.nextFloat())*NOISE);
		}
		ArrayList<Node> openNodes = new ArrayList<Node>();
		openNodes.add(new Node(new Point(width/2, height/2), RIGHT));
		genRoom(openNodes, random, roomLayouts.get(1));
		for(int i =0 ; i <15; i++){
			genRoom(openNodes, random, roomLayouts.get((int)(random.nextFloat()*roomLayouts.size())));
		}
		playerSpawnLocation = new Vector3(width/2, height/2, getHeight(width/2, height/2));
		levelExitLocation = new Vector3(0, 0, 0);
		inisialised = true;
	}
	
	public long getCurrentSeed(){
		return seed;
	}
	
	private void genRoom(ArrayList<Node> nodes, Random random, Room room){
		Node node = getClearNode(nodes, room, random);
		Point offSet = calculateRoomOffSet(node, room);
		for(int i = 0; i < room.getTiles().size(); i++ ){	
			Tile roomTile = room.getTiles().get(i);
			setTile(roomTile.getPosition().x + offSet.x, roomTile.getPosition().y + offSet.y, roomTile);
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
			if(getTile(tilePos.x, tilePos.y) == null || getTile(tilePos.x, tilePos.y).isEdited()){
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
	
	private void setTile(int x, int y, Tile template){
		Tile mapTile = getTile(x, y);
		if(template.getEditedHeight()){
			float noise = 0;
			if(template.isUsingNoise()){
				noise+=NOISE;
			}
			mapTile.setCollisionHeight(template.getCollisionHeight()+noise);
			mapTile.setRenderHeight(template.getRenderHeight()+noise);
			mapTile.setEditedHeight(true);
		}
		if (template.getEditedTexture()) {			
			mapTile.setTexture(template.getTexture());
			mapTile.setEditedTexture(true);
		}
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
	
	public ArrayList<Vector3> getEnemySpawnLocations(){
		return enemySpawnLocations;
	}
	
	public ArrayList<Vector3> getPickupSpawnLocations(){
		return pickupSpawnLocations;
	}
	
	public Vector3 getPlayerSpawnLocation(){
		return playerSpawnLocation;
	}
	
	public Vector3 getLevelExitLocation(){
		return levelExitLocation;
	}
	
	public Vector3 getNextEnemySpawnPosition(){
		if(enemySpawnLocations.size() != 0){
			return enemySpawnLocations.remove((int) (Math.random()*enemySpawnLocations.size()));
		}else{
			return null;
		}
	}
	
	public Vector3 getNextPickupSpawnPosition(){
		if(pickupSpawnLocations.size() != 0){
			return pickupSpawnLocations.remove((int) (Math.random()*pickupSpawnLocations.size()));
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
