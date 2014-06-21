package com.alycarter.dragonOfAging.game.object.state.level;

import java.awt.Point;
import java.util.ArrayList;

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
	
	public Map(Level level, int shadow, int width, int height) {
		mapTexture = level.getTiledTexture("map");
		genMap(width, height, 50);
		this.shadow = shadow;
	}
	
	private void genMap(int width, int height, int rooms){
		size = new Point(width, height);
		heightMap = new float[width*height];
		for(int i = 0; i < width*height; i++){
			heightMap[i] = DEFAULT_HEIGHT+Math.round(Math.random())*NOISE; 
		}
		ArrayList<Node> openNodes = new ArrayList<Node>();
		openNodes.add(new Node(new Point(width/2, height/2), UP));
		for(int i = 0; i<rooms && openNodes.size()>0; i++){
			genCircle(openNodes, (int)(Math.random()*openNodes.size()), (int)(Math.random()*5)+5);
		}
	}
	
	public void genCircle(ArrayList<Node> nodes, int nodeToUse, int diameter){
		Node node = nodes.remove(nodeToUse);
		int radius = diameter/2;
		Point center = new Point(node.location.x+(node.direction.x * radius), node.location.y+(node.direction.y * radius));
		int endX = center.x+radius;
		int endY = center.y+radius;
		for(int y = center.y-radius; y <= endY; y++){
			for(int x = center.x-radius; x <= endX; x++){
				if(center.distance(x, y) <= radius){
					setHeight(x, y, Math.round(Math.random())*NOISE);
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
		float tilesHigh = bottom - top;
		float tilesWide = right - left;
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
			GL11.glScalef(1.0f/tilesWide, 1.0f/tilesHigh, 1);
			for(float y = -0.5f; y < Math.ceil(tilesHigh); y++){	
				GL11.glPushMatrix();
					for(float x = 0; x < Math.ceil(tilesWide); x++){
						float height = getHeight((int)(x+left), (int)(y+top));
						float xPos = x + left + 0.5f;
						float yPos = y + top + 1.0f - height;
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						graphics.drawImage(shadow, xPos, yPos, y+0.01f, 1, 2, 0);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glTranslatef(1, 0, 0);
					}	
				GL11.glPopMatrix();
				GL11.glTranslatef(0, 2, 0);
			}
			GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPopMatrix();
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
