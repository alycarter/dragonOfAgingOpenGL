package com.alycarter.dragonOfAging.game.object.state.level.entity;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.AnimationTimer;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.Item;

public class ItemPickUp extends DynamicEntity {

	private Item item;
	private float pickUpDelay;
	
	private static final float PICK_UP_DELAY = 2;
	
	public ItemPickUp(Level level, Item item, float x, float y, float z) {
		super(level,"pick up", "itemPickup", x, y, z, 0.5f, 0.2f, 1,1,2, true);
		this.item=item;
		setWeight(100);
		setDrag(20);
		pickUpDelay = 0;
		getSprite().appendFrameLayer(item.getTexture());
		getSprite().addAnimationTimer(new AnimationTimer(0, 1, 1, false), true);
	}

	@Override
	public void onCollision(Level level, Entity e) {
		super.onCollision(level, e);
		if(e.getEntityType().equals(Player.PLAYER_TYPE)){
			if(item!= null && pickUpDelay <= 0){
				loadNewItem(level.getPlayer().pickUpItem(item));
			}
		}
	}
	
	private void loadNewItem(Item item){
		pickUpDelay = PICK_UP_DELAY;
		this.item = item;
		if(item == null){
			markForRemoval();
		}else{			
			getSprite().replaceFrameLayer(item.getTexture(), 0);
		}
	}

	@Override
	public void onUpdate(Level level, Controls controls) {
		pickUpDelay -= level.getDeltaTime();
	}

	@Override
	public void onRender(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}

}
