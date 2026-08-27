package com.nsachs317.simpleEngine.renderer.lighting;

import com.nsachs317.simpleEngine.utilities.Vector3f;

public class Light {
	
	private Vector3f position, color;

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}
	
	
}
