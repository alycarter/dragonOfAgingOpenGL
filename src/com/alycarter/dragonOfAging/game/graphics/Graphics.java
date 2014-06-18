package com.alycarter.dragonOfAging.game.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.object.state.State;

public class Graphics {

	private Point resolution;
	
	private Mesh quad;
	
	private ArrayList<Texture> textures;
	
	private int invalidTextureID;
	
	public Graphics(int width, int height) {
		resolution = new Point(width, height);
		textures = new ArrayList<Texture>();
	}
	
	public void initialize(){
		setUpMatrices();
		setUpQuad();
		setUpTextures();
	}
	
	private void setUpMatrices(){
		//set the background colour to pink
		GL11.glClearColor(1.0f, 0.0f, 1.0f, 0.0f);
		//allow transparency
		GL11.glEnable(GL11.GL_BLEND); 
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//set up the projection matrix to work in screen coordinates and put it on the stack
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glOrtho(0, resolution.getX(), resolution.getY(), 0, 1, -1);
		//swap back to model view ready to draw to the screen
		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
	}
	
	private void setUpQuad(){
		//vertex data
		float vertexArray[] = 
			{-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f, 
			0.5f, 0.5f, 0.0f,
			-0.5f, 0.5f, 0.0f};
		float textureArray[] = 
			{0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f};
		int indexArray[] = {0, 1 ,2, 0, 2, 3};
		//but the vertex data into buffers
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(12); 
		vertexBuffer.put(vertexArray);
		vertexBuffer.flip();
		
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(8);
		textureBuffer.put(textureArray);
		textureBuffer.flip();
		
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(6);
		indexBuffer.put(indexArray);
		indexBuffer.flip();
		//load the vertex buffers into the mesh
		quad = new Mesh(indexBuffer, vertexBuffer, textureBuffer);
	}
	
	private void setUpTextures(){
		BufferedImage invalid = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		invalid.setRGB(0, 0, Color.pink.getRGB());
		invalid.setRGB(1, 0, Color.black.getRGB());
		invalid.setRGB(0, 1, Color.black.getRGB());
		invalid.setRGB(1, 1, Color.pink.getRGB());
		invalidTextureID = addTexture(invalid, null);
	}
	//returns the index that the texture is in the texture list
	public int addTexture(BufferedImage texture, State assignedState){
		//add a new texture
		textures.add(new Texture(texture, assignedState));
		//return the texture index
		return textures.size()-1;
	}

	public void resourcesUpdate(){
		//while the texture at the end of the list has a state assigned and needs removing
		while(!textures.isEmpty() && textures.get(textures.size()-1).getState() != null && textures.get(textures.size()-1).getState().needsRemoving()){
			//remove it from the end of the list and destroy it
			textures.remove(textures.size()-1).destroy();
		}
	}
	
	public Point getResolution(){
		//return the render resolution
		return resolution;
	}
	
	public void clearScreen(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void enableWorldCamera(float cameraX, float cameraY, float unitResolution){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
			GL11.glTranslatef((float)resolution.getX()/2.0f, (float)resolution.getY()/2.0f, 0);
			GL11.glScalef(unitResolution, unitResolution, unitResolution);
			GL11.glTranslatef(-cameraX, -cameraY, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void disableWorldCamera(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void drawRectangle(FloatColor color, float x, float y, float width, float height, float rotation){
		//set the colour of the rectangle
		GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
		//add a matrix to the stack
		GL11.glPushMatrix();
			//load the identity matrix
			GL11.glLoadIdentity();
			//move to the rectangles position
			GL11.glTranslatef(x, y, 0.0f);
			//rotate the rectangle
			GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
			//scale the rectangle to the right size
			GL11.glScalef(width, height, 0);
			//draw our rectangle
			quad.drawMesh();
		//pop the matrix off the stack
		GL11.glPopMatrix();
		//go back to white colours
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public void drawImage(int textureID, FloatColor color, float x, float y, float width, float height, float rotation){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(textureID >= 0 && textureID < textures.size()){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(textureID).getTextureID());
		}else{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(invalidTextureID).getTextureID());
		}
		drawRectangle(color, x, y, width, height, rotation);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public void drawImage(int textureID, float x, float y, float width, float height, float rotation){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(textureID >= 0 && textureID < textures.size()){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(textureID).getTextureID());
		}else{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(invalidTextureID).getTextureID());
		}
		drawRectangle(FloatColor.WHITE, x, y, width, height, rotation);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void destroy(){
		//pop the orthographic projection back off
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		//destroy the quad
		quad.destroy();
		//remove all the textures
		for(int i = 0; i < textures.size(); i++){
			textures.get(i).destroy();
		}
		textures.clear();
	}
}
