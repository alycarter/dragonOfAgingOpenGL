package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items;

import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Stat extends Item {

	public Stat(String name, Level level, float speedModifier, float damageResistanceModifier, float armStrain, float legStrain, float chestStrain, float headStrain) {
		super(STAT_TYPE, name, level, speedModifier, damageResistanceModifier, armStrain, legStrain, chestStrain, headStrain);
		// TODO Auto-generated constructor stub
	}

}
