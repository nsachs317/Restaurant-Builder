package com.nsachs317.scenes;

import com.nsachs317.MainApp;
import com.nsachs317.MenuManager;
import com.nsachs317.ResourceManager;
import com.nsachs317.Scene;
import com.nsachs317.restaurantGame.WorldCamera;
import com.nsachs317.restaurantGame.audio.TrackPlayer;
import com.nsachs317.restaurantGame.ground.Ground;
import com.nsachs317.restaurantGame.menus.mainMenu.MainMenu;
import com.nsachs317.restaurantGame.menus.mainMenu.loadingScreen.LoadingScreen;
import com.nsachs317.restaurantGame.menus.mainMenu.savesMenu.CreateMenu;
import com.nsachs317.restaurantGame.menus.mainMenu.savesMenu.SavesMenu;
import com.nsachs317.restaurantGame.menus.mainMenu.settingsMenu.SettingsMenu;
import com.nsachs317.restaurantGame.objects.Restaurant;
import com.nsachs317.restaurantGame.save.SaveSystem;
import com.nsachs317.simpleEngine.Engine;

public class Menu extends Scene {

	private MenuManager manager;
	
	private MainMenu mainMenu;
	private SettingsMenu settingsMenu;
	private SavesMenu savesMenu;
	private LoadingScreen loadingScreen;
	private CreateMenu createMenu;
	
	private Ground previewGround;
	
	public Menu(ResourceManager manager) {
		super(manager);
	}
	
	@Override
	public void loadScene(Engine engine) {
		/*
		 * STARTUP LOAD AND MENU ASSETS
		 */
		MainApp.restaurant = new Restaurant(engine);
		
		//Will do initial load if it hasnt been done yet
		resourceManager.doInitialLoad(engine);
		
		engine.getCamera().resetPosition();
		
		SaveSystem.createFolder();
		
		TrackPlayer trackPlayer = MainApp.trackPlayer;
		trackPlayer = new TrackPlayer();
//		trackPlayer.playNextTrack();
		
		previewGround = new Ground(15,15,engine);
		previewGround.generateGeneric(engine);
		
		/*
		 * MENUS AND OBJECTS
		 */
		//Next create the main menus
		this.manager = new MenuManager();
		
		this.mainMenu = new MainMenu(manager, engine);
		this.settingsMenu = new SettingsMenu(manager, engine);
		this.savesMenu = new SavesMenu(manager, engine);
		this.loadingScreen = new LoadingScreen(manager, engine);
		this.createMenu = new CreateMenu(manager, engine);

		//No matter what, the camera will not move in the menu
		MainApp.menuFocused = true;
	}

	@Override
	public void renderScene(Engine engine) {
		previewGround.getRenderer().setHighlight(-1);
		previewGround.update(engine);
	}

	@Override
	public void updateScene(Engine engine) {
		mainMenu.update();
		settingsMenu.update();
		savesMenu.update();
		loadingScreen.update();
		createMenu.update();
	}

	@Override
	public void unloadScene(Engine engine) {
		//Close reason (exit game or go to new scene)
		mainMenu.getBackground().setActive(false);
		settingsMenu.getMainBody().setActive(false);
		savesMenu.getMainBody().setActive(false);
		loadingScreen.getCover().setActive(false);
	}

}
