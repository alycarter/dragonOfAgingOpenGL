package com.alycarter.dragonOfAging.game.object.state.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

import com.alycarter.dragonOfAging.game.Game;
import com.alycarter.dragonOfAging.game.controls.Controls;
import com.alycarter.dragonOfAging.game.graphics.Graphics;
import com.alycarter.dragonOfAging.game.graphics.TiledTexture;
import com.alycarter.dragonOfAging.game.object.state.State;
import com.alycarter.dragonOfAging.game.object.state.level.entity.AirSpawner;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Entity;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Fire;
import com.alycarter.dragonOfAging.game.object.state.level.entity.ItemPickUp;
import com.alycarter.dragonOfAging.game.object.state.level.entity.Slime;
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;
import com.alycarter.dragonOfAging.game.object.state.level.map.Map;
import com.alycarter.dragonOfAging.game.object.state.level.particle.ParticleSystem;
import com.alycarter.dragonOfAging.game.object.state.level.uiObjects.LevelUIObject;

public class Level extends State {

	//lists of entities and uiObjects to be rendered and updated each frame
	private ArrayList<Entity> entities;
	private ArrayList<LevelUIObject> uiObjects;
	private Player player;
	
	private ItemPool itemPool;
	
	//particles
	private ParticleSystem particles;
	
	//camera
	private Camera camera;
	
	//textures
	private ArrayList<TiledTexture> tilesTextures;
	
	//level tile map
	private Map map;
	
	private static final int MAP_DEFAULT_WIDTH = 400;
	private static final int MAP_DEFAULT_HEIGHT = 400;
	private static final int MAP_DEFAULT_ROOMS = 15;	
	private static final int MAP_DEFAULT_ITEMS = 5;

	//amount of time the last frame took
	private float deltaTime;
	//time multiplier
	private float timeSpeed;
	
	private LevelRenderer levelRenderer;
	
	public Level(String name, Graphics graphics) {
		super(name);
		entities = new ArrayList<Entity>();
		uiObjects = new ArrayList<LevelUIObject>();
		deltaTime = 0.0f;
		timeSpeed =1.0f;
		tilesTextures = new ArrayList<TiledTexture>();
		loadTextures(graphics);
		levelRenderer = new LevelRenderer(this, graphics);
		map =new Map(this);
		player = new Player(this,0, 0);
		particles = new ParticleSystem(2000);
		itemPool = new ItemPool(this);
		loadLevel((long)(Math.random()*(Long.MAX_VALUE-1)));
	}
	
