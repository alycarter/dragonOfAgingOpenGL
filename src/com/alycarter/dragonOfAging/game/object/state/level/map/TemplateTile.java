package com.alycarter.dragonOfAging.game.object.state.level.map;

import java.awt.Point;

public class TemplateTile extends Tile {

	private Point position;
	private boolean editedHeight;
	private boolean editedTexture;
	private boolean useNoise;
	
	public TemplateTile() {
		super(0, 0, 0, false);
		position = new Point(0, 0);
		setEditedHeight(false);
		setEditedTexture(false);
		setUseNoise(false);
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(int x, int y) {
		this.position.setLocation(x, y);
	}

	public boolean isEditedHeight() {
		return editedHeight;
	}

	public void setEditedHeight(boolean editedHeight) {
		this.editedHeight = editedHeight;
	}

	public boolean isEditedTexture() {
		return editedTexture;
	}

	public void setEditedTexture(boolean editedTexture) {
		this.editedTexture = editedTexture;
	}
	
	public boolean isEdited(){
		return editedHeight || editedTexture;
	}

	public boolean isUseNoise() {
		return useNoise;
	}

	public void setUseNoise(boolean useNoise) {
		this.useNoise = useNoise;
	}

}
