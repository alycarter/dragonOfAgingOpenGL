package com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.sword;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.math.Vector3;
import com.alycarter.dragonOfAging.game.object.state.level.Level;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.items.weapons.Weapon;

public class Sword extends Weapon {

	private ArrayList<SwordPart> parts;
	
	private Vector3 direction;
	
	private float swingSpeed;
	
	private float delay;
	
	private float particleDelay;
	
	private float baseSpeedModifier;
	
	public Sword(String name, Level level, float speedModifier, float armStrain, float legStrain, float chestStrain, float headStrain, float swingSpeed, float bladeDamage, float handleDamage,
			float bladePartSize, float bladeLength, float handlePartSize, float handleLength, FloatColor bladeColor, FloatColor handleColor) {
		super(name, level, speedModifier, 0, armStrain, legStrain, chestStrain, headStrain);
		parts = new ArrayList<SwordPart>();
		for(int i = 0; i < handleLength/handlePartSize; i++){
			parts.add(new SwordPart(SwordPart.HANDLE_PART, handlePartSize, handleDamage, handleColor));
		}
		for(int i = 0; i < bladeLength/bladePartSize; i++){
			parts.add(new SwordPart(SwordPart.BLADE_PART, bladePartSize, bladeDamage, bladeColor));
		}
		direction = new Vector3(0, 0, 0);
		this.swingSpeed = swingSpeed;
		delay = 0;
		particleDelay = handlePartSize/swingSpeed;
		baseSpeedModifier = speedModifier;
	}
	
	@Override
	public void update(Level level, Player player, Controls controls) {
		//get the vector of the direction the player is wanting the sword to go
		Vector3 swordDir = getSwordSwingDirection(controls);
		if(swordDir.getLength() != 0){
			swordDir.normalize();
			//scale it by the amount of movement the sword can do in 1 frame
			swordDir.scale(level.getDeltaTime()*getSwingSpeed(player));
			//add the movement to the current sword direction
			direction.add(swordDir);
			if(direction.getLength() > 1){
				direction.normalize();
			}
			//make the player face the way we are swinging
			player.setDirection(direction.getNormalized());
		}else{
			//start sheathing sword;
			swordDir = new Vector3(direction.getNormalized());
			swordDir.scale(-1);
			swordDir.scale(level.getDeltaTime()*getSwingSpeed(player));
			if(swordDir.getLength() < direction.getLength()){
				direction.add(swordDir);
			}else{
				direction.scale(0);
			}
		}
		if(!isSwordSheithed()){
			setSpeedModifier(baseSpeedModifier*2);
			//get the position on the sword in relation to the player
			Vector3 swordPos = getSwordPosition(player);	
			updateSwordParts(swordPos, level, controls);
		}else{
			setSpeedModifier(baseSpeedModifier);
		}
		if(delay <=0){
			delay = particleDelay;
		}
		delay -= level.getDeltaTime();
	}
	
	private float getSwingSpeed(Player player){
		return swingSpeed * player.getArmStrength();
	}
	
	private void updateSwordParts(Vector3 swordPos, Level level, Controls controls){
		for(int i =0; i < parts.size(); i++){
			SwordPart part = parts.get(i);
			//get the direction of the sword steps
			Vector3 swordDir = new Vector3(direction);
			swordDir.scale(parts.get(i).getBoundingBox().getX());
			if(delay <=0){
				if(part.getPartType().equals(SwordPart.BLADE_PART)){
					level.getParticles().createParticle(false, 0.05f, part.getBoundingBox().getX(), swordPos.getX(),swordPos.getY(), swordPos.getZ(), 
						part.getColor().getR(), part.getColor().getG(), part.getColor().getB(), part.getColor().getA(), 0, 0, 0);
				}
			}
			part.getPosition().set(swordPos);
			part.update(level, controls);
			swordPos.add(swordDir);
		}
	}
	
	private boolean isSwordSheithed(){
		return (direction.getLength() < 0.01f);
	}
	
	private Vector3 getSwordSwingDirection(Controls controls){
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
		return new Vector3(x, y, 0);
	}
	
	private Vector3 getSwordPosition(Player player){
		Vector3 swordPos = new Vector3(direction.getNormalized());
		//rotate the sword to the players hand
		float handPos = 90.0f;
		swordPos.setX((float)((swordPos.getX() * Math.cos(handPos)) - (swordPos.getY() * Math.sin(handPos))));
		swordPos.setY((float)((swordPos.getY() * Math.cos(handPos)) - (swordPos.getX() * Math.sin(handPos))));
		//move the sword to align with the player bounding box
		swordPos.setX(swordPos.getX() * player.getBoundingBox().getX()/2.0f);
		swordPos.setY(swordPos.getY() * player.getBoundingBox().getY()/2.0f);
		//move the sword to world space
		swordPos.add(player.getPosition());
		//set the height of the sword
		swordPos.setZ(player.getPosition().getZ()+(player.getBoundingBox().getZ()/2.5f));
		return swordPos;
	}
	
	@Override
	public void render(Graphics graphics) {
		if(! isSwordSheithed()){
			for(int i =0; i < parts.size(); i++){
				parts.get(i).render(graphics);
			}
		}
	}

}