	private void loadLevel(long seed){
		Random random = new Random(seed);
		if(!map.isInisialised()){
			map.genMap(LevelType.FOREST_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
		}else{
			switch (map.getLevelType().getLevelName()){
			case LevelType.FOREST_NAME:
				map.genMap(LevelType.LAVA_LEVEL,MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			case LevelType.LAVA_NAME:
				map.genMap(LevelType.CAVE_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			case LevelType.CAVE_NAME:
				map.genMap(LevelType.DESERT_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			case LevelType.DESERT_NAME:
				map.genMap(LevelType.SNOW_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			case LevelType.SNOW_NAME:
				map.genMap(LevelType.DUNGEON_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			case LevelType.DUNGEON_NAME:
				map.genMap(LevelType.FOREST_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			default:
				map.genMap(LevelType.FOREST_LEVEL, MAP_DEFAULT_WIDTH, MAP_DEFAULT_HEIGHT, MAP_DEFAULT_ROOMS, MAP_DEFAULT_ITEMS, random);
				break;
			}
		}
		entities.clear();
		particles.killAllParticles();
		entities.add(player);
		player.updateAge();
		player.getPosition().set(map.getPlayerSpawnLocation());
		entities.add(new AirSpawner(this, player.getPosition().getX(), player.getPosition().getY()-1, map.getHeight((int)player.getPosition().getX(), (int)player.getPosition().getY())));
		camera = new Camera(player.getPosition());
		while(map.getEnemySpawnLocations().size()>0){
			entities.add(new Slime(this, map.getNextEnemySpawnPosition(random)));				
		}
		while(map.getPickupSpawnLocations().size()>0){
			entities.add(new ItemPickUp(this, itemPool.getRandomItem(this,random), map.getNextPickupSpawnPosition(random)));
		}
		entities.add(new Fire(this, map.getLevelExitLocation()));	
	}
	
	private void loadTextures(Graphics graphics){
		try {
			tilesTextures.add(new TiledTexture(graphics, this, "playerBase", ImageIO.read(Level.class.getResource("/playerBase.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "ironArms", ImageIO.read(Level.class.getResource("/ironArms.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "ironChestPlate", ImageIO.read(Level.class.getResource("/ironChestPlate.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "ironLegs", ImageIO.read(Level.class.getResource("/ironLegs.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "leatherArms", ImageIO.read(Level.class.getResource("/leatherArms.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "leatherChestPlate", ImageIO.read(Level.class.getResource("/leatherChestPlate.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "leatherLegs", ImageIO.read(Level.class.getResource("/leatherLegs.png")), 12, 24));
			tilesTextures.add(new TiledTexture(graphics, this, "map", ImageIO.read(Level.class.getResource("/map.png")), 16, 32));
			tilesTextures.add(new TiledTexture(graphics, this, "slime", ImageIO.read(Level.class.getResource("/slime.png")), 12, 12));
			tilesTextures.add(new TiledTexture(graphics, this, "shadow", ImageIO.read(Level.class.getResource("/shadow.png")), 16, 16));
			tilesTextures.add(new TiledTexture(graphics, this, "sword", ImageIO.read(Level.class.getResource("/sword.png")), 12, 12));
			tilesTextures.add(new TiledTexture(graphics, this, "fire", ImageIO.read(Level.class.getResource("/fire.png")), 16, 16));
			tilesTextures.add(new TiledTexture(graphics, this, "bird", ImageIO.read(Level.class.getResource("/bird.png")), 16, 16));
			tilesTextures.add(new TiledTexture(graphics, this, "airSpawner", ImageIO.read(Level.class.getResource("/airSpawner.png")), 16, 16));
		} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void update(Game game) {
		if(game.getControls().isKeyHeld(Keyboard.KEY_EQUALS) || game.getControls().getController().isButtonPressed(4)){
			levelRenderer.setUnitResolution(levelRenderer.getUnitResolution()+(deltaTime * 20));
		}
		if(game.getControls().isKeyHeld(Keyboard.KEY_MINUS) || game.getControls().getController().isButtonPressed(5)){
			levelRenderer.setUnitResolution(levelRenderer.getUnitResolution()-(deltaTime * 20));
		}
		if(game.getControls().isKeyTyped(Keyboard.KEY_BACK) || game.getControls().getController().isButtonPressed(6)){
			levelRenderer.setUnitResolution(LevelRenderer.BASE_UNITRESOLUTION);
		}
		if(game.getControls().isKeyTyped(Keyboard.KEY_ESCAPE) || game.getControls().getController().isButtonPressed(3)){
			markForRemoval();
		}
		if(game.getControls().isKeyTyped(Keyboard.KEY_P)){
			player.updateAge();
		}
		
		deltaTime = game.getDeltaTime() * timeSpeed;
		//update all uiObjects and entities
		updateObjects(game.getControls());
		//update particles
		particles.update(this);
		//update camera
		camera.update(this);
		//remove any objects that are marked for removal during this frame
		removeMarkedObjects();
		if(player.getPosition().distanceTo(map.getLevelExitLocation()) < 1){
			loadLevel((long)(Math.random()*(Long.MAX_VALUE-1)));
		}
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
		levelRenderer.render(graphics);
	}
	
	public ArrayList<Entity> getEntities(){
		return entities;
	}

	public ArrayList<LevelUIObject> getUIObjects(){
		return uiObjects;
	}

	public ParticleSystem getParticles(){
		return particles;
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
	
	public Camera getCamera(){
		return camera;
	}
}
