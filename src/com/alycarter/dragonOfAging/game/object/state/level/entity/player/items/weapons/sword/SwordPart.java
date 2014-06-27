package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Entity;

public class SwordPart extends Entity {
		
	public SwordPart(float x, float y, float z, float size) {
		super("sword part", PROJECTILE_TYPE, x, y, z, size, size, 2, false);
	}

	@Override
	public void update(Level level, Controls controls) {
		for(int i = 0 ; i <level.getEntities().size(); i++){
			if(level.getEntities().get(i).getEntityType() == ENEMY_TYPE && isIntersectingWithEntity(level.getEntities().get(i))){
				onCollision(level, level.getEntities().get(i));
				level.getEntities().get(i).onCollision(level, this);
			}
		}
		level.getParticles().createParticle(false, 0.1f, 0.1f, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
				0.5f, 0.5f, 0.5f, 1.0f, 0, 0, 0);
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawRectangle(FloatColor.GREY, getPosition().getX(), getPosition().getY()-getPosition().getZ(), getPosition().getY(),
				getBoundingBox().getX(), getBoundingBox().getX(), 0);
		markForRemoval();
	}

	@Override
	public boolean isOnScreen(float top, float bottom, float left, float right) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onCollision(Level level, Entity e) {
		if(e.getEntityType() == ENEMY_TYPE){
			e.takeDamage(level.getPlayer(), 1);
		}
	}

	@Override
	public void takeDamage(Entity e, float damage) {
		// TODO Auto-generated method stub

	}

}
