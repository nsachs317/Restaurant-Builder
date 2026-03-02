package com.seaSaltedToaster.restaurantGame.building.grid;

import org.lwjgl.opengl.GL11;

import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.entity.Transform;
import com.seaSaltedToaster.simpleEngine.models.Vao;
import com.seaSaltedToaster.simpleEngine.models.VaoLoader;
import com.seaSaltedToaster.simpleEngine.utilities.Matrix4f;
import com.seaSaltedToaster.simpleEngine.utilities.MatrixUtils;
import com.seaSaltedToaster.simpleEngine.utilities.OpenGL;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class PlacementGrid {
	
	private GridShader shader;
	
	private Vao gridMesh;
	private Transform transform;
	
	private Matrix4f transformationMatrix;
	
	private boolean showGrid = false;
	
	public PlacementGrid(Engine engine, float worldSize) {
		this.shader = new GridShader();
		
		float[] gridVertices = {
			-1,0,1,
			-1,0,-1,
			1,0,-1,
				
			1,0,-1,
			1,0,1,
			-1,0,1,
				
//				-1,0,1
//				-1,0,-1,
//				1,0,-1,
//				1,0,1,
		};
		
		int[] gridIndices = {
			0, 1, 3,	
			3, 1, 2
		};
		
		this.gridMesh = engine.getLoader().loadToVAO(gridVertices, 3);
//		this.gridMesh = this.getQuadMesh(engine.getLoader());
		
		this.transform = new Transform();
		this.transform.setPosition(new Vector3f(0,0.05f,0));
		this.transform.setScale(worldSize);
		
		this.transformationMatrix = MatrixUtils.createTransformationMatrix(transform);
	}
	
	public Vao getQuadMesh(VaoLoader loader) {
		float[] positions = 
		{-1,1, -1, -1, 1, 1, 1, -1};
		Vao vao = loader.loadToVAO(positions, 2);
		return vao;
	}
	
	public void renderGrid(Engine engine) {
		if(!showGrid) return;
		
		shader.useProgram();
		shader.loadUniform(transformationMatrix, "transformationMatrix");
		shader.loadUniform(engine.getViewMatrix(), "viewMatrix");
		shader.loadUniform(engine.getProjectionMatrix(), "projectionMatrix");
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		gridMesh.bind(0,1,2,3,4,5);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		gridMesh.unbind(0,1,2,3,4,5);
		
		GL11.glDisable(GL11.GL_BLEND);
		shader.stopProgram();
	}

	public boolean isShowGrid() {
		return showGrid;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

}
