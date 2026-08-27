package com.nsachs317.simpleEngine.renderer.shadows;

import com.nsachs317.simpleEngine.renderer.shader.Shader;
import com.nsachs317.simpleEngine.renderer.shader.uniforms.UniformMatrix4f;

public class ShadowMapShader extends Shader {
	
	private static final String vertexShader = "/shaders/shadows/vert.glsl";
	private static final String fragmentShader = "/shaders/shadows/frag.glsl";
	
	protected UniformMatrix4f transform = new UniformMatrix4f("transform");
	protected UniformMatrix4f view = new UniformMatrix4f("view");
	protected UniformMatrix4f projection = new UniformMatrix4f("projection");

	public ShadowMapShader() {
		super(vertexShader, fragmentShader, "in_position");
		super.locateUniforms(transform, view, projection);
	}

	public static String getVertexshader() {
		return vertexShader;
	}

	public static String getFragmentshader() {
		return fragmentShader;
	}

	public UniformMatrix4f getTransform() {
		return transform;
	}

	public UniformMatrix4f getView() {
		return view;
	}

	public UniformMatrix4f getProjection() {
		return projection;
	}
	
	
	
}
