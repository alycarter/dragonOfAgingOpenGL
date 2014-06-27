package com.alycarter.dragonOfAging.game.object.state.level.particle;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Particle{

	private float size;
	
	private float decay;
	
	private Vector3 position;
	
	private Vector3 velocity;
	
	private FloatColor color;
	
	public Particle() {
		decay = 0;
		size = 0;
		color = new FloatColor(1.0f, 1.0f, 1.0f, 1.0f);
		position = new Vector3();
		velocity = new Vector3();
	}
	
	public void create(float timeToLive, float size, float positionX, float positionY, float positionZ,
			float r, float g, float b, float a, float velocityX, float velocityY, float velocityZ){
		color.setR(r);
		color.setB(b);
		color.setG(g);
		color.setA(a);
		position.setX(positionX);
		position.setY(positionY);
		position.setZ(positionZ);
		velocity.setX(velocityX);
		velocity.setY(velocityY);
		velocity.setZ(velocityZ);
		decay = size / timeToLive;
		this.size = size;
	}

	public void update(Level level) {
		size -= level.getDeltaTime() * decay;
		velocity.setZ(velocity.getZ() - level.getDeltaTime()*5);
		Vector3 deltaVelocity = new Vector3(velocity);
		deltaVelocity.scale(level.getDeltaTime());
		position.add(deltaVelocity);
		if(position.getZ() < level.getMap().getHeight((int)position.getX(), (int)position.getY())){
			size = 0;
		}
	}

	public boolean isAlive(){
		if(size > 0){
			return true;
		}else{
			return false;
		}
	}

	public void render(Graphics graphics) {
		graphics.drawRectangle(color, position.getX(), position.getY()-position.getZ(), position.getY(), size, size, 0);
	}
	
	public Vector3 getPosition(){
		return position;
	}

	public float getSize(){
		return size;
	}
	
	public boolean isOnScreen(float top, float bottom, float left, float right){
		return (((getPosition().getY()+getPosition().getZ()+(size/2)) > top) &&
				((getPosition().getY() - getPosition().getZ() - size) < bottom) &&
				((getPosition().getX()+size > left)) &&
				((getPosition().getX()-size < right)));
	}
	
}
