package com.alycarter.dragonOfAging.game.object.state.level.entity.player;

import java.util.ArrayList;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.Item;

public class ItemManager {

	private ArrayList<Item> items;
	private ArrayList<String> limitedTypeSlots;
	
	public ItemManager() {
		items = new ArrayList<Item>();
		limitedTypeSlots = new ArrayList<String>();
	}
	
	public Item pickUpItem(Item item){
		Item oldItem = null;
		if(isTypeLimited(item.getItemType())){
			boolean found = false;
			for( int i = 0; !found && i < items.size(); i++){
				if(items.get(i).getItemType().equals(item.getItemType())){
					found = true;
					oldItem = items.remove(i);
				}
			}
		}
		items.add(item);
		return oldItem;
	}
	
	public void addTypeLimit(String type){
		limitedTypeSlots.add(type);
	}
	
	public boolean isTypeLimited(String Type){
		boolean found = false;
		for(int i = 0; i < limitedTypeSlots.size() && !found; i++){
			if(limitedTypeSlots.get(i).equals(Type)){
				found  = true;
			}
		}
		return found;
	}
	
	public void update(Level level, Player player, Controls controls){
		for(int i = 0; i < items.size(); i++){
			items.get(i).update(level, player, controls);
		}
	}
	
	public void render(Graphics graphics){
		for(int i = 0; i < items.size(); i++){
			items.get(i).render(graphics);
		}
	}
	
	public float getDamageResitanceModifier(){
		float res = 0;
		for(int i =0; i < items.size(); i++){
			res += items.get(i).getDamageResistanceModifier();
		}
		if(res < -1){
			res = -1;
		}
		return res;
	}
	
	public float getSpeedModifier(){
		float speed = 0;
		for(int i =0; i < items.size(); i++){
			speed += items.get(i).getSpeedModifier();
		}
		if(speed < -1){
			speed = -1;
		}
		return speed;
	}
}
