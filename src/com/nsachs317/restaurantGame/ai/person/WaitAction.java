package com.nsachs317.restaurantGame.ai.person;

import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.save.SaveSystem;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.utilities.Timer;
import com.nsachs317.simpleEngine.utilities.skybox.TimeHandler;

public class WaitAction extends Action {

	//Timer that will count our wait
	private Timer timer;
	private float time;
	
	public WaitAction(float time) {
		this.timer = new Timer(time);
		this.time = time;
	}
	
	@Override
	public void start() {
		//Start our timer
		this.timer.start();
	}

	@Override
	public void update() {
		//Update current time every frame
		double delta = TimeHandler.gameTime;
		this.timer.update(delta);
	}

	@Override
	public boolean isDone() {
		//Return finishing state
		return timer.isFinished();
	}
	

	@Override
	public String type() {
		return "Wait";
	}

	@Override
	public void saveAction(SaveSystem system) {		
		//SAVE ACTION Wait
		BuildingId id = (BuildingId) super.object.getComponent("BuildingId");
		int index = id.getId();
		
		String data = "TIME_DATA[" + timer.getCurrentTime() + "=" + timer.getEndTime() + "=" + timer.getStartTime() + "=" + time + "]";
		system.saveAction(index, super.actionIndex, type(), data);
	}

	@Override
	public void loadAction(String data) {
		//TODO LOAD ACTION Wait
		String[] line = data.replace("[", "").replace("]", "").replace("TIME_DATA", "").replace(";", "").split("=");
		float cur = Float.parseFloat(line[0]);
		float end = Float.parseFloat(line[1]);
		float start = Float.parseFloat(line[2]);
		float length = Float.parseFloat(line[3]);
		
		this.time = length;
		this.timer = new Timer(length);
		this.timer.setCurrentTime(cur);
		this.timer.setEndTime(end);
		this.timer.setStartTime(start);
	}

}
