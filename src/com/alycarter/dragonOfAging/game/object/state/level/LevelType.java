package com.alycarter.dragonOfAging.game.object.state.level;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;

public class LevelType {

	private int textureOffset;
	private FloatColor levelColor;
	private String levelName;
	
	private LevelType(int textureOffset, FloatColor levelColor, String levelName) {
		this.textureOffset = textureOffset;
		this.levelColor = levelColor;
		this.levelName =levelName;
	}

	public int getTextureOffset() {
		return textureOffset;
	}

	public FloatColor getLevelColor() {
		return levelColor;
	}

	public String getLevelName() {
		return levelName;
	}
	
	public static final String FOREST_NAME = "forest";
	public static final String LAVA_NAME = "lava";
	public static final String CAVE_NAME = "cave";
	public static final String DESERT_NAME = "desert";
	public static final String SNOW_NAME = "snow";
	public static final String DUNGEON_NAME = "dungeon";
	
	
	public static final LevelType FOREST_LEVEL = new LevelType(0, new FloatColor(0.3f, 1.0f, 0.3f, 1.0f), FOREST_NAME);
	public static final LevelType LAVA_LEVEL = new LevelType(1, new FloatColor(1.0f, 0.2f, 0.2f, 1.0f), LAVA_NAME);
	public static final LevelType CAVE_LEVEL = new LevelType(2, new FloatColor(0.7f, 0.7f, 1.0f, 1.0f), CAVE_NAME);
	public static final LevelType DESERT_LEVEL = new LevelType(3, FloatColor.YELLOW, DESERT_NAME);
	public static final LevelType SNOW_LEVEL = new LevelType(4, new FloatColor(0.9f, 0.9f, 0.9f, 1.0f), SNOW_NAME);	
	public static final LevelType DUNGEON_LEVEL = new LevelType(5, new FloatColor(0.6f, 0.6f, 0.6f, 0.6f), DUNGEON_NAME);	

}
