package com.alycarter.dragonOfAging.game.object.state.level.entity;

import java.util.ArrayList;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.Sprite;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.Map;

public abstract class DynamicEntity extends Entity {

	private Vector3 velocity;
	
	private float drag;
	
	private float weight;
	
	private boolean grounded;
	
	private float maxStepUp;
	
	private float bouncyness;
	
	private Sprite sprite;
	
	private TiledTexture shadow;
	
	public DynamicEntity(Level level, String name, String type, float x, float y, float z,
			float width, float depth, float height, float imageWidth, float imageHeight, boolean collidesWithEntities) {
		super(name, type, x, y, z, width, depth, height, collidesWithEntities);
		sprite = new Sprite(imageWidth, imageHeight, 0, imageHeight/-2.0f);
		shadow = level.getTiledTexture("shadow");
		velocity = new Vector3(0, 0, 0);
		drag = 4.0f;
		weight = 50.0f;
		maxStepUp = 0.2f;
		bouncyness = 0.3f;
		grounded = false;
	}

	public abstract void onUpdate(Level level, Controls controls);
	
	public abstract void onRender(Graphics graphics);
	
	@Override
	public void update(Level level, Controls controls) {
		//entity specific update
		onUpdate(level, controls);
		//add gravity
		velocity.setZ(velocity.getZ() - (level.getDeltaTime()*weight));
		//slow down due to air resistance
		Vector3 deltaDrag = new Vector3(velocity);
		deltaDrag.scale(drag * level.getDeltaTime());
		dampenVelocity(Math.abs(deltaDrag.getX()), Math.abs(deltaDrag.getY()), Math.abs(deltaDrag.getZ()));
		//get the target new position;
		Vector3 target = calculateNewPosition(level.getDeltaTime());
		Vector3 newPosition = new Vector3(target);
		//handle any collisions with the ground
		//is it clear to move straight away
		if(handleTerrainCollision(newPosition, level.getMap())){			
			getPosition().set(newPosition);			
		}else{
			//are we clear to move just on our y and z axis
			newPosition.setX(getPosition().getX());
			if(handleTerrainCollision(newPosition, level.getMap())){			
				velocity.setX(0);
				getPosition().set(newPosition);			
			}else{
				//are we clear to move just on our x and z axis
				newPosition.setX(target.getX());
				newPosition.setY(getPosition().getY());
				if(handleTerrainCollision(newPosition, level.getMap())){
					velocity.setY(0);
					getPosition().set(newPosition);			
				}else{
					//can we move just on our z axis
					newPosition.setX(getPosition().getX());
					if(handleTerrainCollision(newPosition, level.getMap())){
						velocity.setX(0);
						velocity.setY(0);
						getPosition().set(newPosition);
					}
					//don't move if we get to here
				}
			}
		}	
		//handle entity collision
		handleEntityCollisions(level.getEntities());
	}
	
	@Override
	public void render(Graphics graphics) {
		onRender(graphics);
		graphics.drawImage(shadow.getTileTextureID(0), getPosition().getX(),getPosition().getY(), getBoundingBox().getX(), getBoundingBox().getY(), 0);
		sprite.render(graphics, getPosition().getX(), getPosition().getY()-getPosition().getZ());
	}
	
	private void handleEntityCollisions(ArrayList<Entity> entities){
		for(int i = 0; i < entities.size(); i++){
			if(isCollidingWithEntity(entities.get(i))){
				onCollision(entities.get(i));
				entities.get(i).onCollision(this);
				Vector3 push = new Vector3(getPosition());
				push.subtract(entities.get(i).getPosition());
				push.setZ(0);
				push.normalize();
				push.scale(1/weight);
				addForce(push);
			}
		}
	}
	
	private boolean handleTerrainCollision(Vector3 newPosition, Map map){
		//get the position of the ground under our new position
		float groundPos = getMaxGroundHeight(map, newPosition.getX(), newPosition.getY(), getBoundingBox().getX(), getBoundingBox().getY());
		float lastGroundPos = getMaxGroundHeight(map, getPosition().getX(), getPosition().getY(), getBoundingBox().getX(), getBoundingBox().getY());
		//if we fell bellow the ground
		if(newPosition.getZ() < groundPos){
			//if we can step up from last frame and are walking on the floor
			if(groundPos - lastGroundPos < maxStepUp){
				//snap up to the ground
				newPosition.setZ(groundPos);
				velocity.setZ(velocity.getZ()*-bouncyness);
				grounded = true;
				//we can move here now
				return true;
			}else{
				//we are under the block and can't step up
				if(newPosition.getX() == getPosition().getX() && newPosition.getY() == getPosition().getY()){
					//we spawned under the ground
					//snap to the ground
					newPosition.setZ(groundPos);
					return true;
				}
				//we cannot move there at all
				grounded = false;
				return false;
			}
		}else{
			//we are in the air 
			grounded = false;
			//we are good to move here
			return true;
		}
	}
	
	private float getMaxGroundHeight(Map map, float hitBoxX, float hitBoxY, float hitBoxWidth,float hitBoxHeight){
		float height = 0;
		for(int x = (int) Math.floor(hitBoxX - (hitBoxWidth/2)); x <= Math.floor(hitBoxX + (hitBoxWidth/2)); x++){
			for(int y = (int) Math.floor(hitBoxY - (hitBoxHeight/2)); y <= Math.floor(hitBoxY + (hitBoxHeight/2)); y++){
				float tile = map.getHeight(x, y);
				if(tile > height){
					height = tile;
				}
			}
		}
		return height;
	}
	
	public void setDrag(float drag){
		this.drag = drag;
	}
	
	public void setWeight(float weight){
		this.weight = weight;
	}
	
	public void dampenVelocity(float xAmount, float yAmount, float zAmount){
		if(xAmount > Math.abs(velocity.getX())){
			velocity.setX(0);
		}else{
			if(velocity.getX()>0){
				velocity.setX(velocity.getX() - xAmount);
			}else{
				velocity.setX(velocity.getX() + xAmount);
			}
		}
		if(yAmount > Math.abs(velocity.getY())){
			velocity.setY(0);
		}else{
			if(velocity.getY()>0){
				velocity.setY(velocity.getY() - yAmount);
			}else{
				velocity.setY(velocity.getY() + yAmount);
			}
		}
		if(zAmount > Math.abs(velocity.getZ())){
			velocity.setZ(0);
		}else{
			if(velocity.getZ()>0){
				velocity.setZ(velocity.getZ() - zAmount);
			}else{
				velocity.setZ(velocity.getZ() + zAmount);
			}
		}
	}

	private Vector3 calculateNewPosition(float deltaTime){
		Vector3 newPos = new Vector3(getPosition());
		newPos.add(new Vector3(velocity.getX()*deltaTime, velocity.getY()*deltaTime, velocity.getZ()*deltaTime));
		return newPos;
	}
	
	public void addForce(float x, float y, float z){
		velocity.setX(velocity.getX()+x);
		velocity.setY(velocity.getY()+y);
		velocity.setZ(velocity.getZ()+z);
	}
	
	public void addForce(Vector3 force){
		velocity.add(force);
	}

	public void limitSpeed(float speed){
		if(velocity.getLength() > speed){
			velocity.normalize();
			velocity.scale(speed);
		}
	}
	
	public Vector3 getVelocity(){
		return velocity;
	}
	
	public boolean isGrounded(){
		return grounded;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
}
