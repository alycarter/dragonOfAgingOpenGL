package com.alycarter.dragonOfAging.game.object.state.level.map;


public class Tile {

	private int texture;
	private float renderHeight;
	private float collisionHeight;
	private boolean reserved;
	
	public Tile(int texture, float renderHeight, float collisionHeight, boolean reserved) {
		setTexture(texture);
		setRenderHeight(renderHeight);
		setCollisionHeight(collisionHeight);
		setReserved(reserved);
	}

	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}

	public float getRenderHeight() {
		return renderHeight;
	}

	public void setRenderHeight(float renderHeight) {
		this.renderHeight = renderHeight;
	}

	public float getCollisionHeight() {
		return collisionHeight;
	}

	public void setCollisionHeight(float collisionHeight) {
		this.collisionHeight = collisionHeight;
	}

	public void setHeight(float height){
		setCollisionHeight(height);
		setRenderHeight(height);
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
}
