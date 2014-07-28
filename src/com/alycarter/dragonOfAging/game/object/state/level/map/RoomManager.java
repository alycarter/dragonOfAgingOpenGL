package com.alycarter.dragonOfAging.game.object.state.level.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class RoomManager {

	private ArrayList<Room> rooms;
	
	public RoomManager() {
		rooms = new ArrayList<Room>();
	}
	
	public void loadRoomLayouts(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(RoomManager.class.getResourceAsStream("/maps/rooms.txt")));
		try {
			String value = reader.readLine();
			while(!value.equals("eof")){
				loadRoomFolder(value, Integer.valueOf(reader.readLine()));
				value = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void loadRoomFolder(String folderName, int roomCount){		
		for(int i =0 ; i < roomCount; i++){
			Room room = new Room(folderName);
			room.loadRoom("/maps/"+folderName+"/"+i+".map");
			rooms.add(room);
		}
	}
	
	public Room getRoom(Random random){
		return rooms.get((int) (random.nextFloat()*rooms.size()));
	}

}
