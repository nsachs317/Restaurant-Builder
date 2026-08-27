package com.seaSaltedToaster.simpleEngine.renderer.shadows;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.entity.Entity;
import com.seaSaltedToaster.simpleEngine.entity.Transform;
import com.seaSaltedToaster.simpleEngine.models.Vao;
import com.seaSaltedToaster.simpleEngine.renderer.Fbo;
import com.seaSaltedToaster.simpleEngine.renderer.Renderer;
import com.seaSaltedToaster.simpleEngine.utilities.Matrix4f;
import com.seaSaltedToaster.simpleEngine.utilities.MatrixUtils;
import com.seaSaltedToaster.simpleEngine.utilities.OpenGL;
import com.seaSaltedToaster.simpleEngine.utilities.Vector2f;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class ShadowMapRenderer extends Renderer {
	
	private Vector3f sunPos = new Vector3f(25000.0f);
	private Vector3f sunDir = new Vector3f(-1, -1, -1).normalize();
	
	private Fbo fbo;
	
	private Matrix4f lightView, lightProjection, projectionViewMatrix;
	private ShadowBox shadowBox;
	
	public ShadowMapRenderer(Engine engine) {
		super(new ShadowMapShader(), engine);
		this.fbo = new Fbo(1000, 1000, Fbo.DEPTH_TEXTURE);
		
		this.lightView = new Matrix4f();
		this.lightProjection = new Matrix4f();
		this.projectionViewMatrix = new Matrix4f();
		
		this.shadowBox = new ShadowBox(lightView, engine.getCamera());
	}

	@Override
	public void prepare() {
	    fbo.bindFrameBuffer();
	    shader.useProgram();
	    prepareFrame();
	}
	
	public void updateShadowBox() {
		this.shadowBox.update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(Object obj) {		
		this.updateLightViewMatrix(sunDir, sunPos); //TODO may cause issues
		this.updateOrthoProjectionMatrix(shadowBox.getWidth(), shadowBox.getHeight(), shadowBox.getLength());
		
		Matrix4f.mul(lightProjection, lightView, projectionViewMatrix);
		
		ArrayList<Entity> list = (ArrayList<Entity>) obj;
		for(Entity entity : list) {
			if(entity == null || entity.getModel() == null) continue;
			
			Transform t = entity.getTransform();
			Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(t.getPosition(), t.getRotation().x, t.getRotation().y, t.getRotation().z, t.getScale());
			this.shader.loadUniform(transformationMatrix, "transform");
			this.shader.loadUniform(lightView, "view");
			this.shader.loadUniform(lightProjection, "projection");
			
			Vao vao = entity.getModel();
			vao.render();
		}
	}

	@Override
	public void endRender() {
		this.shader.stopProgram();
		this.fbo.unbindFrameBuffer();
	}
	
	private void updateOrthoProjectionMatrix(float width, float height, float length) {
		lightProjection.setIdentity();
		lightProjection.m00 = 2f / width;
		lightProjection.m11 = 2f / height;
		lightProjection.m22 = -2f / length;
		lightProjection.m33 = 1;
	}
	
	private void updateLightViewMatrix(Vector3f direction, Vector3f center) {	
		center.negate();
		direction.normalize();
				
		lightView.setIdentity();
		float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightView, lightView);
		float yaw = (float)Math.toDegrees(Math.atan2(direction.x, direction.z));
		yaw = direction.z > 0 ? yaw - 180 : yaw;
		Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightView,
				lightView);
		Matrix4f.translate(center, lightView, lightView);
	}

	public Fbo getFbo() {
		return fbo;
	}

	public void setFbo(Fbo fbo) {
		this.fbo = fbo;
	}

	public int getShadowMap() {
		return fbo.getDepthTexture();
	}
	
	public void prepareFrame() {
		OpenGL.setDepthTest(true);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

}
