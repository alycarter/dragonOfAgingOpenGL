package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Bird extends DynamicEntity {

	public Bird(Level level, Vector3 position) {
		super(level, "bird", Entity.ENEMY_TYPE, position, 0.5f, 0.25f, 0.25f, 1.0f,
				1.0f, true, 1, true,0.25f);
		setDrag(0.25f);
		setWeight(1.5f);
		getSprite().appendFrameLayer(level.getTiledTexture("bird"));
		for(int i = 0; i <8; i++){
			getSprite().addAnimationTimer(new AnimationTimer(i*4, (i*4)+1, 0, true), true);			
		}
		for(int i = 0; i <8; i++){
			getSprite().addAnimationTimer(new AnimationTimer(i*4, (i+1)*4, 8, true), true);			
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUpdate(Level level, Controls controls) {
		Vector3 dir = new Vector3(level.getPlayer().getPosition());
		dir.subtract(getPosition());
		dir.setZ(0);
		dir.normalize();
		dir.scale(level.getDeltaTime() * 10);
		addForce(dir);
		if(getPosition().getZ()-level.getMap().getHeight((int)getPosition().getX(), (int)getPosition().getY()) < 0.25f && getVelocity().getZ() < 0){
			addForce(0, 0, 4);
			dir.set(getVelocity());
			dir.setZ(0);
			dir.normalize();
			dir.scale(3);
			addForce(dir);
		}
		getSprite().update(level.getDeltaTime());
	}

	@Override
	public void onRender(Graphics graphics) {
		double angle = Math.toDegrees(Math.atan2(getVelocity().getX(), getVelocity().getY()));
		angle+=202.5f;
		angle%=360;
		int offset = (int)(angle/45);
		if(getVelocity().getZ() > 0){
			offset += 8;			
		}
		getSprite().setCurrentAnimationTimer(offset);
		
	}
	
	@Override
	public void onCollision(Level level, Entity e) {
		if(e == level.getPlayer()){
			e.takeDamage(level, this, 0.5f);
		}
	}

}
