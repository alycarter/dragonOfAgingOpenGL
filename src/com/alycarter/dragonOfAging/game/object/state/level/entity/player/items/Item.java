package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;

public class Item {

	public static final String ARM_CLOTHING_TYPE = "arm";
	public static final String CHEST_CLOTHING_TYPE = "chest";
	public static final String LEG_CLOTHING_TYPE = "legs";
	public static final String HEAD_CLOTHING_TYPE = "head";
	public static final String WEAPON_TYPE = "weapon";
	public static final String STAT_TYPE = "stat";
	
	
	private String itemType;
	private String itemName;
	private TiledTexture texture;
	
	private float speedModifier;
	private float damageResistanceModifier;
	
	public Item(String type, String name, Level level, float speedModifier, float damageResistanceModifier) {
		texture = level.getTiledTexture(name);
		itemName = name;
		itemType = type;
		this.setSpeedModifier(speedModifier);
		this.setDamageResistanceModifier(damageResistanceModifier);
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
	
	public void update(Level level, Player player, Controls controls){
		
	}
	
	public void render(Graphics graphics){
		
	}

	public float getSpeedModifier() {
		return speedModifier;
	}

	public void setSpeedModifier(float speedModifier) {
		this.speedModifier = speedModifier;
	}

	public float getDamageResistanceModifier() {
		return damageResistanceModifier;
	}

	public void setDamageResistanceModifier(float damageResistanceModifier) {
		this.damageResistanceModifier = damageResistanceModifier;
	}

}
