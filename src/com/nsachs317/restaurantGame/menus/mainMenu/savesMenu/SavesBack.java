package com.nsachs317.restaurantGame.menus.mainMenu.savesMenu;

import com.nsachs317.MenuManager;
import com.nsachs317.restaurantGame.audio.AudioTracks;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.audio.AudioSource;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.UiConstraints;
import com.nsachs317.simpleEngine.uis.constraints.XAlign;
import com.nsachs317.simpleEngine.uis.constraints.YAlign;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignX;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignY;
import com.nsachs317.simpleEngine.uis.constraints.scale.AspectRatio;
import com.nsachs317.simpleEngine.uis.constraints.scale.RelativeScale;
import com.nsachs317.simpleEngine.utilities.SmoothFloat;

public class SavesBack extends UiComponent {

	//Objects
	private SavesMenu savesMenu;
	private AudioSource source;
	
	//Animation
	private SmoothFloat sizeAnim;
	
	public SavesBack(SavesMenu savesMenu, Engine engine) {
		super(1);
		this.savesMenu = savesMenu;
		createButton(engine);
		this.source = new AudioSource();
	}
	
	@Override
	public void updateSelf() {
		this.sizeAnim.update(Window.DeltaTime);
		
		float val = this.sizeAnim.getValue();
		this.setScaleMultiplier(val);
	}
	
	@Override
	public void onClick() {
		MenuManager manager = this.savesMenu.getManager();
		manager.getMainMenu().slide(true);
		savesMenu.slide(false);
		source.Play(AudioTracks.BUTTON_CLICK);
	}
	
	@Override
	public void onHover() {
		this.sizeAnim.setTarget(1.33f);
		source.Play(AudioTracks.BUTTON_HOVER);
	}
	
	@Override
	public void stopHover() {
		this.sizeAnim.setTarget(1.0f);
	}

	private void createButton(Engine engine) {
		int icon = engine.getTextureLoader().loadTexture("/uis/settings/back");
		this.setTexture(icon);
		
		UiConstraints cons = this.getConstraints();
		cons.setWidth(new AspectRatio(1.0f));
		cons.setHeight(new RelativeScale(0.85f));
		cons.setX(new AlignX(XAlign.LEFT, 0.05f));
		cons.setY(new AlignY(YAlign.TOP, 0.33f));
		
		this.sizeAnim = new SmoothFloat(1.0f);
		this.sizeAnim.setValue(1.0f);
	}

}
