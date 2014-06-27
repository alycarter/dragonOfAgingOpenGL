package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import org.lwjgl.input.Keyboard;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.Weapon;

public class Sword extends Weapon {

	private Vector3 direction;
	
	private float swordLength;
	
	public Sword(String name, Level level, float swordLength) {
		super(name, level);
		this.swordLength = swordLength;
		direction = new Vector3(0, 0, 0);
	}
	
	@Override
	public void update(Level level, Player player, Controls controls) {
		//get the vector of the direction the player is wanting the sword to go
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
		//scale it by the amount of movement the sword can do in 1 frame
		swordDir.scale(level.getDeltaTime()*10);
		//add the movement to the current sword direction
		direction.add(swordDir);
		if(direction.getLength() > 1){
			direction.normalize();
		}
		//if the player had an input
		if(swordDir.getLength() != 0){
			//get a normilized version of the sword direction
			Vector3 normDirection = direction.getNormalized();
			//make the player face the way we are swinging
			player.setDirection(swordDir);
			//get the direction of the sword steps
			swordDir = new Vector3(direction);
			swordDir.scale(0.1f);
			//get the position on the sword in relation to the player
			Vector3 swordPos = new Vector3(normDirection);
			//rotate the sword to the players hand
			float handPos = 90.0f;
			swordPos.setX((float)((swordPos.getX() * Math.cos(handPos)) - (swordPos.getY() * Math.sin(handPos))));
			swordPos.setY((float)((swordPos.getY() * Math.cos(handPos)) + (swordPos.getX() * Math.sin(handPos))));
			//move the sword to align with the player bounding box
			swordPos.setX(swordPos.getX() * player.getBoundingBox().getX()/2.0f);
			swordPos.setY(swordPos.getY() * player.getBoundingBox().getY()/2.0f);
			//move the sword to world space
			swordPos.add(player.getPosition());
			//move the sword out a little more
			swordPos.add(swordDir);
			swordPos.add(swordDir);
			//make the segments of the sword
			for(int i =0; i < swordLength*10; i++){
				swordPos.add(swordDir);
				level.getEntities().add(new SwordPart(swordPos.getX(),swordPos.getY(), player.getPosition().getZ()+(player.getBoundingBox().getZ()/2.5f), 0.1f));
			}
		}
	}

}
