package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.GameObject;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public abstract class Entity extends GameObject {
	
	//position in the world
	private Vector3 position;
	//size of the bounding box
	private Vector3 boundingBox;
	//what type of entity it is
	private String entityType;
	// does this collide with entities
	private boolean collidesWithEntities;
	
	public static final String PLAYER_TYPE = "player";
	public static final String ENEMY_TYPE = "enemy";
	public static final String FRIENDLY_TYPE = "friendly";
	public static final String PICKUP_TYPE = "pickup";
	public static final String PROJECTILE_TYPE = "projectile";
	
	
	public Entity(String name, String type, float x, float y, float z, float width, float depth, float height, boolean collidesWithEntities) {
		super(name);
		entityType = type;
		position = new Vector3(x, y, z);
		boundingBox = new Vector3(width, depth, height);
		this.collidesWithEntities = collidesWithEntities;
	}

	public abstract void update(Level level, Controls controls);
	
	public abstract void render(Graphics graphics);
	
	public Vector3 getPosition(){
		return position;
	}
	
	public Vector3 getBoundingBox(){
		return boundingBox;
	}
	
	public String getEntityType(){
		return entityType;
	}
	
	public boolean collidesWithEntities(){
		return collidesWithEntities;
	}
	
	public boolean isCollidingWithEntity(Entity e){
		if(e != this && collidesWithEntities && e.collidesWithEntities){
			if(isIntersectingWithEntity(e)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isIntersectingWithEntity(Entity e){
		float xDistance = Math.abs(getPosition().getX() - e.getPosition().getX());
		float yDistance = Math.abs(getPosition().getY() - e.getPosition().getY());
		float zDistance = Math.abs(getPosition().getZ() - e.getPosition().getZ());
		if(xDistance < (getBoundingBox().getX()/2.0f) + (e.getBoundingBox().getX()/2.0f) &&
					yDistance < (getBoundingBox().getY()/2.0f) + (e.getBoundingBox().getY()/2.0f) &&
					zDistance < (getBoundingBox().getZ()/2.0f) + (e.getBoundingBox().getZ()/2.0f)){
			return true;
		}else{
			return false;
		}
	}
	
	public abstract boolean isOnScreen(float top, float bottom, float left, float right);
	
	public abstract void onCollision(Level level, Entity e);
	
	public abstract void takeDamage(Entity e, float damage);
}
