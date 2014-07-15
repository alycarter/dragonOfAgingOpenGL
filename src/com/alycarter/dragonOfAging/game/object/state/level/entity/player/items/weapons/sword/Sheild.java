package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class Sheild extends Sword {

	public Sheild(Level level) {
		super("sword", level, -0.2f,0.2f, 0, 0,
				0, 10, 0.5f, 0.5f,
				0.5f, 0.5f, 0.2f, 0.2f,
				FloatColor.GREY, FloatColor.BROWN);
		// TODO Auto-generated constructor stub
	}

}
