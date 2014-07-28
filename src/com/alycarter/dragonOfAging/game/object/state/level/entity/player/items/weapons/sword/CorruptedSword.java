package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class CorruptedSword extends Sword {

	public CorruptedSword(Level level) {
		super("sword", level, -0.2f, 0.2f, 0.2f, 0.2f,
				0.2f, 20, 2.5f, 1.0f, 0.1f, 1.0f, 0.05f, 0.2f,
				FloatColor.BLACK, new FloatColor(0.5f, 0.0f, 0.5f, 1.0f));
	}

}
