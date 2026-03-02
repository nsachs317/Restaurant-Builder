package com.seaSaltedToaster.restaurantGame.building.floors;

import java.util.List;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.building.Building;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingList;
import com.seaSaltedToaster.restaurantGame.objects.FloorComponent;
import com.seaSaltedToaster.simpleEngine.models.wavefront.ObjData;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class RepeatingFloorGenerator extends FloorGenerator {

	private FloorComponent id;
	
	private ObjData data;
	
	private float height = 0.0125f;
	
	public RepeatingFloorGenerator(FloorComponent comp) {
		this.id = comp;
		
		Building building = BuildingList.getBuilding(id.getType());
		String model = building.modelPath;
		
		ObjData data = MainApp.restaurant.engine.getObjLoader().getModelData(model);
		this.data = data;
	}

	@Override
	public void addTile(List<Vector3f> vertices, List<Integer> triangles, List<Vector3f> colors, float x, float z) {
		int size = vertices.size();
		
		for(int i = 0; i < data.getPositions().length; i+=3) {
			float dx = data.getPositions()[i] + x + 0.5f;
			float dy = data.getPositions()[i+1];
			float dz = data.getPositions()[i+2] + z + 0.5f;
			Vector3f vertex = new Vector3f(dx,dy + height,dz);
			vertices.add(vertex);

			float r = data.getColors()[i];
			float g = data.getColors()[i+1];
			float b = data.getColors()[i+2];
			Vector3f color = new Vector3f(r,g,b);
			colors.add(color);

			height += 0.0001f;
		}
		
		for(int j = 0; j < data.getIndices().length; j++) {
			int triangle = data.getIndices()[j] + size;
			triangles.add(triangle);
		}
	}

	@Override
	public void addCornerTile() {
//		float dx = data.getPositions()[i] + x + 0.5f;
//		float dy = data.getPositions()[i+1];
//		float dz = data.getPositions()[i+2] + z + 0.5f;
//		Vector3f vertex = new Vector3f(dx,dy + height,dz);
//		vertices.add(vertex);
//		triangles.add(vertices.indexOf(vertex));
	}

	@Override
	public String getType() {
		return "Repeating";
	}

}
