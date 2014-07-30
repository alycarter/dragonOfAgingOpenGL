package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class AirSpawner extends DynamicEntity {

	private float spawnTimer;
	
	private static final float SPAWN_DELAY = 5;
	
	public AirSpawner(Level level, float x, float y, float z) {
		super(level, "airSpawner", Entity.ENEMY_TYPE, x, y, z, 1.5f, 0.5f, 2, 2, 2, true, 15, true, 0.5f);
		getSprite().setImageOffSet(0, -0.8f);
		getSprite().appendFrameLayer(level.getTiledTexture("airSpawner"));
		getSprite().addAnimationTimer(new AnimationTimer(0, 1, 0, true), true);
		getSprite().addAnimationTimer(new AnimationTimer(0, 4, 16, true), true);
		spawnTimer = SPAWN_DELAY;
		setWeight(50);
	}

	@Override
	public void onUpdate(Level level, Controls controls) {
		getSprite().update(level.getDeltaTime());
		if(spawnTimer < 0.5f || getDamageCoolDown() > 0){
			getSprite().setCurrentAnimationTimer(1);
		}else{
			getSprite().setCurrentAnimationTimer(0);
		}
		if(spawnTimer < 0 && getPosition().distanceTo(level.getPlayer().getPosition()) < 10){
			spawnBird(level);
		}
		spawnTimer -= level.getDeltaTime();
	}

	@Override
	public void onRender(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCollision(Level level, Entity e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDamage(Level level, Entity e) {
		spawnBird(level);
	}
	
	private void spawnBird(Level level){
		Vector3 pos = new Vector3(getPosition());
		pos.setZ(pos.getZ()+1.5f);
		Bird bird = new Bird(level, pos);
		pos.setX((float)Math.random()-0.5f);
		pos.setY((float)Math.random()-0.5f);
		pos.setZ(0);
		pos.normalize();
		pos.scale(5);
		pos.setZ(1);
		bird.addForce(pos);
		level.getEntities().add(bird);
		spawnTimer = SPAWN_DELAY;
	}

}
