package com.alycarter.dragonOfAging.game.object.state.level;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.graphics.FloatColor;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;

public class Map {

	private final float DEFAULT_HEIGHT = 0.5f;
	private final float NOISE = (1.0f/12.0f);
	
	private final static Point UP = new Point(0, -1);
	private final static Point DOWN = new Point(0, 1);
	private final static Point LEFT = new Point(-1, 0);
	private final static Point RIGHT = new Point(1, 0);
	
	
	private Point size;
	private float heightMap[];
	private TiledTexture mapTexture;
	private int shadow;
	private long seed = 0;
	
	public Map(Level level, int shadow) {
		mapTexture = level.getTiledTexture("map");
		this.shadow = shadow;
	}
	
	public void genMap(int width, int height, int rooms, long seed){
		this.seed = seed;
		Random random = new Random(seed);
		size = new Point(width, height);
		heightMap = new float[width*height];
		for(int i = 0; i < width*height; i++){
			heightMap[i] = DEFAULT_HEIGHT+Math.round(random.nextFloat())*NOISE; 
		}
		ArrayList<Node> openNodes = new ArrayList<Node>();
		openNodes.add(new Node(new Point(width/2, height/2), UP));
		for(int i = 0; i<rooms && openNodes.size()>0; i++){
			genCircle(openNodes, (int)(random.nextFloat()*openNodes.size()), (int)(random.nextFloat()*7)+5, random);
		}
	}
	
	public long getCurrentSeed(){
		return seed;
	}
	
	private void genCircle(ArrayList<Node> nodes, int nodeToUse, int diameter, Random random){
		Node node = nodes.remove(nodeToUse);
		int radius = diameter/2;
		Point center = new Point(node.location.x+(node.direction.x * radius), node.location.y+(node.direction.y * radius));
		int endX = center.x+radius;
		int endY = center.y+radius;
		for(int y = center.y-radius; y <= endY; y++){
			for(int x = center.x-radius; x <= endX; x++){
				if(center.distance(x, y) <= radius){
					setHeight(x, y, Math.round(random.nextFloat())*NOISE);
				}
			}
		}
		if(node.direction.x == 0){
			nodes.add(new Node(new Point(center.x-radius, center.y), LEFT));
			nodes.add(new Node(new Point(center.x+radius, center.y), RIGHT));
		}
		if(node.direction.y == 0){
			nodes.add(new Node(new Point(center.x, center.y-radius), UP));
			nodes.add(new Node(new Point(center.x, center.y+radius), DOWN));
		}
		node.location.x+=diameter*node.direction.x;
		node.location.y+=diameter*node.direction.y;
		nodes.add(node);
	}
	
	private void setHeight(int x, int y, float z){
		if(x >= 0 && x < size.getX() && y >= 0 && y < size.getY()){
			heightMap[(y * size.y) + x] = z ;
		}
	}
	
	public float getHeight(int x, int y){
		if(x >= 0 && x < size.getX() && y >= 0 && y < size.getY()){
			return heightMap[(y*size.y) + x];
		}else{
			return 1.0f;
		}
	}
	
	public void render(Graphics graphics, float left, float right, float top, float bottom){
		renderTexture(graphics, left, right, top, bottom);
		renderShadows(graphics, left, right, top, bottom);
	}
	
	private void renderTexture(Graphics graphics, float left, float right, float top, float bottom){
		graphics.bindTexture(mapTexture.getTileTextureID(0));
		for(int y =(int)Math.ceil(bottom); y > Math.floor(top)-1; y--){	
			for(int x = (int) Math.floor(left); x < Math.ceil(right); x++){
				float height = getHeight(x, y);
				float value = 1-(height/1.5f);
				FloatColor color = new FloatColor(value, value, value, 1.0f);
				float xPos = x + 0.5f;
				float yPos = y + 0.5f - height;
				graphics.drawRectangle(color, xPos, yPos+0.5f, y, 1, 2, 0);
			}	
		}
		graphics.unBindTexture();
	}
	
	private void renderShadows(Graphics graphics, float left, float right, float top, float bottom){
		float tilesHigh = bottom - top;
		float tilesWide = right - left;
		graphics.bindTexture(shadow);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
			GL11.glTranslatef(0, -1f, 0);
			GL11.glScalef(1.0f/(tilesWide), (1.0f/(tilesHigh))*-0.5f, 1);
			for(int y =(int)Math.ceil(bottom); y > Math.floor(top)-1; y--){	
				for(int x = (int) Math.floor(left); x < Math.ceil(right); x++){
					float height = getHeight(x, y);
					float xPos = x + 0.5f;
					float yPos = y + 0.5f - height;
					GL11.glPushMatrix();
						GL11.glTranslatef(x-left, y-top, 0);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						graphics.drawRectangle(xPos, yPos, y+0.01f, 1, 1, 0);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glTranslatef(0, 1.0f, 0);
						GL11.glScalef(1, 1.0f/(tilesHigh*16), 1);
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
	
	public Point getSize(){
		return size;
	}
	
	private class Node{
		
		
		public Point location;
		public Point direction;
		
		public Node(Point location, Point direction){
			this.location = location;
			this.direction = direction;
		}
	}
}
