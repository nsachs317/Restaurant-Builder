package com.nsachs317.restaurantGame.ai.person;

import com.nsachs317.restaurantGame.ai.Mover;
import com.nsachs317.restaurantGame.save.SaveSystem;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public class MoveToAction extends Action {
	
	private Mover mover;
	
	private Entity entity;
	private Vector3f dest;

	public MoveToAction(Vector3f go, Entity object) {
		this.dest = go;
		this.entity = object;
	}

	@Override
	public void start() {
		this.mover = new Mover(entity);
		this.mover.setTarget(dest);
		
		entity.getPosition().setY(dest.getY());
	}

	@Override
	public void update() {
		Vector3f newPos = mover.update();
		if(newPos != null)
			entity.getTransform().setPosition(newPos);
	}

	@Override
	public boolean isDone() {
		return mover.reachedTarget();
	}

	@Override
	public String type() {
		return null;
	}

	@Override
	public void saveAction(SaveSystem system) {

	}

	@Override
	public void loadAction(String data) {
		
	}

}
