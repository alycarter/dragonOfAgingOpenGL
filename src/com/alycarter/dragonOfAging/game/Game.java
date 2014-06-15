package com.alycarter.dragonOfAging.game;

import java.awt.Point;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.State;
import com.alycarter.dragonOfAging.game.object.state.StateMachine;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Game {
	//if the game will quit at the end of the next loop
	private boolean needsToQuit;
	
	//handles the states. the game will need to quit if empty
	private StateMachine stateMachine;
	
	//handles any drawing and graphics resources
	private Graphics graphics;
	
	//handles input from the user
	private Controls controls;
	
	//amount of time the last frame took
	private float deltaTime;
	
	//size of display
	private Point windowSize;
	
	//title of window
	private String name;
	
	//constructor
	public Game(String name, int width, int height) {
		//the game will need to quit at first
		needsToQuit = true;
		stateMachine = new StateMachine();
		graphics = new Graphics(width, height);
		controls = new Controls();
		deltaTime = 0.0f;
		this.name = name;
		windowSize = new Point(width, height);
	}
	
	public void play(){
		//set up the game to start running
		initialize();
		//set up timing
		long start = 0;
		long end = 0;
		float ns =  1000000000;
		//main game loop
		while(!needsToQuit){
			start = System.nanoTime();
			update();
			render();
			end = System.nanoTime();
			deltaTime = (float)(end - start) / (float)ns;
		}
		//release all resources and end the game
		quit();
	}
	
	private void initialize(){
		//the game no longer needs to end
		initializeDisplay();
		graphics.initialize();
		stateMachine.pushState(new Level("level", graphics));
		needsToQuit = false;
	}
	
	private void initializeDisplay(){
		try {
			Display.setDisplayMode(new DisplayMode(windowSize.x, windowSize.y));
			Display.setTitle(name);
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("error creating game display");
			e.printStackTrace();
		}
	}
	
	private void update(){
		//check for any changes in the controls
		controls.update();
		//update the current state and mark to end the game if their is no states
		State state = stateMachine.getCurrentState(); 
		if(state != null){
			state.update(this);
		}else{
			needsToQuit = true;
		}
		if(Display.isCloseRequested()){
			needsToQuit = true;
		}
		//update the state machine and remove any states that need removing
		stateMachine.update();
		//do any maintenance on graphics resources
		graphics.resourcesUpdate();
	}
	
	private void render(){
		//clear what ever's in the buffer
		graphics.clearScreen();
		//render the current state if there is one
		State state = stateMachine.getCurrentState(); 
		if(state != null){
			state.render(graphics);
		}
		//swap buffers
		Display.update();
	}
	
	private void quit(){
		//clear the state machine and mark all states as needing to be removed
		stateMachine.removeAllStates();
		//free all memory used in graphics
		graphics.destroy();
		//destroy the window and openGL context
		Display.destroy();
	}
	
	public void pushState(State state){
		//add the state to the state machine
		stateMachine.pushState(state);
	}
	
	public float getDeltaTime(){
		//get the amount of time the last frame took
		return deltaTime;
	}
	
	public Controls getControls(){
		return controls;
	}

}
