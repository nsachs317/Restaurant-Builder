package com.seaSaltedToaster.restaurantGame.ai.person.customer;

import com.seaSaltedToaster.restaurantGame.ai.person.Action;
import com.seaSaltedToaster.restaurantGame.building.BuildingId;
import com.seaSaltedToaster.restaurantGame.objects.seating.SeatComponent;
import com.seaSaltedToaster.restaurantGame.save.SaveSystem;

public class WaitForOrderCustomer extends Action {

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
		
		SeatComponent seat = seating.getSeating().get(object);
		if(seat == null) return false;
		
		if(seat.getTable().hasFood)
			return true;
		
		return false;
	}

	@Override
	public String type() {
		return "WaitForOrderCustomer";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) super.object.getComponent("BuildingId");
		int index = id.getId();

		system.saveAction(index, super.actionIndex, type(), "WAIT_FOR_ORDER_CUSTOMER_DATA");
	}

	@Override
	public void loadAction(String data) {
		
	}

}
