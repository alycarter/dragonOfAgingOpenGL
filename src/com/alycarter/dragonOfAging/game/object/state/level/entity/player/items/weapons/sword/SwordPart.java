package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Entity;

public class SwordPart extends Entity {

	public SwordPart(String name, String type, float x, float y, float z,
			float width, float depth, float height, boolean collidesWithEntities) {
		super(name, type, x, y, z, width, depth, height, collidesWithEntities);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Level level, Controls controls) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics graphics) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOnScreen(float top, float bottom, float left, float right) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCollision(Level level, Entity e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void takeDamage(Entity e, float damage) {
		// TODO Auto-generated method stub

	}

}
