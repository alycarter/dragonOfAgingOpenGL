package com.alycarter.dragonOfAging.game.object.state.level.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class RoomManager {

	private ArrayList<ArrayList<Room>> rooms;
	
	public RoomManager() {
		rooms = new ArrayList<ArrayList<Room>>();
	}
	
	public void loadRoomLayouts(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(RoomManager.class.getResourceAsStream("/maps/rooms.txt")));
		try {
			String value = reader.readLine();
			while(!value.equals("eof")){
				ArrayList<Room> roomList = new ArrayList<Room>();
				loadRoomFolder(value, Integer.valueOf(reader.readLine()), roomList);
				if(!roomList.isEmpty()){
					rooms.add(roomList);
				}
				value = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void loadRoomFolder(String folderName, int roomCount, ArrayList<Room> roomList){		
		for(int i =0 ; i < roomCount; i++){
			Room room = new Room(folderName);
			room.loadRoom("/maps/"+folderName+"/"+i+".map");
			roomList.add(room);
		}
	}
	
	public Room getRoom(Random random, String roomType){
		boolean found  = false;
		int i = 0;
		Room room = null;
		while(i < rooms.size() && !found){
			if(rooms.get(i).get(0).getRoomType().equals(roomType)){
				found = true;
				room = rooms.get(i).get((int) (random.nextFloat()*rooms.get(i).size()));
			}
			i++;
		}
		return room;
	}

}
