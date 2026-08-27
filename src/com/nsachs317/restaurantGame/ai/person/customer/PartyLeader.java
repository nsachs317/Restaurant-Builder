package com.nsachs317.restaurantGame.ai.person.customer;

import java.util.List;

import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.objects.seating.TableComponent;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.componentArchitecture.Component;

public class PartyLeader extends Component {
	
	private List<Entity> partyMembers;
	private Entity partyLeader;
	private TableComponent table;
	private int partySize = -127;
	
	public PartyLeader(Entity object, List<Entity> partyMembers, int partySize) {
		this.partyLeader = object;
		this.partyMembers = partyMembers;
		this.partySize = partySize;
	}
	
	public void save() {
		CreateParty party = new CreateParty(partyMembers, partySize);
		party.object = partyLeader;
		party.actionIndex = 0;
		party.setTable(table);
		
		ActionComponent comp = (ActionComponent) partyLeader.getComponent("Action");
		comp.getActions().add(0, party);
	}

	public TableComponent getTable() {
		return table;
	}

	public void setTable(TableComponent table) {
		this.table = table;
	}

	public List<Entity> getPartyMembers() {
		return partyMembers;
	}

	public void setPartyMembers(List<Entity> partyMembers) {
		this.partyMembers = partyMembers;
	}

	public Entity getPartyLeader() {
		return partyLeader;
	}

	public void setPartyLeader(Entity partyLeader) {
		this.partyLeader = partyLeader;
	}

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void reset() {
		
	}

	@Override
	public String getComponentType() {
		return "PartyLeader";
	}

	@Override
	public boolean changesRenderer() {
		return false;
	}

	@Override
	public Component copyInstance() {
		return null;
	}

}
