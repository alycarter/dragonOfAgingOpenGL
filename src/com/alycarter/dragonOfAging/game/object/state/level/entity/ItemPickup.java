package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class ItemPickup extends Entity {

	public ItemPickup(String name, String type, float x, float y, float z,
			float width, float depth, float height, boolean collidesWithEntities) {
		super(name, type, x, y, z, width, depth, height, collidesWithEntities);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Level level, Controls controls) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics graphics, float unitResolution,
			float renderOffsetX, float renderOffsetY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollision(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
