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
import com.alycarter.dragonOfAging.game.object.state.level.entity.player.Player;
import com.alycarter.dragonOfAging.game.object.state.level.particle.Particle;
import com.alycarter.dragonOfAging.game.object.state.level.particle.ParticleSystem;
import com.alycarter.dragonOfAging.game.object.state.level.uiObjects.LevelUIObject;

public class Level extends State {

	//lists of entities and uiObjects to be rendered and updated each frame
	private ArrayList<Entity> entities;
	private ArrayList<LevelUIObject> uiObjects;
	private Player player;
	
	//particles
	private ParticleSystem particles;
	
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
	private static final float BASE_UNITRESOLUTION = 96;//96
	
	private int shadowBuffer;
	private int shadow;
	
	public Level(String name, Graphics graphics) {
		super(name);
		entities = new ArrayList<Entity>();
		uiObjects = new ArrayList<LevelUIObject>();
		deltaTime = 0.0f;
		timeSpeed =1.0f;
		unitResolution = BASE_UNITRESOLUTION; // set to 92
		tilesTextures = new ArrayList<TiledTexture>();
		loadTextures(graphics);
		shadowBuffer = graphics.addTexture(graphics.getResolution().x, graphics.getResolution().y*2, this);
		shadow = getTiledTexture("shadow").getTileTextureID(0);
		map =new Map(this, shadowBuffer);
		player = new Player(this,0, 0);
		particles = new ParticleSystem(2000);
		loadLevel((long)(Math.random()*(Long.MAX_VALUE-1)));
	}
	
	private void loadLevel(long seed){
		if(!map.isInisialised()){
			map.genMap(LevelType.FOREST_LEVEL,100, 100, 15, seed);
		}else{
			switch (map.getLevelType().getLevelName()){
			case LevelType.FOREST_NAME:
				map.genMap(LevelType.LAVA_LEVEL, 100, 100, 15, seed);
				break;
			case LevelType.LAVA_NAME:
				map.genMap(LevelType.CAVE_LEVEL, 100, 100, 15, seed);
				break;
			case LevelType.CAVE_NAME:
				map.genMap(LevelType.DESERT_LEVEL, 100, 100, 15, seed);
				break;
			case LevelType.DESERT_NAME:
				map.genMap(LevelType.SNOW_LEVEL, 100, 100, 15, seed);
				break;
			case LevelType.SNOW_NAME:
				map.genMap(LevelType.DUNGEON_LEVEL, 100, 100, 15, seed);
				break;
			case LevelType.DUNGEON_NAME:
				map.genMap(LevelType.FOREST_LEVEL, 100, 100, 15, seed);
				break;
			default:
				map.genMap(LevelType.FOREST_LEVEL, 100, 100, 15, seed);
				break;
			}
		}
		entities.clear();
		particles.killAllParticles();
		entities.add(player);
		player.updateAge();
		player.getPosition().set(map.getPlayerSpawnLocation());
		camera = new Camera(player.getPosition());
//		while(map.getEnemySpawnLocations().size()>0){
//			entities.add(new Slime(this, map.getNextEnemySpawnPosition(), FloatColor.BROWN));				
//		}
//		entities.add(new ItemPickUp(this, new ArmClothing("leatherArms", this, -0.05f, -0.1f, 0.05f, 0, 0, 0),map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new ChestClothing("leatherChestPlate", this, -0.05f, -0.1f, 0, 0, 0.05f, 0),map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new LegClothing("leatherLegs", this, -0.05f, -0.1f, 0, 0.05f, 0, 0), map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new ArmClothing("ironArms", this, -0.1f, -0.2f, 0.1f, 0, 0, 0), map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new ChestClothing("ironChestPlate", this, -0.1f, -0.2f, 0, 0, 0.1f, 0), map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new LegClothing("ironLegs", this, -0.1f, -0.2f, 0, 0.1f, 0, 0), map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new LongSword(this), map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new Spear(this), map.getNextPickupSpawnPosition()));
//		entities.add(new ItemPickUp(this, new Sheild(this), map.getNextPickupSpawnPosition()));	
//		entities.add(new Fire(this, map.getLevelExitLocation()));	
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
		} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void update(Game game) {
		if(game.getControls().isKeyHeld(Keyboard.KEY_EQUALS) || game.getControls().getController().isButtonPressed(4)){
			unitResolution-=deltaTime * 20;
		}
		if(game.getControls().isKeyHeld(Keyboard.KEY_MINUS) || game.getControls().getController().isButtonPressed(5)){
			unitResolution+=deltaTime * 20;
		}
		if(game.getControls().isKeyTyped(Keyboard.KEY_BACK) || game.getControls().getController().isButtonPressed(6)){
			unitResolution = BASE_UNITRESOLUTION;
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
		//corner locations in game
		float left = camera.getPosition().getX() - (float)(graphics.getResolution().getX()/2/unitResolution); 
		float right = camera.getPosition().getX() + (float)(graphics.getResolution().getX()/2/unitResolution); 
		float top = camera.getPosition().getY() - (float)(graphics.getResolution().getY()/2/unitResolution); 
		float bottom = camera.getPosition().getY() + (float)(graphics.getResolution().getY()/2/unitResolution); 
		
		//start world drawing
		graphics.enableWorldCamera(camera.getPosition().getX(), camera.getPosition().getY(), unitResolution);
		//draw entities
		for(int i = 0;i < entities.size(); i++){
			if(entities.get(i).isOnScreen(top, bottom, left, right)){
				entities.get(i).render(graphics);				
			}
		}
		//draw particles
		particles.render(graphics, top, bottom, left, right);
		//draw shadows to texture
		drawShadows(graphics,top,bottom,left,right);
		//draw the map
		map.render(graphics, left, right, top, bottom);
		//end world drawing
		graphics.disableWorldCamera();
		//render ui objects here
		
		//debug draw shadow buffer texture
		//graphics.drawImage(shadowBuffer, 400, 300, -900, 800, -600, 0);
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
		for(int i = 0;i < entities.size(); i++){
			Entity e = entities.get(i); 
			if(e.isOnScreen(top, bottom, left, right)){
				float y = e.getPosition().getY();
				graphics.drawRectangle(e.getPosition().getX(), y, 0,
						e.getBoundingBox().getX(), e.getBoundingBox().getY(), 0);
			}
		}
		//draw all the particle shadows
		ArrayList<Particle> particleList = particles.getActiveParticles();
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
}
