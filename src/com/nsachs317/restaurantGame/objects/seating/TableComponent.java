package com.nsachs317.restaurantGame.objects.seating;

import java.util.ArrayList;
import java.util.List;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.building.Building;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.building.BuildingManager;
import com.nsachs317.restaurantGame.building.BuildingType;
import com.nsachs317.restaurantGame.building.layers.BuildLayer;
import com.nsachs317.restaurantGame.building.layers.PlacementListener;
import com.nsachs317.restaurantGame.objects.food.Food;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.componentArchitecture.Component;
import com.nsachs317.simpleEngine.entity.componentArchitecture.ModelComponent;
import com.nsachs317.simpleEngine.utilities.MathUtils;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public class TableComponent extends Component implements PlacementListener {
		
	//Constants
	private static float MAX_DIST = 1, HEIGHT = 0.4f, dx = 0.2f, dz = 0.125f;
	
	//Claim state
	private boolean isTaken = false, checkedSelf = false;
	private int capacity = 0;
	
	//Chairs
	private Entity tablePartyLeader;
	private List<SeatComponent> chairs;
	
	//Food on table
	public List<Entity> foodItems;
	public boolean hasFood = false;
	
	public TableComponent() {
		this.chairs = new ArrayList<SeatComponent>();
	}

	private void addFoodPlaceholders() {
		this.foodItems = new ArrayList<Entity>();
		float x = this.getEntity().getPosition().x;
		float z = this.getEntity().getPosition().z;

		this.foodItems.add(new Entity(addSpot(x + dx, z + dz)));
		this.foodItems.add(new Entity(addSpot(x + dx, z - dz)));
		this.foodItems.add(new Entity(addSpot(x - dx, z + dz)));
		this.foodItems.add(new Entity(addSpot(x - dx, z - dz)));
		
		for(Entity item : foodItems) {
			MainApp.restaurant.engine.addEntity(item);
			
			ModelComponent comp = new ModelComponent(null);
			item.addComponent(comp);
		}
		setFoodItem(null);
	}
	
	private Vector3f addSpot(float x, float z) {
		float angleDeg = -entity.getTransform().getRotation().y;
		Vector3f foodPosition = MathUtils.rotatePoint(new Vector3f(x, HEIGHT, z), entity.getPosition(), angleDeg);
		return foodPosition;
	}
	
	public void setFoodItem(Food[] food) {	
		for(int i = 0; i < foodItems.size(); i++) {		
			Entity item = foodItems.get(i);
			ModelComponent comp = (ModelComponent) item.getComponent("Model");
			
			if(food != null && i < food.length && food[i] != null) {
				comp.setMesh(food[i].getVao());
				hasFood = true;				
			}
			if(food == null) {
				comp.setMesh(null);
				hasFood = false;
			}
		}
	}

	@Override
	public void init() {
		MainApp.restaurant.tables.add(this);
		BuildingManager.listeners.add(this);
		checkExisting();
		addFoodPlaceholders();
	}
	
	public void checkExisting() {
		BuildLayer layer = ((BuildingId) entity.getComponent("BuildingId")).getLayer();
		for(Entity entity : layer.getBuildings()) {
			if(entity == null || !entity.hasComponent("BuildingId")) continue;
			BuildingId id = (BuildingId) entity.getComponent("BuildingId");
			this.notifyPlacement(entity, id.getType());
		}
	}

	@Override
	public void notifyPlacement(Entity obj, Building type) {
		boolean isChair = (type.type == BuildingType.Object) && obj.hasComponent("Chair");
		if(isChair) 
		{
			Vector3f toChair = obj.getPosition().copy().subtract(entity.getPosition().copy());
			float dist = Math.abs(toChair.length());
			
			if(dist <= MAX_DIST) 
			{
				SeatComponent component = (SeatComponent) obj.getComponent("Chair");
				if(component.getTable() != null) return;
				
				int dot = (int) Math.toDegrees(Vector3f.angle(toChair, component.facing));
				if(dot != 179 && dot != 180 && dot != 149 && dot != 150) return;
				
				component.setTable(this);
				this.chairs.add(component);
				this.capacity++;
				if(chairs.size() == 1)
					System.out.println("made table");
			}
		}
	}
	
	public int capacity() {
		return capacity;
	}

	public Entity getTablePartyLeader() {
		return tablePartyLeader;
	}

	public void setTablePartyLeader(Entity tablePartyLeader) {
		this.tablePartyLeader = tablePartyLeader;
	}

	public List<SeatComponent> getChairs() {
		return chairs;
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isTaken() {
		return isTaken;
	}
	
	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

	@Override
	public void update() {
		if(!checkedSelf) {
			for(Component componentA : this.getEntity().getComponents()) {
				if(componentA.getComponentType() == "Chair") {
					SeatComponent component = (SeatComponent) componentA;
					component.setTable(this);
					this.chairs.add(component);
					this.capacity++;
					if(component.isTaken())
						this.isTaken = true;
				}
			}
			checkedSelf = true;
		}
	}

	@Override
	public void reset() {
		
	}

	@Override
	public String getComponentType() {
		return "Table";
	}

	@Override
	public boolean changesRenderer() {
		return false;
	}

	@Override
	public Component copyInstance() {
		TableComponent tbl = new TableComponent();
		return tbl;
	}

}
