package com.alycarter.dragonOfAging.game.object.state.level.entity;

import org.lwjgl.input.Keyboard;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;


public class Player extends DynamicEntity {

	private Vector3 direction;
	
	private float acceleration;
	
	public Player(Level level, float x, float y) {
		super(level, "player", "player", x, y, 0, 0.5f, 0.25f, 1.5f, 1, 2,true);
		direction = new Vector3(0, 1, 0);
		acceleration = 15;
		getSprite().appendFrameLayer(level.getTiledTexture("playerBase"));
		getSprite().appendFrameLayer(level.getTiledTexture("ironArms"));
		getSprite().appendFrameLayer(level.getTiledTexture("ironLegs"));
		getSprite().appendFrameLayer(level.getTiledTexture("ironChestPlate"));
		for(int i = 0; i < 8; i++){
			//add walking frames
			getSprite().addAnimationTimer(new AnimationTimer(i*4, (i+1)*4, 1.5f, true), false);
		}
		for(int i = 0; i < 8; i++){
			//add sliding frames
			getSprite().addAnimationTimer(new AnimationTimer((i*4)+1, (i*4)+2, 1.5f, true), false);
		}
		for(int i = 0; i < 8; i++){
			//add standing frames
			getSprite().addAnimationTimer(new AnimationTimer(i*4, (i*4)+1, 1.5f, true), true);
		}
	}

	@Override
	public void onUpdate(Level level, Controls controls) {
		float x = 0;
		float y = 0;
		if(controls.isKeyHeld(Keyboard.KEY_W)){
			y--;
		}
		if(controls.isKeyHeld(Keyboard.KEY_S)){
			y++;
		}
		if(controls.isKeyHeld(Keyboard.KEY_A)){
			x--;
		}
		if(controls.isKeyHeld(Keyboard.KEY_D)){
			x++;
		}
		if(x != 0 || y != 0){
			direction.setX(x);
			direction.setY(y);
			direction.normalize();
			Vector3 force = new Vector3(direction);
			force.scale(level.getDeltaTime()*acceleration);
			addForce(force);		
		}else{
			dampenVelocity(level.getDeltaTime()*acceleration, level.getDeltaTime()*acceleration, 0);
		}
		if(controls.isKeyHeld(Keyboard.KEY_SPACE) && isGrounded()){
			addForce(0, 0, 10);
		}
		getSprite().update(level.getDeltaTime()*getVelocity().getLength());
	}

	@Override
	public void onRender(Graphics graphics, float unitResolution, float renderOffsetX, float renderOffsetY) {
		//calculate animation to use
		double angle = Math.toDegrees(Math.atan2(direction.getX(), direction.getY()));
		angle+=180;
		angle%=360;
		int offset = 0;
		if(getVelocity().dot(direction)<0){
			offset=8;
		}
		getSprite().setCurrentAnimationTimer((int)(angle/45)+offset);
	}

	@Override
	public void onCollision(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
