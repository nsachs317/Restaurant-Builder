package com.seaSaltedToaster.restaurantGame.menus.mainMenu.tutorial;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;

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
