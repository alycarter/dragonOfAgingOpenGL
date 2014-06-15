package com.alycarter.dragonOfAging.game.graphics;

import java.awt.image.BufferedImage;

import com.alycarter.dragonOfAging.game.object.state.State;

public class TiledTexture {
	
	private int[] tiles;
	private int tileSize;
	private String name;
	
	public TiledTexture(Graphics graphics, State state, String name, BufferedImage image, int tileWidth, int tileHeight) {
		int tilesWide = image.getWidth() / tileWidth;
		int tilesHigh = image.getHeight() / tileHeight;
		this.name = name;
		tileSize = tilesWide*tilesHigh;
		tiles = new int[tileSize];
		for(int y = 0; y < tilesHigh; y++){
			for(int x = 0; x < tilesWide; x++){
				BufferedImage tile = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
				tile.getGraphics().drawImage(image, x*tileWidth*-1, y*tileHeight*-1, null);
				tiles[(y * tilesWide) + x] = graphics.addTexture(tile, state);
			}	
		}
	}
	
	public int getTileTextureID(int tileNumber){
		return tiles[tileNumber%tileSize];
	}
	
	public int getTilesSize(){
		return tileSize;
	}

	public String getName(){
		return name;
	}
}
