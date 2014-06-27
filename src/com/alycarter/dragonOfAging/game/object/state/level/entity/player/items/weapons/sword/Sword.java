package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import org.lwjgl.input.Keyboard;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.Weapon;

public class Sword extends Weapon {

	private float delay;
	private Vector3 direction;
	
	public Sword(String name, Level level) {
		super(name, level);
		delay = 0;
		direction = new Vector3(0, 0, 0);
	}
	
	@Override
	public void update(Level level, Player player, Controls controls) {
		float x = 0;
		float y = 0;
		if(controls.isKeyHeld(Keyboard.KEY_J)){
			x--;
		}		
		if(controls.isKeyHeld(Keyboard.KEY_L)){
			x++;
		}		
		if(controls.isKeyHeld(Keyboard.KEY_I)){
			y--;
		}	
		if(controls.isKeyHeld(Keyboard.KEY_K)){
			y++;
		}	
		Vector3 swordDir = new Vector3(x, y, 0);
		swordDir.normalize();
		swordDir.scale(level.getDeltaTime()*10);
		direction.add(swordDir);
		if(direction.getLength() > 1){
			direction.normalize();
		}
		if( delay < 0 && swordDir.getLength() != 0){
			swordDir = new Vector3(direction);
			swordDir.scale(0.1f);
			Vector3 swordPos = new Vector3(player.getPosition());
			for(int i =0; i < 10; i++){
				swordPos.add(swordDir);
				level.getEntities().add(new SwordPart(swordPos.getX(),swordPos.getY(), player.getPosition().getZ()+(player.getBoundingBox().getZ()/2.5f), 0.1f));
			}
			delay = 0.01f;
		}
		delay-=level.getDeltaTime();
	}

}
