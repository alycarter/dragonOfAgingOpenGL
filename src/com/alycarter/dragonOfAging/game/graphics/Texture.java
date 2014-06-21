package com.alycarter.dragonOfAging.game.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.object.state.State;

public class Texture {

	private State assignedState;
	private int textureObjectID;
	public Texture(BufferedImage image, State assignedState) {
		this.assignedState = assignedState;
		createTextureObject(image);
	}
	
	private void createTextureObject(BufferedImage image){
		//create the colour buffer
		ByteBuffer colorBuffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		//place each pixel into the buffer
		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x< image.getWidth(); x++){
				Color color = new Color(image.getRGB(x, y), true);
				colorBuffer.put((byte) color.getRed());
				colorBuffer.put((byte) color.getGreen()); 
				colorBuffer.put((byte) color.getBlue());
				colorBuffer.put((byte) color.getAlpha());
			}
		}
		//flip the buffer
		colorBuffer.flip();
		//create a buffer id
		textureObjectID = GL11.glGenTextures();
		//select our texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureObjectID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		//put our pixel info into the buffer
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, colorBuffer);
		//set it to wrap
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		//use nearest neighbour scaling
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		//deselect our texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public Texture(int width, int height, State state){
		assignedState = state;
		createTextureObject(width, height);
	}
	
	public void createTextureObject(int width, int height){
		textureObjectID= GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureObjectID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_INT , (java.nio.ByteBuffer)null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public State getState(){
		return assignedState;
	}
	
	public int getTextureID(){
		return textureObjectID;
	}
	
	public void destroy(){
		//destroy the texture buffer
		GL11.glDeleteTextures(textureObjectID);
	}

}
