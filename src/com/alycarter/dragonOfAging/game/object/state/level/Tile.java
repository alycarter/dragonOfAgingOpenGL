package com.alycarter.dragonOfAging.game.object.state.level;

import java.awt.Point;

public class Tile {

	private int texture;
	private float renderHeight;
	private float collisionHeight;
	private boolean editedHeight;
	private boolean editedTexture;
	private boolean useNoise;
	private Point position;
	
	public Tile(int x, int y, int texture, float renderHeight, float collisionHeight) {
		editedHeight = false;
		editedTexture = false;
		setUseNoise(true);
		setTexture(texture);
		setRenderHeight(renderHeight);
		setCollisionHeight(collisionHeight);
		position =new Point(x, y);
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

	public boolean isEdited() {
		return editedHeight || editedTexture;
	}
	
	public boolean getEditedTexture(){
		return editedTexture;
	}
	
	public boolean getEditedHeight(){
		return editedHeight;
	}
	
	public void setEditedTexture(boolean edited) {
		editedTexture = edited;
	}
	
	public void setEditedHeight(boolean edited) {
		editedHeight = edited;
	}
	
	public void setHeight(float height){
		setCollisionHeight(height);
		setRenderHeight(height);
	}
	
	public Point getPosition(){
		return position;
	}
	
	public void setPosition(int x, int y){
		position.setLocation(x, y);
	}

	public boolean isUsingNoise() {
		return useNoise;
	}

	public void setUseNoise(boolean useNoise) {
		this.useNoise = useNoise;
	}
	
}
