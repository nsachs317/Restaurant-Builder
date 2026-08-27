package com.nsachs317.restaurantGame.menus.mainMenu.tutorial;

import com.nsachs317.MainApp;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.uis.UiComponent;

public class WelcomePanel extends UiComponent {

	public WelcomePanel(Engine engine) {
		super(9);
		this.setInteractable(true, engine);
		
		this.setScale(0.25f, 0.6f);
	}
	
	@Override
	public void updateSelf() {
		
	}
	
	@Override
	public void whileHover() {
		MainApp.menuFocused = true;
	}
	
	@Override
	public void stopHover() {
		MainApp.menuFocused = false;
	}

}
