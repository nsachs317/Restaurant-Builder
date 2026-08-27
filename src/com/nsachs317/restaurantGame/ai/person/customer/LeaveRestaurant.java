package com.nsachs317.restaurantGame.ai.person.customer;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.objects.seating.SeatComponent;
import com.nsachs317.restaurantGame.objects.seating.TableComponent;
import com.nsachs317.restaurantGame.save.SaveSystem;
import com.nsachs317.simpleEngine.entity.Entity;

public class LeaveRestaurant extends Action {
	
	private PartyLeader leader;

	public LeaveRestaurant(PartyLeader member) {
		this.leader = member;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update() {

	}

	@Override
	public boolean isDone() {
		this.leader = (PartyLeader) object.getComponent("PartyLeader");
		
		TableComponent table = ((PartySeating) leader.getEntity().getComponent("PartySeating")).getSeating().get(object).getTable();
		table.setFoodItem(null);
		table.setTaken(false);
		
		for(SeatComponent seat : table.getChairs()) {
			seat.setTaken(false);
		}
		
		MainApp.restaurant.money += 100;
		
		for(Entity entity : leader.getPartyMembers()) {
			BuildingId id = (BuildingId) entity.getComponent("BuildingId");
			int index = id.getLayer().getBuildings().indexOf(entity);
			if(index > 0)
				id.getLayer().getBuildings().set(index, null);
			System.out.println("Party has left the restaurant : " + leader.getPartyLeader().getId());
		}
		
		BuildingId id = (BuildingId) object.getComponent("BuildingId");
		int index = id.getLayer().getBuildings().indexOf(object);
		id.getLayer().getBuildings().set(index, null);
		
		return true;
	}

	@Override
	public String type() {
		return "Leave";
	}

	@Override
	public void saveAction(SaveSystem system) {
		BuildingId id = (BuildingId) super.object.getComponent("BuildingId");
		int index = id.getId();
		
		String data = "" + id.getId();

		system.saveAction(index, super.actionIndex, type(), "LEAVE_DATA[" + data + "]");
	}

	@Override
	public void loadAction(String data) {
		String line = data.replace("[", "").replace("]", "").replace("LEAVE_DATA", "").replace(";", "");
		int leaderId = Integer.parseInt(line);
		
	}

}
