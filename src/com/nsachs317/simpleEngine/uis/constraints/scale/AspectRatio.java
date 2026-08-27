package com.nsachs317.simpleEngine.uis.constraints.scale;

import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.ConstraintType;
import com.nsachs317.simpleEngine.uis.constraints.UiConstraint;

public class AspectRatio extends UiConstraint {
	
	private float aspect;
	
	public AspectRatio(float aspect) {
		this.aspect = aspect;
	}
	
	@Override
	protected void update(UiComponent component, ConstraintType type) {
		if(type == ConstraintType.HEIGHT) {
			float displayRatio = (float) Window.getAspectRatio();
			component.getScale().setY(component.getScale().x * aspect * displayRatio);
		}
		if(type == ConstraintType.WIDTH) {
			float displayRatio = (float) (Window.getHeight() / Window.getWidth());
			component.getScale().setX(component.getScale().y * aspect * displayRatio);
		}
	}

}
