package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons;

import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.Item;

public abstract class Weapon extends Item {

	public Weapon(String name, Level level, float speedModifier, float damageResistanceModifier) {
		super(WEAPON_TYPE, name, level, speedModifier, damageResistanceModifier);
		// TODO Auto-generated constructor stub
	}
	
}
