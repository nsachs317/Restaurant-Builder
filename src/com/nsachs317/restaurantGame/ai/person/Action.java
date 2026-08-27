package com.nsachs317.restaurantGame.ai.person;

import com.nsachs317.restaurantGame.save.SaveSystem;
import com.nsachs317.simpleEngine.entity.Entity;

public abstract class Action {
	
	//Entity
	public Entity object;
	public int actionIndex;
	
	//Two action methods
	public abstract void start();
	public abstract void update();
	
	//Check if action is over
	public abstract boolean isDone();
	
	//Saving and Loading
	public abstract String type();
	public abstract void saveAction(SaveSystem system);
	public abstract void loadAction(String data);

}
