package com.alycarter.dragonOfAging.game.object.state.level;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Room {
	
	private ArrayList<Tile> tiles;
	private Point size;
	
	public Room() {
		tiles = new ArrayList<Tile>();
		size = new Point(0, 0);
	}
	
	public void loadRoom(String fileName){
		BufferedReader reader = new BufferedReader(new InputStreamReader(Map.class.getResourceAsStream(fileName)));
		try {
			size.setLocation(Integer.valueOf(reader.readLine()), Integer.valueOf(reader.readLine()));
			String value = reader.readLine();
			while(!value.equals( "eof")){
				Tile tile = new Tile(0 , 0, 0, 0, 0);
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
				tiles.add(tile);
				value = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Tile> getTiles(){
		return tiles;
	}
	
	public Point getSize(){
		return size;
	}
}
