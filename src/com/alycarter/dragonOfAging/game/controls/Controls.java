package com.alycarter.dragonOfAging.game.controls;

import java.util.ArrayList;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.input.ControllerAdapter;


public class Controls {

	private ArrayList<Integer> keysHeld = new ArrayList<Integer>();
	private ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	private ArrayList<Integer> keysTyped = new ArrayList<Integer>();
	
	private Controller controller;
	
	public Controls(){
		try {
			Controllers.create();
			controller = Controllers.getController(0);
			for(int i =0; i < controller.getAxisCount(); i++){
				controller.setDeadZone(i, 0.20f);
			}		
		} catch (Exception e) {
			controller = new ControllerAdapter();
		}
		System.out.println(controller.getName() + " connected");
	}
	
	public void update(){
		controller.poll();
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
	
	public Controller getController(){
		return controller;
	}
}
