package com.seaSaltedToaster.restaurantGame.objects.people;

import java.util.*;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.ai.person.GoToAction;
import com.seaSaltedToaster.restaurantGame.ai.person.WaitAction;
import com.seaSaltedToaster.restaurantGame.ai.person.waiter.FindHostStation;
import com.seaSaltedToaster.restaurantGame.ai.person.waiter.GiveTableOrder;
import com.seaSaltedToaster.restaurantGame.ai.person.waiter.TakeOrder;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingList;
import com.seaSaltedToaster.restaurantGame.objects.Restaurant;
import com.seaSaltedToaster.restaurantGame.objects.food.ItemOrder;
import com.seaSaltedToaster.simpleEngine.entity.componentArchitecture.Component;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class ServerComponent extends Employee {
	
	public ItemOrder order;
	
	@Override
	public void init() {
		MainApp.restaurant.servers.add(this);
		super.name = "Waiter";
		this.model = BuildingList.getBuilding("Waiter").getEntity().getModel();
	}
	
	public void takeOrder(ItemOrder itemOrder) {
		ActionComponent comp = (ActionComponent) entity.getComponent("Action");
		this.order = itemOrder;
		this.order.setTaken(true);
		
		Vector3f tablePosition = itemOrder.getTable().entity.getPosition();
		//comp.getActions().add(new WaitAction(Restaurant.GET_ORDER_WAIT));
		comp.getActions().add(new GoToAction(tablePosition, entity, true));
		comp.getActions().add(new TakeOrder(itemOrder));
	}
	
	public void deliverOrder(ItemOrder itemOrder) {
		ActionComponent comp = (ActionComponent) entity.getComponent("Action");
		this.setStatus("Delivering Order");
		this.order = itemOrder;
		
		if(order.getTable() == null) return;
		Vector3f tablePosition = order.getTable().entity.getPosition();
		comp.getActions().add(new GoToAction(MainApp.restaurant.getChef().getPosition(), entity, true));
		comp.getActions().add(new WaitAction(Restaurant.GET_ORDER_WAIT));
		comp.getActions().add(new GoToAction(tablePosition, entity, true));
		comp.getActions().add(new GiveTableOrder(order));
		comp.getActions().add(new FindHostStation());
	}

	@Override
	public String getComponentType() {
		return "Server";
	}

	@Override
	public boolean changesRenderer() {
		return false;
	}

	@Override
	public Component copyInstance() {
		return new ServerComponent();
	}

	@Override
	public void fireEmployee() {
			if(order != null) {
				MainApp.restaurant.orders.remove(order);
				order.setCooked(false);
				order.setDelivered(false);
				order.setTaken(false);
				MainApp.restaurant.orders.add(order);
			}
			
		MainApp.restaurant.servers.remove(this);
		
		MainApp.restaurant.layers.get(0).getBuildings().remove(this.getEntity());
	}

}
