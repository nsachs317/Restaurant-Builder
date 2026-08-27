package com.nsachs317.restaurantGame.ai.person.chef;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.ai.person.GoToAction;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.save.SaveSystem;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.restaurantGame.objects.people.ChefStation;

public class FindChefStation extends Action {

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
			ChefStation id = (ChefStation) object.getComponent("ChefStation");
			if(id == null) continue;
			
			if(!id.isOccupied()) {				
				ActionComponent ac = (ActionComponent) this.object.getComponent("Action");
				if(object.getPosition().copy().subtract(this.object.getPosition().copy()).length() > 0.3)
					ac.getActions().add(new GoToAction(object.getPosition(), this.object, true));
				ac.getActions().add(new WaitToCook());
				System.out.println("Chef Found Workstation, Now moving to " + object.getTransform().getPosition());
				return true;
			}
		}
		return false;
	}

	@Override
	public String type() {
		return "FindChefStation";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "ChefStation[]");
	}

	@Override
	public void loadAction(String data) {
		
	}

}
