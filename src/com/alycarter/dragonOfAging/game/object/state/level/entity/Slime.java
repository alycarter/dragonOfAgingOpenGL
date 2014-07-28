package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Slime extends DynamicEntity {

	private float jumpDelay = 2;
	
	public Slime(Level level, Vector3 position) 	{
		super(level,"slime", ENEMY_TYPE, position, 0.7f, 0.3f, 1.0f, 1.0f, 1.0f,true,5,true, 0.25f);
		getSprite().appendFrameLayer(level.getTiledTexture("slime"));
		//normal
		getSprite().addAnimationTimer(new AnimationTimer(0, 1, 1, false), true);
		//jump
		getSprite().addAnimationTimer(new AnimationTimer(1, 3, 4, false), false);
		//air
		getSprite().addAnimationTimer(new AnimationTimer(3, 4, 1, false), false);
		//land
		getSprite().addAnimationTimer(new AnimationTimer(4, 6, 8, false), false);
		setBaseColor(level.getMap().getLevelType().getLevelColor());
	}

	@Override
	public void onUpdate(Level level, Controls controls) {
		//jumping stuff
		switch (getSprite().getCurrentAnimationTimerID()) {
		case 0:
			if(isGrounded() && jumpDelay < 0 && level.getPlayer().getPosition().distanceTo(getPosition())< 10){
				//start the squash animation
				getSprite().setCurrentAnimationTimer(1, true);
				jumpDelay = 2+(float)Math.random();
			}
			break;
		case 1:
			if(getSprite().getCurrentAnimationTimer().hasEnded()){
				//jump
				Vector3 direction = new Vector3(level.getPlayer().getPosition());
				direction.subtract(getPosition());
				direction.normalize();
				direction.scale(5);
				direction.setZ(10);
				addForce(direction);
				getSprite().setCurrentAnimationTimer(2);
			}
			break;
		case 2:
			if(isGrounded()){
				//start the landing squash animation
				getSprite().setCurrentAnimationTimer(3,true);
				dampenVelocity(2, 2, 2);
				//create particles
				for(int i =0 ; i< 10; i++){
					float x = ((float)Math.random()*2)-1;
					float y = 1-Math.abs(x);
					if(Math.random()>0.5){
						y = -y;
					}
					Vector3 direction = new Vector3(x, y, 0);
					direction.normalize();
					direction.setZ(1.0f);
					level.getParticles().createParticle(false, 3, 0.1f, getPosition().getX()+(x*getBoundingBox().getX()/2), getPosition().getY(), getPosition().getZ(),
							getBaseColor().getR(), getBaseColor().getG(), getBaseColor().getB(), getBaseColor().getA(), direction.getX(), direction.getY(), direction.getZ(), 5);
				}
			}
			break;
		case 3:
			if(getSprite().getCurrentAnimationTimer().hasEnded()){
				//go back to normal animation
				getSprite().setCurrentAnimationTimer(0);
			}
			break;
		default:
			break;
		}
		getSprite().update(level.getDeltaTime());
		jumpDelay -= level.getDeltaTime();
	}

	@Override
	public void onRender(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCollision(Level level, Entity e) {
		super.onCollision(level, e);
		if(e.getEntityType().equals(PLAYER_TYPE)){
			e.takeDamage(e, 1);
		}
	}

}
