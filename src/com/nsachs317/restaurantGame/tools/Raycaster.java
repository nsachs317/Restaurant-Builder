package com.nsachs317.restaurantGame.tools;

import org.lwjgl.glfw.GLFW;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.building.BuildingManager;
import com.nsachs317.restaurantGame.building.BuildingType;
import com.nsachs317.restaurantGame.building.layers.BuildLayer;
import com.nsachs317.restaurantGame.ground.Ground;
import com.nsachs317.restaurantGame.menus.PaintMenu;
import com.nsachs317.restaurantGame.menus.buildingSelector.BuildingViewer;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.input.listeners.MouseEventData;
import com.nsachs317.simpleEngine.input.listeners.MouseListener;
import com.nsachs317.simpleEngine.input.listeners.MousePosData;
import com.nsachs317.simpleEngine.input.listeners.MousePosListener;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public class Raycaster implements MouseListener, MousePosListener {

	//Objects
	public static RayMode mode;
	public static Vector3f lastRay;

	//Raycasting
	private MousePicker picker;
	private Engine engine;
	
	//Building and ground
	public Ground ground;
	public BuildingManager builder;
	
	//Menus used with mouse
	private BuildingViewer viewer;
	public PaintMenu paint;
	
	private float MAX_DIST = 250f;

	public Raycaster(Engine engine) {
		register(engine);
		this.picker = new MousePicker(engine);
		this.viewer = new BuildingViewer(engine);
		engine.addUi(viewer);
		viewer.open(null);
		
		Raycaster.mode = RayMode.DEFAULT;
	}
	
	private void register(Engine engine) {
		this.engine = engine;
		this.engine.getMouse().getMouseButtonCallback().addListener(this);
		this.engine.getMouse().getMousePositionCallback().addListener(this);		
	}

	@Override
	public void notifyButton(MousePosData eventData) {
		//If we are in a menu, do nothing
		if(MainApp.menuFocused) {
			this.ground.selectAt(null);
			this.builder.setSelectedEntity(null);
			return;
		}
		
		//Get layer height
		float groundHeight = BuildLayer.HEIGHT_OFFSET * builder.getCurLayer();
		this.picker.update(groundHeight);
		
		//If we are not on the ground (0), do nothing
		if(groundHeight > 0) {
			this.ground.selectAt(null);
		}
		
		//Get raycast point
		Vector3f ray = picker.getCurrentTerrainPoint();
		Raycaster.lastRay = ray;
		if(ray == null) return;
		
		if(builder.getSelectedEntity() != null) {
			BuildingId id = (BuildingId) builder.getSelectedEntity().getComponent("BuildingId");
			if(id != null) {
				if(id.getType().type != BuildingType.WallObject) {
					
				}
				else {
					float rayLength = ray.length();
					float dist = builder.getSelectedEntity().getPosition().copy().subtract(engine.getCamera().getPosition().copy()).length();
					if(rayLength > dist) {
						float multi = dist / rayLength;
						ray = ray.scale(multi - 0.5f);
					}
				}
			}
		}

		/*
		 * BUILDING UPDATE
		 */
		this.builder.movePreview(ray);
		
		/*
		 * GROUND UPDATE
		 */
		if(builder.getSelectedEntity() != null || builder.isBuilding() || groundHeight != 0)
			this.ground.selectAt(null);
		else
			this.ground.selectAt(ray.round());
	}

	@Override
	public void notifyButton(MouseEventData eventData) {
		if(MainApp.menuFocused) {
			ground.selectAt(null);
			return;
		}
				
		//Key states
		boolean isLeftDown = eventData.getKey() == GLFW.GLFW_MOUSE_BUTTON_LEFT;
		boolean isRightDown = eventData.getKey() == GLFW.GLFW_MOUSE_BUTTON_RIGHT;

		boolean isPlaceDown = eventData.getAction() == GLFW.GLFW_PRESS;
		
		switch(mode) {
			case DEFAULT:
				this.click(eventData);
				break;
			case DELETE:
				if(!isLeftDown || !isPlaceDown || builder.getSelectedEntity() == null) break;
				BuildingId id = (BuildingId) builder.getSelectedEntity().getComponent("BuildingId");
				if(id == null) return;
				
				id.getLayer().remove(builder.getSelectedEntity());
				break;
			case PAINT_PICK:
				if(!isPlaceDown || builder.getSelectedEntity() == null) break;

				if(isLeftDown) {
					BuildingId id2 = (BuildingId) builder.getSelectedEntity().getComponent("BuildingId");
					if(id2 == null) return;
					
					id2.setPrimary(paint.getPrimary());
					id2.setSecondary(paint.getSecondary());
				}
				else if(isRightDown) {
					this.pickColor(builder.getSelectedEntity());
				}
				
				break;
			default:
				this.click(eventData);
				break;
		}
	}
	
	private void pickColor(Entity selectedEntity) {
		if(selectedEntity == null || !selectedEntity.hasComponent("BuildingId")) return;
		
		BuildingId id = (BuildingId) selectedEntity.getComponent("BuildingId");
		paint.setPrimary(id.getPrimary());
		paint.setSecondary(id.getSecondary());
	}
	
	private void click(MouseEventData eventData) {
		//Key states
		boolean leftClicked = (eventData.getAction() == GLFW.GLFW_PRESS && eventData.getKey() == GLFW.GLFW_MOUSE_BUTTON_LEFT && !MainApp.menuFocused);
		
		//Check if we clicked on an object
		boolean selectedExistingObject = (leftClicked && !builder.isBuilding() && builder.getSelectedEntity() != null);
		if(selectedExistingObject) {
			viewer.open(builder.getSelectedEntity());
			return;
		}
		
		//Get raycast
		Vector3f ray = picker.getCurrentTerrainPoint();
		if(ray == null) return;
		
		//Place object on the ground, if you havent selected another
		boolean canPlace = (leftClicked && builder.isBuilding());
		if(canPlace) {
			this.builder.place(ray);
			return;
		}
	}
		
}