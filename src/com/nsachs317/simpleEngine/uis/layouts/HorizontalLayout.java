package com.nsachs317.simpleEngine.uis.layouts;

import java.util.List;

import com.nsachs317.simpleEngine.uis.UiComponent;

public class HorizontalLayout extends UiLayout {
	
	private float edgeSpace, componentSpace;
	
	public HorizontalLayout(float edgeSpace, float componentSpace) {
		this.edgeSpace = edgeSpace;
		this.componentSpace = componentSpace;
	}

	@Override
	public void update(UiComponent component, List<UiComponent> children) {
		float spacing = (component.getPosition().x - component.getScale().x) + (edgeSpace * component.getScale().x);
		
		for(UiComponent child : children) {
			if(hasExistingConstraints(child)) continue;
			
			spacing += child.getScale().x * 2;
			child.getPosition().setX(spacing);
			spacing += (componentSpace * component.getScale().x);
		}
	}

	private boolean hasExistingConstraints(UiComponent child) {
		return (child.getConstraints().getXConstraint() != null || !child.getConstraints().applyLayout); 
	}

	public float getEdgeSpace() {
		return edgeSpace;
	}

	public void setEdgeSpace(float edgeSpace) {
		this.edgeSpace = edgeSpace;
	}

	public float getComponentSpace() {
		return componentSpace;
	}

	public void setComponentSpace(float componentSpace) {
		this.componentSpace = componentSpace;
	}
	
}
