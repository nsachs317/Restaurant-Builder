package com.nsachs317.restaurantGame.menus.pauseMenu;

import org.lwjgl.glfw.GLFW;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.WorldCamera;
import com.nsachs317.restaurantGame.menus.GeneralMenu;
import com.nsachs317.scenes.Menu;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.input.listeners.KeyEventData;
import com.nsachs317.simpleEngine.input.listeners.KeyListener;
import com.nsachs317.simpleEngine.uis.UiComponent;

public class PauseMenu extends UiComponent implements KeyListener {

	private boolean isOpen = false;
	
	private GeneralMenu menu;
	
	public PauseMenu(Engine engine, GeneralMenu menu) {
		super(0);
		this.setActive(isOpen);
		this.setInteractable(true, engine);
		this.menu = menu;
		
		buildMenu();
		engine.getKeyboard().addKeyListener(this);
	}
	
	@Override
	public void notifyButton(KeyEventData eventData) {
		if(eventData.getKey() == GLFW.GLFW_KEY_ESCAPE && eventData.getAction() == GLFW.GLFW_PRESS && isOpen) {
			menu.doPauseMenu();
		}
	}
	
	@Override
	public void onClick() {
        MainApp.restaurant.engine.setCurrentScene(new Menu(MainApp.resourceManager));
	}
	
	@Override
	public void updateSelf() {
		if(isOpen)
			MainApp.menuFocused = true;
	}
	
	private void buildMenu() {
		this.setScale(0.2f, 0.5f);
	}

	public void show() {		
		this.isOpen = !isOpen;
		this.setActive(isOpen);
				
		WorldCamera camera = (WorldCamera) MainApp.restaurant.engine.getCamera();
		camera.lock(isOpen);
		
		MainApp.menuFocused = isOpen;
	}

	public boolean isOpen() {
		return isOpen;
	}

}
