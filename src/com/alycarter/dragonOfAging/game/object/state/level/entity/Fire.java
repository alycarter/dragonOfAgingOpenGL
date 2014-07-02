package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Animation;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Fire extends Entity {

	private Animation flicker;
	
	private float smokeDelay;
	private float emberDelay;
	
	public Fire(Level level, Vector3 position){
		super("fire", Entity.FRIENDLY_TYPE, position.getX(), position.getY(), position.getZ(), 0.75f, 0.4f, 1, true);
		flicker = new Animation(level.getTiledTexture("fire"));
		flicker.addAnimationTimer(new AnimationTimer(0, 1, 6, true), true);
		smokeDelay = 0;
	}

	@Override
	public void update(Level level, Controls controls) {
		flicker.update(level.getDeltaTime());
		smokeDelay -= level.getDeltaTime();
		if(smokeDelay < 0){
			smokeDelay = (float)Math.random()/10.0f;
			level.getParticles().createParticle(false, 3, 0.1f+(0.1f *(float)Math.random()), getPosition().getX(), getPosition().getY(), getPosition().getZ()+0.5f, 
					0.2f, 0.2f, 0.2f, 1.0f, (float)(Math.random()/2.0f)-0.25f, (float)(Math.random()/2.0f)-0.25f, 0.75f, 0.1f);
		}
		emberDelay -= level.getDeltaTime();
		if(emberDelay < 0){
			emberDelay = (float)Math.random()/6.0f;
			level.getParticles().createParticle(false, (float)Math.random()*2, 0.075f, getPosition().getX(), getPosition().getY(), getPosition().getZ()+0.25f, 
					1.0f, (float)Math.random(), 0.0f, 1.0f, (float)(Math.random()/2.0f)-0.25f, (float)(Math.random()/2.0f)-0.25f, 1, 0.2f);
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(flicker.getFrameTextureID(), getPosition().getX(), getPosition().getY()-getPosition().getZ()-0.5f, getPosition().getY(), 1, 1, 0);
	}

	@Override
	public boolean isOnScreen(float top, float bottom, float left, float right) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onCollision(Level level, Entity e) {
		e.takeDamage(this, 1);

	}

	@Override
	public void takeDamage(Entity e, float damage) {
		// TODO Auto-generated method stub

	}

}
