package com.alycarter.dragonOfAging.game.object.state.level;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Entity;
import com.alycarter.dragonOfAging.game.object.state.level.particle.Particle;

public class LevelRenderer {

	private Level level;
	
	private int shadowBuffer;
	private int shadow;
	
	private TiledTexture mapTexture;

	//pixel size of the in game units
	private float unitResolution;
	public static final float BASE_UNITRESOLUTION = 96;//96

	public LevelRenderer(Level level, Graphics graphics) {
		this.level = level;
		mapTexture = level.getTiledTexture("map");
		shadowBuffer = graphics.addTexture(graphics.getResolution().x, graphics.getResolution().y*2, level);
		shadow = level.getTiledTexture("shadow").getTileTextureID(0);
		unitResolution = BASE_UNITRESOLUTION; // set to 92
	}
	
	public void render(Graphics graphics){
		//corner locations in game
		Camera camera = level.getCamera();
		float left = camera.getPosition().getX() - (float)(graphics.getResolution().getX()/2/unitResolution); 
		float right = camera.getPosition().getX() + (float)(graphics.getResolution().getX()/2/unitResolution); 
		float top = camera.getPosition().getY() - (float)(graphics.getResolution().getY()/2/unitResolution); 
		float bottom = camera.getPosition().getY() + (float)(graphics.getResolution().getY()/2/unitResolution); 
		//start world drawing
		graphics.enableWorldCamera(camera.getPosition().getX(), camera.getPosition().getY(), unitResolution);
		//draw entities
		renderEntities(graphics, top, bottom, left, right);
		//draw particles
		level.getParticles().render(graphics, top, bottom, left, right);
		//draw shadows to texture
		drawShadows(graphics,top,bottom,left,right);
		//draw the map
		renderMap(graphics,top,bottom,left,right);
		//end world drawing
		graphics.disableWorldCamera();
		//render ui objects here
	}
	
	public void renderEntities(Graphics graphics, float top, float bottom, float left, float right){
		ArrayList<Entity> entities = level.getEntities();
		for(int i = 0;i < entities.size(); i++){
			if(entities.get(i).isOnScreen(top, bottom, left, right)){
				entities.get(i).render(graphics);				
			}
		}
	}
	
	private void drawShadows(Graphics graphics, float top, float bottom, float left, float right){
		graphics.bindToFrameBuffer(shadowBuffer);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		//draw shadows
		graphics.bindTexture(shadow);
		//scale the world to fit the shadow texture
		GL11.glViewport(0, 0, graphics.getResolution().x, graphics.getResolution().y*2);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, top/2, 0);
		GL11.glScalef(1.0f, 0.5f, 1.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		bottom += bottom-top;
		//draw all the entity shadows
		ArrayList<Entity> entities = level.getEntities();
		for(int i = 0;i < entities.size(); i++){
			Entity e = entities.get(i); 
			if(e.isOnScreen(top, bottom, left, right)){
				float y = e.getPosition().getY();
				graphics.drawRectangle(e.getPosition().getX(), y, 0,
						e.getBoundingBox().getX(), e.getBoundingBox().getY(), 0);
			}
		}
		//draw all the particle shadows
		ArrayList<Particle> particleList = level.getParticles().getActiveParticles();
		for(int i = 0;i < particleList.size(); i++){
			if(particleList.get(i).isOnScreen(top, bottom, left, right)){
				float y = particleList.get(i).getPosition().getY();
				graphics.drawRectangle(particleList.get(i).getPosition().getX(), y, 0,
					particleList.get(i).getSize(), particleList.get(i).getSize()/2.0f, 0);
			}
		}
		//move back to the normal view port
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glViewport(0, 0, graphics.getResolution().x, graphics.getResolution().y);
		//deselect the shadow texture
		graphics.unBindTexture();
		//switch back to window buffer
		graphics.unbindFromFrameBuffer();
	}
	
	private void renderMap(Graphics graphics, float top, float bottom, float left, float right){
		renderTexture(graphics, left, right, top, bottom);
		renderShadows(graphics, left, right, top, bottom);
	}
	
	private void renderTexture(Graphics graphics, float left, float right, float top, float bottom){
		for(int y =(int)Math.ceil(bottom); y > Math.floor(top)-1; y--){	
			for(int x = (int) Math.floor(left); x < Math.ceil(right); x++){
				float height = level.getMap().getHeight(x, y);
				float value = 1-(height/2);
				FloatColor color = new FloatColor(value, value, value, 1.0f);
				float xPos = x + 0.5f;
				float yPos = y + 0.5f - height;
				graphics.drawImage(mapTexture.getTileTextureID(level.getMap().getTileTexture(x, y)),color, xPos, yPos+0.5f, y, 1, 2, 0);
			}	
		}
	}
	
	private void renderShadows(Graphics graphics, float left, float right, float top, float bottom){
		float tilesHigh = bottom - top;
		float tilesWide = right - left;
		graphics.bindTexture(shadowBuffer);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
			GL11.glTranslatef(0, -1f, 0);
			GL11.glScalef(1.0f/(tilesWide), (1.0f/(tilesHigh))*-0.5f, 1);
			for(int y =(int)Math.ceil(bottom); y > Math.floor(top)-1; y--){	
				for(int x = (int) Math.floor(left); x < Math.ceil(right); x++){
					float height = level.getMap().getHeight(x, y);
					float xPos = x + 0.5f;
					float yPos = y + 0.5f - height;
					GL11.glPushMatrix();
						GL11.glTranslatef(x-left, y-top, 0);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						graphics.drawRectangle(xPos, yPos, y+0.01f, 1, 1, 0);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glTranslatef(0, 1.0f, 0);
						GL11.glScalef(1, 1.0f/(tilesHigh*192), 1);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						graphics.drawRectangle(xPos, yPos+1, y+0.01f, 1, 1, 0);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glPopMatrix();
				}	
			}
		GL11.glPopMatrix();	
		graphics.unBindTexture();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public float getUnitResolution() {
		return unitResolution;
	}

	public void setUnitResolution(float unitResolution) {
		this.unitResolution = unitResolution;
	}

}
