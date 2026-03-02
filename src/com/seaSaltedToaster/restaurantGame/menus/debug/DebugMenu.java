package com.seaSaltedToaster.restaurantGame.menus.debug;

import org.lwjgl.glfw.GLFW;

import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.input.listeners.KeyEventData;
import com.seaSaltedToaster.simpleEngine.input.listeners.KeyListener;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;

public class DebugMenu implements KeyListener {
	
	public UiComponent menu;
	
	public PathViewer viewer;
	
	public DebugMenu(Engine engine) {
		engine.getKeyboard().addKeyListener(this);
		
		this.menu = new UiComponent(9);
		this.menu.setScale(1, 1);
		this.menu.setAlpha(0.25f);
		this.menu.setActive(false);
		engine.addUi(menu);
		
		this.viewer = new PathViewer();
		this.menu.addComponent(viewer);
	}

	@Override
	public void notifyButton(KeyEventData eventData) {
		if(eventData.getKey() == GLFW.GLFW_KEY_P && eventData.getAction() == GLFW.GLFW_PRESS) {
			this.menu.setActive(!menu.isActive());
			viewer.updateInfo();
		}
	}

}
