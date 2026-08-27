package com.nsachs317.restaurantGame.ai.person.chef;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.restaurantGame.save.SaveSystem;

public class FinishCooking extends Action {
	
	private int orderId;

	public FinishCooking(int id) {
		this.orderId = id;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isDone() {
		ItemOrder order = MainApp.restaurant.getChefOrder(orderId);
		if(order == null) return false;
		
		//Change order properties
		order.setCooked(true);
		MainApp.restaurant.chefOrders.remove(order);
		MainApp.restaurant.orders.add(order);
		System.out.println("Chef Cooked Order : " + order.getId());

		//Reset tree
		ActionComponent aComp = (ActionComponent) object.getComponent("Action");
		aComp.doTree(true);
		
		//Return
		return true;
	}

	@Override
	public String type() {
		return "FinishCooking";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "COOK[" + orderId + "]");
	}

	@Override
	public void loadAction(String data) {
		String line = data.replace("[", "").replace("]", "").replace("COOK", "").replace(";", "");
		this.orderId = Integer.parseInt(line);
		if(orderId == -32767) {
			System.err.println("Could not load order ID");
		}
			
	}

}
