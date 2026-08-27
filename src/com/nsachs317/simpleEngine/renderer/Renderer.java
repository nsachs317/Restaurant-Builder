package com.nsachs317.simpleEngine.renderer;

import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.Transform;
import com.nsachs317.simpleEngine.entity.componentArchitecture.Component;
import com.nsachs317.simpleEngine.models.Vao;
import com.nsachs317.simpleEngine.renderer.shader.Shader;
import com.nsachs317.simpleEngine.utilities.Matrix4f;
import com.nsachs317.simpleEngine.utilities.MatrixUtils;
import com.nsachs317.simpleEngine.utilities.OpenGL;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public abstract class Renderer {
	
	//Shading
	protected Shader shader;
	
	//Engine
	protected Engine engine;
	protected MatrixUtils utils;

	public Renderer(Shader shader, Engine engine) {
		this.shader = shader;
		this.engine = engine;
		this.utils = new MatrixUtils();
	}
	
	//Preparing frame
	public abstract void prepare();
	
	protected void prepareFrame(boolean clearDisplay) {
		this.shader.useProgram();
		if(clearDisplay) {
			OpenGL.setDepthTest(true);
			OpenGL.clearColor();
			OpenGL.clearDepth();
			OpenGL.clearColor(new Vector3f(0.0f, 0.25f, 0.25f), 1.0f);
		}
	}
	
	//Render
	public abstract void render(Object obj);
	
	protected void loadComponents(Entity entity) {
		for(Component component : entity.getComponents()) {
			switch(component.getComponentType()) {
			case "Model" :
				loadMatrices(entity.getTransform());
				break;
			default :
				break;
			}
		}
	}
	
	protected void loadMatrices(Transform transform) {
		Matrix4f transformationMatrix = utils.createTransformationMatrix(transform);
		shader.loadUniform(transformationMatrix, "transformationMatrix");
		shader.loadUniform(engine.getViewMatrix(), "viewMatrix");
		shader.loadUniform(engine.getProjectionMatrix(), "projectionMatrix");
	}
	
	protected void renderVao(Vao vao) {
		if(vao == null) return;
		vao.render();
	}
	
	//Finish 
	public abstract void endRender();
	
	protected void endRendering() {
		this.shader.stopProgram();
	}
}
