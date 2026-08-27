package com.nsachs317.restaurantGame.ai.person.chef;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.objects.Restaurant;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.restaurantGame.objects.people.ChefComponent;
import com.nsachs317.restaurantGame.objects.people.ServerComponent;
import com.nsachs317.restaurantGame.save.SaveSystem;

public class WaitToCook extends Action {

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isDone() {
		ActionComponent aComp = (ActionComponent) object.getComponent("Action");
		if(aComp.getActions().size() > 1)
		{
			return true;
		}
		
		Restaurant restaurant = MainApp.restaurant;
		for(ItemOrder order : restaurant.chefOrders) {
			if(order == null) continue;
			boolean canDeliver = (order.isTaken() && !order.isCooked());
			if(canDeliver) {
				ChefComponent comp = (ChefComponent) object.getComponent("Chef");
				comp = MainApp.restaurant.chefs.get(0);
				comp.deliverOrder(order);
				System.out.println("Chef is now taking cooking order : " + order.getId());

				return true;
			}
		}
		return false;
	}


	@Override
	public String type() {
		return "WaitToCook";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "WAIT_TO_COOK[]");
	}

	@Override
	public void loadAction(String data) {
		
	}

}
