package com.nsachs317.restaurantGame.building.objects;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.building.BuildingManager;
import com.nsachs317.restaurantGame.building.layers.BuildLayer;
import com.nsachs317.restaurantGame.tools.Reporter;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.entity.Transform;
import com.nsachs317.simpleEngine.entity.componentArchitecture.ModelComponent;
import com.nsachs317.simpleEngine.input.Mouse;
import com.nsachs317.simpleEngine.renderer.Fbo;
import com.nsachs317.simpleEngine.renderer.Renderer;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.utilities.Matrix4f;
import com.nsachs317.simpleEngine.utilities.OpenGL;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public class SelectionRenderer extends Renderer {

	//Framebuffer
	private Fbo fbo;
	private ByteBuffer pix;
	private int[] data;
	private int selectedId = -127;
	
	//Manager
	private BuildingManager manager;
	private Matrix4f transform;
	
	public UiComponent test;
	
	public SelectionRenderer(BuildingManager manager, Engine engine) {
		super(new SelectionShader(), engine);
		this.manager = manager;
		this.transform = new Matrix4f();
		
		this.fbo = new Fbo(Window.getWidth(), Window.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		this.pix = BufferUtils.createByteBuffer(16);
		this.data = new int[16];
		
//		this.test = new UiComponent(0);
//		this.test.setScale(1f, -1f);
//		this.test.setTexture(fbo.getColourTexture());
//		//this.engine.addUi(test);
	}

	@Override
	public void prepare() {
		prepareFbo();
		this.fbo.unbindFrameBuffer();
		this.fbo.bindFrameBuffer();
		super.prepareFrame(true);
	}

	private void prepareFbo() {
		if(fbo.getWidth() != Window.width || fbo.getHeight() != Window.getHeight()) {
			this.fbo = new Fbo(Window.getWidth(), Window.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		}
		fbo.bindFrameBuffer();
		OpenGL.setDepthTest(true);
		OpenGL.clearColor();
		OpenGL.clearDepth();
		OpenGL.clearColor(new Vector3f(1,0,0), 1);	
		fbo.unbindFrameBuffer();		
	}

	@Override
	public void render(Object obj) {
		for(BuildLayer layer : manager.getLayers()) {
			if(!layer.isOn()) continue;
			for(Entity entity : layer.getBuildings()) {
				if(entity == null || !entity.hasComponent("BuildingId")) continue;
				BuildingId buildingId = (BuildingId) entity.getComponent("BuildingId");
				Vector3f color = getIdColor(buildingId.getId());
				renderToFBO(entity, color);
			}
		}
	}
	
	private void renderToFBO(Entity entity, Vector3f color) {
		GL11.glViewport(0, 0, (int) Window.getWidth(), (int) Window.getHeight());
		
		//Matrices
		Transform transform = entity.getTransform();
		super.loadMatrices(transform);
		shader.loadUniform(color, "objColor");
		
		//Transformation
		this.transform = utils.createTransformationMatrix(this.transform, transform.getPosition(), transform.getRotation().x, transform.getRotation().y, transform.getRotation().z, transform.getScale());
		shader.loadUniform(this.transform, "transformationMatrix");
		
		//Render
		ModelComponent comp = (ModelComponent) entity.getComponent("Model");
		super.renderVao(comp.getMesh());
		
		
	}

	@Override
	public void endRender() {
		super.endRendering();
		this.fbo.unbindFrameBuffer();
		
		//Get selected objects
		int[] bytes = readPixelColour(); //Takes up 12% of frame time! TODO fix
		int id = (int) (decodeIdFromColor(bytes));
		this.selectedId = id;
		Entity selected = BuildingManager.getBuildingWithID(id);
		if(selected != null && !MainApp.menuFocused) {
			manager.setSelectedEntity(selected); 
		}
		else
			manager.setSelectedEntity(null);		
		this.fbo.unbindFrameBuffer();
	}
	
	private int[] readPixelColour() {
		this.fbo.bindToRead();
	    int mouseX = (int)Mouse.getMouseX();
	    int mouseY = (int)-Mouse.getMouseY() + (int)Window.getHeight();
	    GL11.glReadPixels(mouseX, mouseY, 1, 1, GL40.GL_RGBA, GL30.GL_UNSIGNED_BYTE, this.pix);
	    this.fbo.unbindFrameBuffer();
	    for(int i = 0; i < 3; i++) {
	    	if(pix.get(i) < 0) {
	    		data[i] = (int)(257.0f - Math.abs(pix.get(i)));
	    	}
	    	else {
	    		data[i] = pix.get(i);
	    	}
	    }
	    return data;
	}
	
	  private static float F1 = 256.0f;
	  
	  private static float F2 = F1 * F1;
	  
	  private Vector3f getIdColor(float index) {
			int id = (int) index;
			float r = 0, g = 0, b = 0;
			
			for(int i = 0; id > 0; i++) {
				r++;
				if(r == F1) {
					r = 0;
					g++;
					
					if(g == F1) {
						g = 0;
						b++;
					}
					
				}
				id--;
			}		    
			return new Vector3f(r/F1,g/F1,b/F1);
	  }
		  		  
	  private static float decodeIdFromColor(int[] bytes) {
		    float id = (bytes[0]);
		    id += (bytes[1]) * F1;
		    id += (bytes[2]) * F2;
		    return id;
		  }

	public Fbo getFbo() {
		return fbo;
	}

	public int getSelectedId() {
		return selectedId;
	}

}
