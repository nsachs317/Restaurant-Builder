package com.nsachs317.restaurantGame.ai.person.waiter;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.ai.person.WaitAction;
import com.nsachs317.restaurantGame.ai.person.customer.LeaveRestaurant;
import com.nsachs317.restaurantGame.ai.person.customer.PartyLeader;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.objects.Restaurant;
import com.nsachs317.restaurantGame.objects.food.Food;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.restaurantGame.objects.people.ServerComponent;
import com.nsachs317.restaurantGame.objects.seating.TableComponent;
import com.nsachs317.restaurantGame.save.SaveSystem;

public class GiveTableOrder extends Action {

	private int orderId = -32767;
	
	public GiveTableOrder(ItemOrder order) {
		if(order != null)
			this.orderId = order.getId();
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
		order.setDelivered(true);
		
		//Give people the food
		TableComponent table = order.getTable();
		table.setFoodItem(order.getFoodItem());
		
		//Reset tree
		ActionComponent aComp = (ActionComponent) object.getComponent("Action");
		aComp.getActions().clear();
		aComp.doTree(true);
		
		ServerComponent c = (ServerComponent) object.getComponent("Server");
		c.order = null;
		
		//Return
		return true;
	}

	@Override
	public String type() {
		return "GiveTableOrder";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "GIVE_TABLE_ORDER[" + orderId + "]");
	}

	@Override
	public void loadAction(String data) {
		String line = data.replace("[", "").replace("]", "").replace("GIVE_TABLE_ORDER", "").replace(";", "");
		this.orderId = Integer.parseInt(line);
	}

}
