package com.alycarter.dragonOfAging.game.graphics;

public class AnimationTimer {
	
	private float currentFrame;
	
	private float fps;
	
	private int startFrame;
	
	private int endFrame;
	
	private boolean looping;
	
	private boolean hasEnded;
	
	public AnimationTimer(int startFrame, int endFrame, float fps, boolean looping) {
		this.fps = fps;
		this.startFrame = startFrame;
		this.endFrame = endFrame;
		currentFrame = startFrame;
		this.looping = looping;
		hasEnded = false;
	}
	
	public void update(float deltaTime){
		currentFrame += fps*deltaTime;
		if(currentFrame >= endFrame){
			if(looping){
				currentFrame -= endFrame-startFrame;
			}else{
				currentFrame-=1;
				hasEnded = true;
			}
		}
	}
	
	public int getCurrentFrame(){
		return (int)currentFrame;
	}
	
	public void reset(){
		currentFrame = startFrame;
		hasEnded = false;
	}
	
	public boolean hasEnded(){
		return hasEnded;
	}

}
