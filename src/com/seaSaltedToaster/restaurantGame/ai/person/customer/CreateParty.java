package com.seaSaltedToaster.restaurantGame.ai.person.customer;

import java.util.ArrayList;
import java.util.List;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.ai.person.Action;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.building.Building;
import com.seaSaltedToaster.restaurantGame.building.BuildingId;
import com.seaSaltedToaster.restaurantGame.building.BuildingManager;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingList;
import com.seaSaltedToaster.restaurantGame.building.layers.BuildLayer;
import com.seaSaltedToaster.restaurantGame.objects.seating.TableComponent;
import com.seaSaltedToaster.restaurantGame.save.SaveSystem;
import com.seaSaltedToaster.simpleEngine.entity.Entity;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class CreateParty extends Action {
	
	private List<Entity> partyMembers;
	private TableComponent table;
	private int partySize = -127;
	
	public CreateParty() {
		this.partyMembers = new ArrayList<Entity>();
	}
	
	public CreateParty(List<Entity> partyMembers, int partySize) {
		this.partyMembers = partyMembers;
		this.partySize = partySize;
	}


	@Override
	public void start() {
		//None
	}

	private Vector3f randomShirtColor() {
		return new Vector3f(Math.random(), Math.random(), Math.random());
	}
	
	private Vector3f randomSkinTone(BuildingId id) {
		float randomValue = Math.max((float) Math.random()+0.5f, 0.5f);
		return id.getSecondary().copy().scale(randomValue);
	}

	@Override
	public void update() {
		//None
	}

	@Override
	public boolean isDone() {
		if(partySize != -127) return true;
		
		//Main person leader
//		this.partySize =  MainApp.restaurant.capacity();
		this.partySize = (int) Math.abs(Math.random() * MainApp.restaurant.capacity());
		if(partySize <= 0)
			partySize++;
		
		
		BuildingId leaderId = (BuildingId) object.getComponent("BuildingId");
		
		if(this.partySize < 1 || MainApp.restaurant.capacity() < 1)
			return false;

		//Create our party
		for(int i = 0; i < partySize; i++) 
		{
			if(i == 0)
			{
				leaderId.setPrimary(randomShirtColor());
				leaderId.setSecondary(randomSkinTone(leaderId));
			}
			else
			{
				Building bld = BuildingList.getBuilding("Customer_TEST");
				Entity member = bld.getEntity().copyEntity();
				member.getPosition().setX(object.getPosition().x);
				member.getPosition().setZ(object.getPosition().z);

				partyMembers.add(member);
				leaderId.getLayer().addBuilding(member, bld, -127, true);
				
				ActionComponent memberAComp = (ActionComponent) member.getComponent("Action");
				memberAComp.setTree("Guest");
				memberAComp.doTree(false);
				
				PartyMember memberComp = new PartyMember(object);
				member.addComponent(memberComp);
				
				BuildingId id = (BuildingId) member.getComponent("BuildingId");
				id.setPrimary(randomShirtColor());
				id.setSecondary(randomSkinTone(id));
			}
		}
		
		PartyLeader leader = new PartyLeader(object, partyMembers, partySize);
		object.addComponent(leader);
		
		for(Entity member : partyMembers) {
			ActionComponent memberAComp = (ActionComponent) member.getComponent("Action");
			HoverParty party = new HoverParty();
			party.object = member;
			memberAComp.getActions().add(party);
		}
		
		return partySize != -127;
	}

	@Override
	public String type() {
		return "CreateParty";
	}

	@Override
	public void saveAction(SaveSystem system) {
		//SAVE ACTION GoTo
		BuildingId id = (BuildingId) super.object.getComponent("BuildingId");
		int index = id.getId();
		PartyLeader leader = (PartyLeader) super.object.getComponent("PartyLeader");
		
		String data = "PARTY_DATA[";
		
		BuildingId tableId = (BuildingId) table.getEntity().getComponent("BuildingId");
		data += (tableId.getId() + "=");
		
		for(Entity entity : leader.getPartyMembers()) {
			BuildingId partyMember = (BuildingId) entity.getComponent("BuildingId");
			data += (partyMember.getId() + "=");
		}
		data += "]";
		system.saveAction(index, super.actionIndex, type(), data);
	}

	@Override
	public void loadAction(String data) {		
		String line = data.replace("[", "").replace("]", "").replace("PARTY_DATA", "").replace(";", "");
		String[] lines = line.split("=");
		
		int tableId = -1;
		TableComponent tableParty = null;
		
		this.partySize = 0;
		for(String member : lines) {
			if(lines.length < 1) break;
			if(member.equalsIgnoreCase("")) continue;
			
			//Load table component
			if(lines[0] == member) {
				tableId = Integer.parseInt(member);
				continue;
			}
			
			int num = Integer.parseInt(member);
			System.out.println(num);

			for(BuildLayer layer : MainApp.restaurant.layers) {
				boolean exists = BuildingManager.getBuildingWithID(num) != null;
				System.out.println(exists);
				for(Entity entity : layer.getBuildings()) {
					
					BuildingId id = (BuildingId) entity.getComponent("BuildingId");
					if(id.getId() == num) {
						
						ActionComponent memberAComp = (ActionComponent) entity.getComponent("Action");
						memberAComp.doTree(false);

						PartyMember memberComp = new PartyMember(object);
						entity.addComponent(memberComp);
						
						this.partyMembers.add(entity);
						this.partySize++;
					}
				}
			}
		}
		tableParty = (TableComponent) BuildingManager.getBuildingWithID(tableId).getComponent("Table");
		
		PartyLeader leader = new PartyLeader(object, partyMembers, partySize);
		leader.setTable(tableParty);
		object.addComponent(leader);
		
		for(Entity member : partyMembers) {
			PartyMember memberComp = (PartyMember) member.getComponent("PartyMember");
			memberComp.setLeader(object);
		}
		
		ActionComponent comp = (ActionComponent) object.getComponent("Action");
		comp.doTree(false);
	}

	public TableComponent getTable() {
		return table;
	}

	public void setTable(TableComponent table) {
		this.table = table;
	}

}
