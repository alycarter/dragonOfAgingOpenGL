package com.alycarter.dragonOfAging.game.object.state;

import com.alycarter.dragonOfAging.game.Game;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.GameObject;

public abstract class State extends GameObject {

	public State(String name) {
		super(name);
	}

	public abstract void update(Game game);
	
	public abstract void render(Graphics graphics);
	
}
