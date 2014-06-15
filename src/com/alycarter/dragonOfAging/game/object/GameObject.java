package com.alycarter.dragonOfAging.game.object;

public class GameObject {

	private boolean needsRemoving;
	private String name;
	
	public GameObject(String name) {
		//set its name and that it doesnt need removing
		this.name = name;
		needsRemoving = false;
	}

	public String getName(){
		//return the objects name
		return name;
	}
	
	public void markForRemoval(){
		//mark the object as needing removing
		needsRemoving = true;
	}
	
	public boolean needsRemoving(){
		//does the object need removing
		return needsRemoving;
	}
	
	public String toString(){
		//returns a handy string for outputting to console
		return "name: "+name+" marked for removal:" + needsRemoving;
	}
}
