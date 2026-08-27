package com.seaSaltedToaster;

import org.lwjgl.glfw.GLFW;

import com.seaSaltedToaster.restaurantGame.WorldCamera;
import com.seaSaltedToaster.restaurantGame.audio.TrackPlayer;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingList;
import com.seaSaltedToaster.restaurantGame.objects.Restaurant;
import com.seaSaltedToaster.restaurantGame.tools.Reporter;
import com.seaSaltedToaster.scenes.Menu;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.audio.management.AudioMaster;
import com.seaSaltedToaster.simpleEngine.entity.Entity;
import com.seaSaltedToaster.simpleEngine.renderer.window.Window;
import com.seaSaltedToaster.simpleEngine.utilities.ScreenshotUtils;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class MainApp {
	
	//Game logic
	public static Restaurant restaurant;
	public static TrackPlayer trackPlayer;
	public static ResourceManager resourceManager;
	
	//Menu logic
	public static boolean menuFocused = false;
	public static boolean quitApp = false;
	
	//Scenes in the game
	public static String curSave = "";
	
	public static void main(String[] args) {	
		/*
		 * ENGINE AND RESOURCE BODIES
		 */
		Engine engine = new Engine("Restaurant Game", ClientConfigs.WINDOW_X, ClientConfigs.WINDOW_Y);
		engine.setCamera(new WorldCamera(engine));
		engine.getKeyboard().addKeyListener(new ScreenshotUtils());
		
		resourceManager = new ResourceManager();
		trackPlayer = new TrackPlayer();

		//Set the new scene
		engine.setCurrentScene(new Menu(resourceManager));
		
		//Debug interface
		double lastTime = GLFW.glfwGetTime();
		
		//Update per frame
		while(!engine.getWindow().shouldClose()) {	
			//Prepare new frame
			Reporter.report("Update Restaurant");
			engine.prepareFrame();
			restaurant.update();
			
			//Shadow map pass
			if(restaurant.layers.size() > 0)
				engine.renderShadows(restaurant.layers.get(0).getBuildings());
			
			//Post processing
			engine.startPostProcess();
			
			//Render Normal Pass
			Reporter.report("Render Scene");
			engine.renderScene();
			engine.render();
			
			//Post processing
			Reporter.report("Post Process");
			engine.postProcess();
			
			//Render Uis on top
			Reporter.report("Update Scene");
			engine.updateScene();
			Reporter.report("Render UIs");
			engine.renderUis();
			Reporter.section("Update Engine");
			engine.update();
			
			Reporter.report("Other");
			trackPlayer.update();
			//Check if we closed
			
			if(GLFW.glfwGetTime() > lastTime+5) {
				Reporter.report(); 
				lastTime = GLFW.glfwGetTime();
			}
			Reporter.clear();
			
			if(quitApp)
				break;
		}
		
		AudioMaster.cleanUp();
		
		engine.getCurrentScene().unloadScene(engine);
		
		engine.getWindow().closeWindow();
	}
	
	public static void addExample(Vector3f seatPosition) {
		Entity ent = BuildingList.getBuilding("Ball").getEntity().copyEntity();
		ent.setPosition(seatPosition);
		MainApp.restaurant.engine.addEntity(ent);
	}


}