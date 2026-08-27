package com.nsachs317.restaurantGame.building.grid;

import com.nsachs317.simpleEngine.renderer.shader.Shader;
import com.nsachs317.simpleEngine.renderer.shader.uniforms.UniformMatrix4f;

public class GridShader extends Shader {

	private static final String vertexShader = "/shaders/grid/vert.glsl";
	private static final String fragmentShader = "/shaders/grid/frag.glsl";
		
	protected UniformMatrix4f transformation = new UniformMatrix4f("transformationMatrix");
	protected UniformMatrix4f viewMatrix = new UniformMatrix4f("viewMatrix");
	protected UniformMatrix4f projectionMatrix = new UniformMatrix4f("projectionMatrix");

	public GridShader() {
		super(vertexShader, fragmentShader, "position");
		super.locateUniform(transformation);
		super.locateUniform(viewMatrix);
		super.locateUniform(projectionMatrix);
	}
		
	public UniformMatrix4f getTransformation() {
		return transformation;
	}
		
	public UniformMatrix4f getViewMatrix() {
		return viewMatrix;
	}

	public UniformMatrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}
