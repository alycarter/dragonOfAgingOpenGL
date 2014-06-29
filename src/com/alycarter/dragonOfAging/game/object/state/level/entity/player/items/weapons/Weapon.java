package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons;

import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.Item;

public abstract class Weapon extends Item {

	public Weapon(String name, Level level, float speedModifier, float damageResistanceModifier, float armStrain, float legStrain, float chestStrain, float headStrain) {
		super(WEAPON_TYPE, name, level, speedModifier, damageResistanceModifier, armStrain, legStrain, chestStrain, headStrain);
		// TODO Auto-generated constructor stub
	}
	
}
