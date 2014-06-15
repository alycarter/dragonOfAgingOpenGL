package com.alycarter.dragonOfAging.game.object.state.level.uiObjects;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.uiObject.UIObject;

public abstract class LevelUIObject extends UIObject{

	public LevelUIObject(String name) {
		super(name);
	}
	
	public abstract void update(Level level, Controls controls);

}
