package com.nsachs317.restaurantGame.building.layers;

import com.nsachs317.restaurantGame.building.Building;
import com.nsachs317.simpleEngine.entity.Entity;

public interface PlacementListener {
	
	public void notifyPlacement(Entity obj, Building type);

}
