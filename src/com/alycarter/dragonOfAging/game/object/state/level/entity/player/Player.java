package com.alycarter.dragonOfAging.game.object.state.level.entity.player;

import org.lwjgl.input.Keyboard;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.DynamicEntity;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.Item;

public class Player extends DynamicEntity {

	private Vector3 direction;
	
	private float acceleration;
	
	private ItemManager itemManager;
	
	public static final String PLAYER_TYPE = "player";
	
	public Player(Level level, float x, float y) {
		super(level, "player", PLAYER_TYPE, x, y, 0, 0.5f, 0.25f, 1.5f, 1, 2,true, 10, true, 0.5f);
		itemManager = new ItemManager();
		itemManager.addTypeLimit(Item.ARM_CLOTHING_TYPE);
		itemManager.addTypeLimit(Item.LEG_CLOTHING_TYPE);
		itemManager.addTypeLimit(Item.CHEST_CLOTHING_TYPE);
		itemManager.addTypeLimit(Item.WEAPON_TYPE);
		direction = new Vector3(0, 1, 0);
		acceleration = 15;
		getSprite().appendFrameLayer(level.getTiledTexture("playerBase"));
		for(int i = 0; i < 8; i++){
			//add walking frames
			getSprite().addAnimationTimer(new AnimationTimer(i*4, (i+1)*4, 1.5f, true), false);
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
		itemManager.update(level, this, controls);
		getSprite().update(level.getDeltaTime()*getVelocity().getLength());
	}

	@Override
	public void onRender(Graphics graphics) {
		//calculate animation to use
		double angle = Math.toDegrees(Math.atan2(direction.getX(), direction.getY()));
		angle+=180;
		angle%=360;
		getSprite().setCurrentAnimationTimer((int)(angle/45));
		itemManager.render(graphics);
	}

	public Item pickUpItem(Item item){
		Item oldItem = null;
		oldItem = itemManager.pickUpItem(item);
		if(oldItem != null){
			getSprite().replaceFrameLayer(item.getTexture(), oldItem.getTexture().getName());
		}else{
			getSprite().appendFrameLayer(item.getTexture());
		}
		return oldItem;
	}
	
	public Vector3 getDirection(){
		return direction;
	}
	
	public void setDirection(Vector3 dir){
		direction = new Vector3(dir);
	}

}
