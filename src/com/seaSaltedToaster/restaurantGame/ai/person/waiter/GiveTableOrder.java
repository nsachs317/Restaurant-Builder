package com.seaSaltedToaster.restaurantGame.ai.person.waiter;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.ai.person.Action;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.ai.person.WaitAction;
import com.seaSaltedToaster.restaurantGame.ai.person.customer.LeaveRestaurant;
import com.seaSaltedToaster.restaurantGame.ai.person.customer.PartyLeader;
import com.seaSaltedToaster.restaurantGame.building.BuildingId;
import com.seaSaltedToaster.restaurantGame.objects.Restaurant;
import com.seaSaltedToaster.restaurantGame.objects.food.Food;
import com.seaSaltedToaster.restaurantGame.objects.food.ItemOrder;
import com.seaSaltedToaster.restaurantGame.objects.people.ServerComponent;
import com.seaSaltedToaster.restaurantGame.objects.seating.TableComponent;
import com.seaSaltedToaster.restaurantGame.save.SaveSystem;

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
