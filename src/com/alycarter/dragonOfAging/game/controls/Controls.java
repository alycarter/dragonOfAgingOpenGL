package com.alycarter.dragonOfAging.game.controls;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;


public class Controls {

	private ArrayList<Integer> keysHeld = new ArrayList<Integer>();
	private ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	private ArrayList<Integer> keysTyped = new ArrayList<Integer>();
	
	public Controls(){

	}
	
	public void update(){
		keysHeld.addAll(keysPressed);
		keysPressed.clear();
		keysTyped.clear();
		while(Keyboard.next()){
			int keyCode = Keyboard.getEventKey();
			if(Keyboard.getEventKeyState() == true){
				keysPressed.add(keyCode);
			}else{
				keysHeld.remove(Integer.valueOf(keyCode));
				keysTyped.add(keyCode);
			}
		}
	}
	
	public boolean isKeyPressed(int keyCode){
		return keysPressed.contains(Integer.valueOf(keyCode));
	}
	
	public boolean isKeyHeld(int keyCode){
		return keysHeld.contains(Integer.valueOf(keyCode));
	}
	
	public boolean isKeyTyped(int keyCode){
		return keysTyped.contains(Integer.valueOf(keyCode));
	}
}
