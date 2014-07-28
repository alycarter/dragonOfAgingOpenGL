package com.alycarter.dragonOfAging.game.object.state.level.map;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Room {
	
	private ArrayList<TemplateTile> tiles;
	private Point size;
	
	private ArrayList<Point> enemySpawnPositions;
	private ArrayList<Point> itemSpawnPositions;
	private ArrayList<Point> playerSpawnPositions;
	private ArrayList<Point> levelExitPositions;
	
	private String roomType;
	
	public static final String TYPE_START = "start";
	public static final String TYPE_END = "end";
	public static final String TYPE_GENERAL = "general";
	public static final String TYPE_ITEM = "item";
	
	public Room(String roomType) {
		tiles = new ArrayList<TemplateTile>();
		size = new Point(0, 0);
		enemySpawnPositions = new ArrayList<Point>();
		itemSpawnPositions = new ArrayList<Point>();
		playerSpawnPositions = new ArrayList<Point>();
		levelExitPositions = new ArrayList<Point>();
		this.roomType = roomType;
	}
	
	public void loadRoom(String fileName){
		BufferedReader reader = new BufferedReader(new InputStreamReader(Map.class.getResourceAsStream(fileName)));
		try {
			size.setLocation(Integer.valueOf(reader.readLine()), Integer.valueOf(reader.readLine()));
			String value = reader.readLine();
			while(!value.equals( "eof")){
				TemplateTile tile = new TemplateTile();
				tile.setPosition(Integer.valueOf(value), Integer.valueOf(reader.readLine()));
				value = reader.readLine();
				if(value.length() != 0){
					tile.setRenderHeight(Float.valueOf(value));
					tile.setCollisionHeight(Float.valueOf(reader.readLine()));
					tile.setEditedHeight(true);
				}
				value = reader.readLine();
				if(value.length() != 0){
					tile.setTexture(Integer.valueOf(value));
					tile.setEditedTexture(true);
				}
				tile.setUseNoise(Boolean.valueOf(reader.readLine()));
				tile.setReserved(Boolean.valueOf(reader.readLine()));
				tiles.add(tile);
				value = reader.readLine();
				if(value.length() != 0){
					enemySpawnPositions.add(new Point(tile.getPosition().x, tile.getPosition().y));
				}
				value = reader.readLine();
				if(value.length() != 0){
					itemSpawnPositions.add(new Point(tile.getPosition().x, tile.getPosition().y));
				}
				if(Boolean.valueOf(reader.readLine())){
					playerSpawnPositions.add(new Point(tile.getPosition().x, tile.getPosition().y));
				}
				if(Boolean.valueOf(reader.readLine())){
					levelExitPositions.add(new Point(tile.getPosition().x, tile.getPosition().y));
				}
				value = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<TemplateTile> getTiles(){
		return tiles;
	}
	
	public Point getSize(){
		return size;
	}

	public ArrayList<Point> getEnemySpawnPositions() {
		return enemySpawnPositions;
	}

	public ArrayList<Point> getItemSpawnPositions() {
		return itemSpawnPositions;
	}

	public ArrayList<Point> getPlayerSpawnPositions() {
		return playerSpawnPositions;
	}

	public ArrayList<Point> getLevelExitPositions() {
		return levelExitPositions;
	}

	public String getRoomType() {
		return roomType;
	}
}
