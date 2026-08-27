package com.nsachs317.restaurantGame.objects;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.building.Building;
import com.nsachs317.restaurantGame.building.categories.BuildingList;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.componentArchitecture.Component;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.utilities.Timer;
import com.nsachs317.simpleEngine.utilities.skybox.TimeHandler;

public class DoorComponent extends Component {

	//Timing
	private Timer timer;
	private int waitTime = 3;
	
	@Override
	public void init() {
		this.timer = new Timer(waitTime);
		this.timer.start();
	}

	@Override
	public void update() {
		this.timer.update(TimeHandler.gameTime);
		
		Restaurant restaurant = MainApp.restaurant;
		if(timer.isFinished() && !restaurant.atCapacity() && restaurant.capacity() > 0 && restaurant.isOpen) {
			spawnPerson(restaurant); //TODO SPAWN PEOPLE
		}
	}

	private void spawnPerson(Restaurant restaurant) {
		//Random
		float random = (float) Math.abs(Math.random());
		if(random < 0.5f) {
			timer.stop();
			timer.start();
			return;
		}
		
		//Create entity
		Building spawn = BuildingList.getBuilding("Customer_TEST");
		Entity newEnt = spawn.getEntity().copyEntity();
		newEnt.getTransform().setPosition(entity.getTransform().getPosition().copy());
		
		//Get layer
		restaurant.layers.get(0).addBuilding(newEnt, spawn, -127, true);
		
		//Timer
		timer.stop();
		timer.start();		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getComponentType() {
		return "Door";
	}

	@Override
	public boolean changesRenderer() {
		return false;
	}

	@Override
	public Component copyInstance() {
		return new DoorComponent();
	}

}
