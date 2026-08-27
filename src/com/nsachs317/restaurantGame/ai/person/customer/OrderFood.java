package com.nsachs317.restaurantGame.ai.person.customer;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.ai.person.WaitAction;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.objects.Restaurant;
import com.nsachs317.restaurantGame.objects.food.Food;
import com.nsachs317.restaurantGame.objects.food.FoodRegistry;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.restaurantGame.objects.seating.SeatComponent;
import com.nsachs317.restaurantGame.save.SaveSystem;

public class OrderFood extends Action {

	public OrderFood() {
		
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public boolean isDone() {
		PartySeating seating = (PartySeating) object.getComponent("PartySeating");
		PartyLeader leader = (PartyLeader) object.getComponent("PartyLeader");
			
		//Party Leader Food
		int memberCount = seating.getSeating().size();
		Food[] food = new Food[memberCount];
		for(int i = 0; i < memberCount; i++) {
			food[i] = FoodRegistry.getRandom();
			System.out.println("Food chosen for order for party " + leader.getEntity().getId() + " : " + food[i].name);
		}
		
		SeatComponent seat = seating.getSeating().get(object);
		if(seat == null) return false;
			
		ItemOrder order = new ItemOrder(seat.getTable(), seat, food);
		
		//Everybody else
			
		int id = ((BuildingId)leader.getTable().getEntity().getComponent("BuildingId")).getId();
		order.setTableId(id);
		
		MainApp.restaurant.orders.add(order);
		
		//Add waiting option
		ActionComponent mComp = (ActionComponent) object.getComponent("Action");
		mComp.getActions().add(0, new WaitForOrderCustomer());
		mComp.getActions().add(1, new WaitAction(Restaurant.eatTime));
		mComp.getActions().add(2, new LeaveRestaurant(leader));
				
		//Return
		return true;
	}

	@Override
	public String type() {
		return "OrderFood";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) super.object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "ORDER_FOOD_DATA");
	}

	@Override
	public void loadAction(String data) {
		
	}

}
