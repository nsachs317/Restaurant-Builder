package com.nsachs317.restaurantGame.ai.person.waiter;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.restaurantGame.objects.people.ServerComponent;
import com.nsachs317.restaurantGame.save.SaveSystem;

public class GiveChefOrder extends Action {
	
	private int orderId = -32767;

	public GiveChefOrder(ItemOrder order) {
		if(order != null)
			this.orderId = order.getId(); //TODO multiple orders
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isDone() {
		//Get order
		ItemOrder order = MainApp.restaurant.getOrder(orderId);
		
		if(order == null) return false; //TODO break case
		
		//Change order properties
		MainApp.restaurant.orders.remove(order);
		MainApp.restaurant.chefOrders.add(order);
		
		//Reset tree
		ActionComponent aComp = (ActionComponent) object.getComponent("Action");
		aComp.doTree(true);
		
		System.out.println("Waiter " + object.getId() + " gave order " + order.getId() + " to Chef");
		
		ServerComponent c = (ServerComponent) object.getComponent("Server");
		c.order = null;
		
		//Return
		return true;
	}

	@Override
	public String type() {
		return "GiveChefOrder";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "GIVE_CHEF_ORDER[" + orderId + "]");
	}

	@Override
	public void loadAction(String data) {
		String line = data.replace("[", "").replace("]", "").replace("GIVE_CHEF_ORDER", "").replace(";", "");
		this.orderId = Integer.parseInt(line);
	}

}
