package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Entity;

public class SwordPart extends Entity {
		
	private float damage;
	private FloatColor color;
	private String partType;
	
	public static final String BLADE_PART = "blade";
	public static final String HANDLE_PART = "handle";
	
	
	public SwordPart(String partType, float size, float damage, FloatColor color) {
		super("sword part", PROJECTILE_TYPE, 0, 0, 0, size, size, 1, false);
		this.damage = damage;
		this.color = color;
		this.partType = partType;
	}

	@Override
	public void update(Level level, Controls controls) {
		for(int i = 0 ; i <level.getEntities().size(); i++){
			if(level.getEntities().get(i).getEntityType() == ENEMY_TYPE && isIntersectingWithEntity(level.getEntities().get(i))){
				onCollision(level, level.getEntities().get(i));
				level.getEntities().get(i).onCollision(level, this);
			}
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawRectangle(color, getPosition().getX(), getPosition().getY()-getPosition().getZ(), getPosition().getY(),
				getBoundingBox().getX(), getBoundingBox().getX(), 0);
	}

	@Override
	public boolean isOnScreen(float top, float bottom, float left, float right) {
		return true;
	}

	@Override
	public void onCollision(Level level, Entity e) {
		if(e.getEntityType() == ENEMY_TYPE){
			e.takeDamage(level.getPlayer(), damage);
		}
	}

	@Override
	public void takeDamage(Entity e, float damage) {
		
	}

	public FloatColor getColor(){
		return color;
	}

	public String getPartType(){
		return partType;
	}
}

