package com.nsachs317.restaurantGame.ai.person;

import java.util.*;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.ai.Node;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.componentArchitecture.ModelComponent;
import com.nsachs317.simpleEngine.models.Vao;

public class NodeViewer {
	
	//
	public static Vao vao;
	
	//Path viewer
	public List<Entity> nodeEntities;

	public NodeViewer() {
		super();
		this.nodeEntities = new ArrayList<Entity>();
	}
	
	public void showPath(List<Node> nodes) {
		for(Node node : nodes) {
			Entity nEnt = new Entity();
			nEnt.setPosition(node.getNodePoint().copy());
			nEnt.getPosition().y += 0.33f;
			
			nEnt.addComponent(new ModelComponent(vao));
			nEnt.getTransform().setScale(0.5f);
			
			nodeEntities.add(nEnt);
			MainApp.restaurant.engine.addEntity(nEnt);
		}
	}

	public void removeNode(Node curNode) {
		Entity toRemove = null;
		for(Entity entity : nodeEntities) {
			if(entity.getPosition().x == curNode.getNodePoint().x) {
				if(entity.getPosition().z == curNode.getNodePoint().z) {
					toRemove = entity;
				}
			}
		}
		this.nodeEntities.remove(toRemove);
		MainApp.restaurant.engine.getEntities().remove(toRemove);
	}

	public void end() {
		MainApp.restaurant.engine.getEntities().removeAll(nodeEntities);
		this.nodeEntities.clear();
	}
	
}
