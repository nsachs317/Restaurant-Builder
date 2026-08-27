package com.seaSaltedToaster.restaurantGame.menus.mainMenu.loadingScreen;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.MenuManager;
import com.seaSaltedToaster.scenes.Game;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.renderer.window.Window;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;
import com.seaSaltedToaster.simpleEngine.utilities.SmoothFloat;

public class LoadingScreen {
	
	//Load state of new scene
	private Engine engine;
	private boolean isLoading = false;
	
	//Cover fade
	private UiComponent cover;
	private SmoothFloat coverAnim;
	
	public LoadingScreen(MenuManager manager, Engine engine) {
		super();
		manager.setLoadingScreen(this);
		this.engine = engine;
		createPanel();
		engine.addUi(cover);
	}

	public void update() {
		if(!this.isLoading) return;
		
		this.coverAnim.update(Window.DeltaTime);
		this.cover.setAlpha(coverAnim.getValue());
		
		if(cover.getAlpha() >= 1.2f) {
			engine.setCurrentScene(new Game(MainApp.resourceManager));
		}
	}
	
	public void slide(boolean in) {
		if(in) {
			this.isLoading = in;
			this.coverAnim.setTarget(1.25f);
		}
	}
	
	private void createPanel() {
		this.cover = new UiComponent(1);
		this.cover.setAlpha(0.0f);
		this.cover.setScale(1.0f, 1.0f);
		this.cover.setColor(1.0f);
		
		this.coverAnim = new SmoothFloat(0.0f);
		this.coverAnim.setAmountPer(0.025f);
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public UiComponent getCover() {
		return cover;
	}

	public void setCover(UiComponent cover) {
		this.cover = cover;
	}

	public SmoothFloat getCoverAnim() {
		return coverAnim;
	}

	public void setCoverAnim(SmoothFloat coverAnim) {
		this.coverAnim = coverAnim;
	}
	
}
