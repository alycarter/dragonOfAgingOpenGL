package com.alycarter.dragonOfAging.game.graphics;

import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

public class Sprite {

	private ArrayList<TiledTexture> frameLayers;
	
	private ArrayList<AnimationTimer> timers;
	
	private AnimationTimer currentTimer;
	
	private Float imageSize;
	
	private Float imageOffSet;
	
	public Sprite(float imageSizeX, float imageSizeY, float imageOffSetX, float imageOffSetY) {
		imageSize = new Float(imageSizeX, imageSizeY); 
		imageOffSet = new Float(imageOffSetX, imageOffSetY);
		timers = new ArrayList<AnimationTimer>();
		currentTimer = null;
		frameLayers = new ArrayList<TiledTexture>();
	}
	
	public void update(float deltaTime){
		if(currentTimer != null){
			currentTimer.update(deltaTime);
		}
	}
	
	public ArrayList<Integer> getFrameTextureIDs(){
		ArrayList<Integer> frameIDs = new ArrayList<Integer>();
		if(currentTimer != null){
			for(int i = 0; i < frameLayers.size(); i++){
				frameIDs.add(frameLayers.get(i).getTileTextureID(currentTimer.getCurrentFrame()));
			}
		}
		return frameIDs;
	}
	
	public int addAnimationTimer(AnimationTimer timer, boolean setAsCurrentTimer){
		timers.add(timer);
		if(setAsCurrentTimer){
			currentTimer = timer;
		}
		return timers.size()-1;
	}

	public void addFrameLayer(TiledTexture frames, int position){
		if(position < 0){
			position = 0;
		}
		if(position > frameLayers.size()){
			position = frameLayers.size();
		}
		frameLayers.add(position, frames);
	}
	
	public void appendFrameLayer(TiledTexture frames){
		frameLayers.add(frames);
	}
	
	public TiledTexture removeFrameLayer(String name){
		boolean found = false;
		TiledTexture texture = null;
		for(int i = 0; i < frameLayers.size() && !found; i++){
			if(frameLayers.get(i).getName().equals(name)){
				texture = frameLayers.remove(i);
				found = true;
			}
		}
		return texture;
	}
	
	public TiledTexture removeFrameLayer(int position){
		if(position >=0 && position < frameLayers.size()){
			return frameLayers.remove(position);
		}else{
			return null;
		}
	}
	
	public TiledTexture replaceFrameLayer(TiledTexture frames, String name){
		boolean found = false;
		TiledTexture texture = null;
		for(int i = 0; i < frameLayers.size() && !found; i++){
			if(frameLayers.get(i).getName().equals(name)){
				texture = frameLayers.remove(i);
				frameLayers.add(i, frames);
				found = true;
			}
		}
		return texture;
	}
	
	public TiledTexture replaceFrameLayer(TiledTexture frames, int position){
		TiledTexture texture = removeFrameLayer(position);
		if(texture != null){
			addFrameLayer(frames, position);
		}
		return texture;
	}
	
	public void setCurrentAnimationTimer(int timer){
		if(timer >=0 && timer < timers.size()){			
			currentTimer = timers.get(timer);
		}
	}
	
	public void setCurrentAnimationTimer(int timer, boolean reset){
		if(timer >=0 && timer < timers.size()){			
			currentTimer = timers.get(timer);
		}
		if(reset){
			currentTimer.reset();
		}
	}
	
	public int getCurrentAnimationTimerID(){
		for(int i = 0; i < timers.size(); i++){
			if(timers.get(i).equals(currentTimer)){
				return i;
			}
		}
		return 0;
	}
	
	public AnimationTimer getCurrentAnimationTimer(){
		return currentTimer;
	}
	public Float getImageSize(){
		return imageSize;
	}
	
	public Float getOffSet(){
		return imageOffSet;
	}
	
	public void render(Graphics graphics, float worldPositionX, float worldPositionY, float depth){
		float x = worldPositionX + imageOffSet.x;
		float y = worldPositionY + imageOffSet.y;
		ArrayList<Integer> frameIDs = getFrameTextureIDs();
		for(int i = 0; i < frameIDs.size(); i++){
			graphics.drawImage(frameIDs.get(i), x, y, depth+(i*0.001f), imageSize.x, imageSize.y, 0.0f);
		}
	}
	
	public int getFrameLayerCount(){
		return frameLayers.size();
	}
}
