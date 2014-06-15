package com.alycarter.dragonOfAging.game.object.state.level;

import com.alycarter.dragonOfAging.game.math.Vector3;

public class Camera {
	
	private Vector3 position;
	private float speed;
	
	public Camera(Vector3 position) {
		this.position = new Vector3(position);
		speed =1.5f;
	}
	
	public Vector3 getPosition(){
		return position;
	}
	
	public void update(Level level){
		Vector3 target = new Vector3(level.getPlayer().getPosition());
		target.setY(target.getY()-0.5f);
		target.add(level.getPlayer().getVelocity());
		target.subtract(getPosition());
		target.scale(level.getDeltaTime()*speed);
		position.add(target);
	}
	
}
