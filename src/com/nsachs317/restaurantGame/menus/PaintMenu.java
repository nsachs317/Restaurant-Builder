package com.nsachs317.restaurantGame.menus;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.tools.ColorPalette;
import com.nsachs317.restaurantGame.tools.RayMode;
import com.nsachs317.restaurantGame.tools.Raycaster;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.input.Mouse;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.UiConstraints;
import com.nsachs317.simpleEngine.uis.constraints.XAlign;
import com.nsachs317.simpleEngine.uis.constraints.YAlign;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignX;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignY;
import com.nsachs317.simpleEngine.uis.constraints.scale.AspectRatio;
import com.nsachs317.simpleEngine.uis.constraints.scale.RelativeScale;
import com.nsachs317.simpleEngine.uis.layouts.HorizontalLayout;
import com.nsachs317.simpleEngine.utilities.Vector2f;
import com.nsachs317.simpleEngine.utilities.Vector3f;

public class PaintMenu extends UiComponent {

	//Coloring / Basics
	private Vector3f primary, secondary;
	private Vector2f offset;
	private Engine engine;
	private boolean isOpen = false;
	
	//Actual menu
	private UiComponent tooltip, primaryC, secondaryC;
	
	//Colors
	private float COLOR = ColorPalette.BUTTON_BASE;
	private float LIGHT = ColorPalette.BUTTON_HIGHLIGHT;
	
	public PaintMenu() {
		super(8);
		this.primary = new Vector3f(0);
		this.secondary = new Vector3f(0);
		this.offset = new Vector2f(0.05f, 0.0f);
		this.setActive(false);
		this.engine = MainApp.restaurant.engine;
		createUis();
	}
	
	public void updateSelf() {
		float x = (float) Mouse.normalizeMouseCoordX(Window.getCursorPosX());
		float y = (float) Mouse.normalizeMouseCoordY(Window.getCursorPosY());		
		this.tooltip.setPosition(x + offset.x, y + offset.y);
		
		this.primaryC.setColor(primary);
		this.secondaryC.setColor(secondary);
	}
	
	@Override
	public void onClick() {
		
	}
	
	public void show() {
		if(isOpen) {
			Raycaster.mode = RayMode.DEFAULT;
		} else if(!isOpen) {
			Raycaster.mode = RayMode.PAINT_PICK;
		}
		this.isOpen = !isOpen;
		this.setActive(isOpen);
		this.tooltip.setActive(isOpen);
	}
	
	private void createUis() {
		UiConstraints cons = this.getConstraints();
		cons.setX(new AlignX(XAlign.LEFT, 2.0f));
		cons.setY(new AlignY(YAlign.MIDDLE, 0.0f));
		cons.setWidth(new RelativeScale(1.5f));
		cons.setHeight(new AspectRatio(1.5f));
		createTooltip();
		this.setAlpha(0.0f);
		this.setInteractable(true, engine);
	}
	
	private void createTooltip() {
		float size = 0.825f;
		
		this.tooltip = new UiComponent(4);
		tooltip.setColor(ColorPalette.MAIN_SHADE);
		MainApp.restaurant.engine.addUi(tooltip);
		UiConstraints cons = new UiConstraints();
		cons.setLayout(new HorizontalLayout(-0.3f, 0.05f));
		cons.setHeight(new RelativeScale(0.025f));
		cons.setWidth(new AspectRatio(2.0f));
		tooltip.setConstraints(cons);
		
		UiComponent primaryCB = new UiComponent(4);
		primaryCB.setColor(ColorPalette.MAIN_LIGHT);
		this.tooltip.addComponent(primaryCB);
		UiConstraints cons1 = new UiConstraints();
		cons1.setY(new AlignY(YAlign.MIDDLE, 0.f));
		cons1.setHeight(new RelativeScale(size));
		cons1.setWidth(new AspectRatio(1.f));
		primaryCB.setConstraints(cons1);
		
		this.primaryC = new UiComponent(4);
		primaryCB.addComponent(primaryC);
		UiConstraints cons2 = new UiConstraints();
		cons2.setY(new AlignY(YAlign.MIDDLE, 0.f));
		cons2.setX(new AlignX(XAlign.CENTER));
		cons2.setHeight(new RelativeScale(size));
		cons2.setWidth(new AspectRatio(1.f));
		primaryC.setConstraints(cons2);
		
		UiComponent secondaryCB = new UiComponent(4);
		secondaryCB.setColor(ColorPalette.MAIN_LIGHT);
		this.tooltip.addComponent(secondaryCB);
		UiConstraints cons3 = new UiConstraints();
		cons3.setY(new AlignY(YAlign.MIDDLE, 0.f));
		cons3.setHeight(new RelativeScale(size));
		cons3.setWidth(new AspectRatio(1.f));
		secondaryCB.setConstraints(cons3);
		
		this.secondaryC = new UiComponent(4);
		secondaryCB.addComponent(secondaryC);
		UiConstraints cons4 = new UiConstraints();
		cons4.setY(new AlignY(YAlign.MIDDLE, 0.f));
		cons4.setX(new AlignX(XAlign.CENTER));
		cons4.setHeight(new RelativeScale(size));
		cons4.setWidth(new AspectRatio(1.f));
		secondaryC.setConstraints(cons4);
		
		this.tooltip.setActive(false);
	}

	public Vector3f getPrimary() {
		return primary;
	}

	public void setPrimary(Vector3f primary) {
		this.primary = primary;
	}

	public Vector3f getSecondary() {
		return secondary;
	}

	public void setSecondary(Vector3f secondary) {
		this.secondary = secondary;
	}

	public boolean isOpen() {
		return isOpen;
	}

}
