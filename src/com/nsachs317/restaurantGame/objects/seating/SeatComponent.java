package com.nsachs317.restaurantGame.objects.seating;

import com.nsachs317.simpleEngine.entity.componentArchitecture.Component;
import com.nsachs317.simpleEngine.utilities.MathUtils;
import com.nsachs317.simpleEngine.utilities.Vector2f;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public class SeatComponent extends Component {
	
	//Properties
	public Vector3f seatPosition, origin, facing;
	public float height;

	//Table
	private TableComponent table;
	private boolean isTaken = false;
	
	public SeatComponent(Vector2f position, float height) {
		this.origin = new Vector3f(position.x, height, position.y);
	}
	
	@Override
	public void init() {
		//Rotate object
		float angleDeg = -entity.getTransform().getRotation().y;
		this.seatPosition = MathUtils.rotatePointAtCenter(origin.copy(), angleDeg);
		this.seatPosition = this.seatPosition.add(entity.getPosition());
		
		//this.seatPosition = entity.getPosition().copy();
		
		this.facing = MathUtils.projectVertex(origin.copy(), 0.25f, angleDeg + 90);
	}

	public TableComponent getTable() {
		return table;
	}

	public void setTable(TableComponent table) {
		this.table = table;
	}

	public boolean isTaken() {
		return isTaken;
	}

	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void reset() {
		
	}

	@Override
	public String getComponentType() {
		return "Chair";
	}

	@Override
	public boolean changesRenderer() {
		return false;
	}

	@Override
	public Component copyInstance() {
		return new SeatComponent(new Vector2f(origin.x, origin.z), height);
	}

}
