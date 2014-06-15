package com.alycarter.dragonOfAging.game.graphics;

import java.util.ArrayList;

public class Animation {

	private TiledTexture frames;
	
	private ArrayList<AnimationTimer> timers;
	
	private AnimationTimer currentTimer;
	
	public Animation(TiledTexture frames) {
		timers = new ArrayList<AnimationTimer>();
		currentTimer = null;
		this.frames = frames;
	}
	
	public void update(float deltaTime){
		if(currentTimer != null){
			currentTimer.update(deltaTime);
		}
	}
	
	public int getFrameTextureID(){
		if(currentTimer != null){
			return frames.getTileTextureID(currentTimer.getCurrentFrame());
		}else{
			return 0;
		}
	}
	
	public int addAnimationTimer(AnimationTimer timer, boolean setAsCurrentTimer){
		timers.add(timer);
		if(setAsCurrentTimer){
			currentTimer = timer;
		}
		return timers.size()-1;
	}

	public void setCurrentAnimationTimer(int timer){
		if(timer >=0 && timer < timers.size()){			
			currentTimer = timers.get(timer);
		}
	}
	
}
