package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items;

import com.alycarter.dragonOfAging.game.graphics.TiledTexture;

public class Item {

	public static final String ARM_CLOTHING_TYPE = "arm";
	public static final String CHEST_CLOTHING_TYPE = "chest";
	public static final String LEG_CLOTHING_TYPE = "legs";
	public static final String HEAD_CLOTHING_TYPE = "legs";
	public static final String WEAPON_TYPE = "legs";
	public static final String STAT_TYPE = "legs";
	
	
	private String itemType;
	private String itemName;
	private TiledTexture texture;
	
	public Item(String type, String name, TiledTexture texture) {
		this.texture = texture;
		itemName = name;
		itemType = type;
	}
	
	public String getItemType(){
		return itemType;
	}
	
	public String getItemName(){
		return itemName;
	}
	
	public TiledTexture getTexture(){
		return texture;
	}

}
