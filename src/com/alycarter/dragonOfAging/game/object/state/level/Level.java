package com.alycarter.dragonOfAging.game.object.state.level;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.alycarter.dragonOfAging.game.Game;
import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.object.state.State;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Entity;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Player;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Slime;
import com.alycarter.dragonOfAging.game.object.state.level.uiObjects.LevelUIObject;

public class Level extends State {

	//lists of entities and uiObjects to be rendered and updated each frame
	private ArrayList<Entity> entities;
	private ArrayList<LevelUIObject> uiObjects;
	private Player player;
	
	//camera
	private Camera camera;
	
	//textures
	private ArrayList<TiledTexture> tilesTextures;
	
	//level tile map
	private Map map;

	//amount of time the last frame took
	private float deltaTime;
	//time multiplier
	private float timeSpeed;
	
	//pixel size of the in game units
	private float unitResolution;
	
	private int shadowBuffer;
	private int shadow;
	
	public Level(String name, Graphics graphics) {
		super(name);
		entities = new ArrayList<Entity>();
		uiObjects = new ArrayList<LevelUIObject>();
		deltaTime = 0.0f;
		timeSpeed =1.0f;
		unitResolution = 92.0f; // set to 92
		tilesTextures = new ArrayList<TiledTexture>();
		try {
			tilesTextures.add(new TiledTexture(graphics, this, "playerBase", ImageIO.read(Level.class.getResource("/playerBase.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "ironArms", ImageIO.read(Level.class.getResource("/ironArms.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "ironChestPlate", ImageIO.read(Level.class.getResource("/ironChestPlate.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "ironLegs", ImageIO.read(Level.class.getResource("/ironLegs.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "map", ImageIO.read(Level.class.getResource("/grass.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "slime", ImageIO.read(Level.class.getResource("/slime.png")), 12, 12));
			tilesTextures.add(new TiledTexture(graphics, this, "shadow", ImageIO.read(Level.class.getResource("/shadow.png")), 12, 12));
		} catch (IOException e) {e.printStackTrace();}
		shadowBuffer = graphics.addTexture(graphics.getResolution().x, graphics.getResolution().y, this);
		shadow = getTiledTexture("shadow").getTileTextureID(0);
		loadLevel();
	}
	
	private void loadLevel(){
		map = new Map(this, shadowBuffer, 100, 100);
		entities.clear();
		player = new Player(this,(float)map.getSize().getX()/2.0f, (float)map.getSize().getY()/2.0f);
		entities.add(player);
		camera = new Camera(player.getPosition());
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				entities.add(new Slime(this, player.getPosition().getX(), player.getPosition().getY(), 1));
			}	
		}
	}

	@Override
	public void update(Game game) {
		if(game.getControls().isKeyTyped(Keyboard.KEY_R)){
			loadLevel();
		}
		deltaTime = game.getDeltaTime() * timeSpeed;
		//update all uiObjects and entities
		updateObjects(game.getControls());
		//update camera
		camera.update(this);
		//remove any objects that are marked for removal during this frame
		removeMarkedObjects();
	}
	
	private void updateObjects(Controls controls){
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).update(this, controls);
		}
		for(int i = 0; i < uiObjects.size(); i++){
			uiObjects.get(i).update(this, controls);
		}
	}
	
	private void removeMarkedObjects(){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).needsRemoving()){
				entities.remove(i);
				i--;
			}
		}
		for(int i = 0; i < uiObjects.size(); i++){
			if(uiObjects.get(i).needsRemoving()){
				uiObjects.remove(i);
				i--;
			}
		}
	}

	@Override
	public void render(Graphics graphics) {
		//corner locations in game
		float left = camera.getPosition().getX() - (float)(graphics.getResolution().getX()/2/unitResolution); 
		float right = camera.getPosition().getX() + (float)(graphics.getResolution().getX()/2/unitResolution); 
		float top = camera.getPosition().getY() - (float)(graphics.getResolution().getY()/2/unitResolution); 
		float down = camera.getPosition().getY() + (float)(graphics.getResolution().getY()/2/unitResolution); 
		
		//start world drawing
		graphics.enableWorldCamera(camera.getPosition().getX(), camera.getPosition().getY(), unitResolution);
		
		//need to sort the entities by depth//removed for depth testing purposes
		//sortEntities();
		for(int i = 0;i < entities.size(); i++){
			entities.get(i).render(graphics);
		}
		
		//draw shadows to texture
		drawShadows(graphics);
		//graphics.drawImage(shadowBuffer, left+((right - left)/2.0f), top+((down - top)/2.0f), 900, right-left, down-top, 0);
		//draw the map
		int mapLayer = 0;
		for(mapLayer =(int)Math.ceil(down); mapLayer > Math.floor(top)-1; mapLayer--){
			map.renderLayer(graphics, mapLayer, left, right, top, down);
		}
		//end world drawing
		graphics.disableWorldCamera();
		//render ui objects here
	}
	
	private void drawShadows(Graphics graphics){
		graphics.bindToFrameBuffer(shadowBuffer);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		//draw shadows
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(int i = 0;i < entities.size(); i++){
			float y = entities.get(i).getPosition().getY();
			graphics.drawImage(shadow, entities.get(i).getPosition().getX(), y, 0,
					entities.get(i).getBoundingBox().getX(), entities.get(i).getBoundingBox().getY(), 0);
		}
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		//switch back to window buffer
		graphics.unbindFromFrameBuffer();
	}
	
	/*private void sortEntities(){
		for(int i =1; i < entities.size(); i++){
			int j = i;
			while( j > 0 && entities.get(j).getPosition().getY() < entities.get(j-1).getPosition().getY()){
				Entity temp = entities.get(j);
				entities.set(j, entities.get(j-1));
				entities.set(j-1, temp);
				j--;
			}
		}
	}*/
	
	public ArrayList<Entity> getEntities(){
		return entities;
	}

	public ArrayList<LevelUIObject> getUIObjects(){
		return uiObjects;
	}

	public TiledTexture getTiledTexture(String textureName){
		TiledTexture texture = null;
		for(int i = 0; i < tilesTextures.size() && texture == null; i++){
			if(tilesTextures.get(i).getName().equals(textureName)){
				texture = tilesTextures.get(i);
			}
		}
		return texture;
	}
	public float getDeltaTime(){
		return deltaTime;
	}
	
	public void setTimeSpeed(float speed){
		timeSpeed = speed;
	}
	
	public Map getMap(){
		return map;
	}
	
	public Player getPlayer(){
		return player;
	}
}
