package com.seaSaltedToaster.restaurantGame.ai.person.waiter;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.ai.person.Action;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.ai.person.GoToAction;
import com.seaSaltedToaster.restaurantGame.ai.person.WaitAction;
import com.seaSaltedToaster.restaurantGame.building.BuildingId;
import com.seaSaltedToaster.restaurantGame.objects.Restaurant;
import com.seaSaltedToaster.restaurantGame.objects.food.ItemOrder;
import com.seaSaltedToaster.restaurantGame.objects.people.ServerComponent;
import com.seaSaltedToaster.restaurantGame.save.SaveSystem;

public class TakeOrder extends Action {
	
	public static int TAKE_ORDER_WAIT = 5;
	
	private ItemOrder order;
	
	public TakeOrder(ItemOrder itemOrder) {
		if(itemOrder == null) return;
		this.order = itemOrder;
	}

	@Override
	public void start() {
//		if(order == null) { //TODO is this important?
//			this.order = MainApp.restaurant.getOrder(orderId);
//		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isDone() {
		//Meet preconditions
		if(!meetsPreconditions()) return false;
		
		if(order == null) return true;
		
		//Change the values of the ItemOrder
		order.setTaken(true);
		
		ServerComponent c = (ServerComponent) object.getComponent("Server");
		c.setStatus("Taking Order");
		
		//Add the next few tasks
		Restaurant rest = MainApp.restaurant;
		ActionComponent aComp = (ActionComponent) object.getComponent("Action");
		aComp.getActions().add(new WaitAction(TAKE_ORDER_WAIT));
		aComp.getActions().add(new GoToAction(rest.getChef().getPosition(), object, false));
		aComp.getActions().add(new GiveChefOrder(order));
		
		//Return
		return true;
	}

	private boolean meetsPreconditions() {
		Restaurant rest = MainApp.restaurant;
		boolean hasChef = (rest.chefs.size() > 0);
		return hasChef;
	}

	@Override
	public String type() {
		return "TakeOrder";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getId();

		if(order == null)
			system.saveAction(index, super.actionIndex, type(), "TAKE_ORDER[" + -32767 + "]");
		else {
			String saveData = "";
			saveData += ("" + order.getId() +","); 
			system.saveAction(index, super.actionIndex, type(), "TAKE_ORDER[" + saveData.substring(0, saveData.length()-1) + "]");
		}
	}

	@Override
	public void loadAction(String data) {
		String line = data.replace("[", "").replace("]", "").replace("TAKE_ORDER", "").replace(";", "");
		String[] ids = line.split(",");
		for(String part : ids) {
			int id = Integer.parseInt(part);
			if((Integer) id == null || id == -32767)
				System.err.println("Could not read order ID");
			
			order = MainApp.restaurant.getOrder(id);
		}
	}

}
