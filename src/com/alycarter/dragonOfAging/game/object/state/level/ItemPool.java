package com.alycarter.dragonOfAging.game.object.state.level;

import java.util.ArrayList;
import java.util.Random;

import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.ArmClothing;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.ChestClothing;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.Item;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.LegClothing;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword.CorruptedSword;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword.LongSword;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword.Sheild;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword.Spear;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword.Zweihander;

public class ItemPool {

	private ArrayList<Item> items;
	
	public ItemPool(Level level) {
		items = new ArrayList<Item>();
		reFillList(level);
	}
	
	private void reFillList(Level level){
		items.add(new ArmClothing("leatherArms", level, -0.05f, -0.1f, 0.05f, 0, 0, 0));
		items.add(new ChestClothing("leatherChestPlate", level, -0.05f, -0.1f, 0, 0, 0.05f, 0));
		items.add(new LegClothing("leatherLegs", level, -0.05f, -0.1f, 0, 0.05f, 0, 0));
		items.add(new ArmClothing("ironArms", level, -0.1f, -0.2f, 0.1f, 0, 0, 0));
		items.add(new ChestClothing("ironChestPlate", level, -0.1f, -0.2f, 0, 0, 0.1f, 0));
		items.add(new LegClothing("ironLegs", level, -0.1f, -0.2f, 0, 0.1f, 0, 0));
		items.add(new LongSword(level));
		items.add(new Spear(level));
		items.add(new Zweihander(level));
		items.add(new Sheild(level));
		items.add(new CorruptedSword(level));
	}
	
	public Item getRandomItem(Level level, Random random){
		if(items.isEmpty()){
			reFillList(level);
		}
		if(!items.isEmpty()){
			return items.remove((int) (random.nextFloat()*items.size()));			
		}else{
			return null;
		}
	}

}
