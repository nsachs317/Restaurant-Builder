package com.nsachs317.restaurantGame.objects.people;

import java.util.*;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.ai.person.GoToAction;
import com.nsachs317.restaurantGame.ai.person.WaitAction;
import com.nsachs317.restaurantGame.ai.person.waiter.FindHostStation;
import com.nsachs317.restaurantGame.ai.person.waiter.GiveTableOrder;
import com.nsachs317.restaurantGame.ai.person.waiter.TakeOrder;
import com.nsachs317.restaurantGame.building.categories.BuildingList;
import com.nsachs317.restaurantGame.objects.Restaurant;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.simpleEngine.entity.componentArchitecture.Component;
import com.nsachs317.simpleEngine.utilities.Vector3f;

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
