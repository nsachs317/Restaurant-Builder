package com.seaSaltedToaster.restaurantGame.ai.person.waiter;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.ai.person.Action;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.ai.person.GoToAction;
import com.seaSaltedToaster.restaurantGame.ai.person.WaitAction;
import com.seaSaltedToaster.restaurantGame.building.BuildingId;
import com.seaSaltedToaster.restaurantGame.save.SaveSystem;
import com.seaSaltedToaster.simpleEngine.entity.Entity;
import com.seaSaltedToaster.restaurantGame.objects.people.*;

public class FindHostStation extends Action {

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isDone() {
		for(Entity object : MainApp.restaurant.layers.get(0).getBuildings()) {
			if(object == null) continue;
			HostStand id = (HostStand) object.getComponent("HostStand");
			if(id == null) continue;
						
			ActionComponent ac = (ActionComponent) this.object.getComponent("Action");
			if(ac.getActions().size() < 3) {
				return true;
			}
			
			ac.getActions().add(0, new GoToAction(object.getPosition(), this.object, true));
			
			System.out.println("Waiter found host station, Moving to " + object.getPosition());
			return true;
		}

		return false;
	}

	@Override
	public String type() {
		return "FindHostStation";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "HOST_STATION[]");
	}

	@Override
	public void loadAction(String data) {
		
	}

}
