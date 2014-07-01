package com.alycarter.dragonOfAging.game.object.state.level.particle;

import java.util.ArrayList;

import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.object.state.level.Level;

public class ParticleSystem {

	private ArrayList<Particle> activeParticles;
	private ArrayList<Particle> deadParticles;
	
	public ParticleSystem(int numberOfParticles) {
		activeParticles = new ArrayList<Particle>(numberOfParticles);
		deadParticles = new ArrayList<Particle>(numberOfParticles);
		for(int i = 0; i < numberOfParticles; i++){
			deadParticles.add(new Particle());
		}
	}
	
	public void update(Level level){
		for(int i = 0 ; i < activeParticles.size();i++){
			if(activeParticles.get(i).isAlive()){
				activeParticles.get(i).update(level);
			}else{
				deadParticles.add(activeParticles.get(i));
				activeParticles.remove(i);
				i--;
			}
		}
	}
	
	public void render(Graphics graphics, float top, float bottom, float left, float right){
		for(int i = 0 ; i < activeParticles.size();i++){
			if(activeParticles.get(i).isOnScreen(top, bottom, left, right)){
			activeParticles.get(i).render(graphics);
			}
		}
	}
	
	public ArrayList<Particle> getActiveParticles(){
		return activeParticles;
	}
	
	public boolean hasFreeParticles(){
		return !deadParticles.isEmpty();
	}
	
	public void createParticle(boolean forceCreate, float timeToLive, float size, float positionX, float positionY, float positionZ,
			float r, float g, float b, float a, float velocityX, float velocityY, float velocityZ, float weight){
		if(!deadParticles.isEmpty()){
			deadParticles.get(deadParticles.size()-1).create(timeToLive, size, positionX, positionY, positionZ, r, g, b, a, velocityX, velocityY, velocityZ, weight);
			activeParticles.add(deadParticles.remove(deadParticles.size()-1));
		}else{
			System.out.println("out of particles");
			if(forceCreate){
				activeParticles.add(activeParticles.remove(0));
				activeParticles.get(activeParticles.size()-1).create(timeToLive, size, positionX, positionY, positionZ, r, g, b, a, velocityX, velocityY, velocityZ, weight);
			}
		}
		
	}
	
	public void killAllParticles(){
		deadParticles.addAll(activeParticles);
		activeParticles.clear();
	}

}
