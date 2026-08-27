package com.nsachs317.restaurantGame.menus;

import org.lwjgl.glfw.GLFW;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.menus.buildingSelector.BuildingMenu;
import com.nsachs317.restaurantGame.menus.employees.EmployeeMenu;
import com.nsachs317.restaurantGame.menus.management.ManagerMenu;
import com.nsachs317.restaurantGame.menus.pauseMenu.PauseMenu;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.input.listeners.KeyEventData;
import com.nsachs317.simpleEngine.input.listeners.KeyListener;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.XAlign;
import com.nsachs317.simpleEngine.uis.constraints.UiConstraints;
import com.nsachs317.simpleEngine.uis.constraints.YAlign;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignX;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignY;
import com.nsachs317.simpleEngine.uis.constraints.scale.AspectRatio;
import com.nsachs317.simpleEngine.uis.layouts.VerticalLayout;

public class GeneralMenu extends UiComponent implements KeyListener {

	//Other menus
	private ManagerMenu manager;
	private BuildingMenu building;
	private EmployeeMenu employee;
	private PaintMenu paint;
	private DeleteTool delete;
	private PauseMenu pause;
	
	//Objects
	private int buttonCount = 6;
	private int[] buttonIcons;
	private GeneralButton[] buttons;
	
	public GeneralMenu(Engine engine) {
		super(1);
		createBacking();
		loadTextures(engine);
		addButtons(engine);
		this.setInteractable(true, engine);
		engine.getKeyboard().addKeyListener(this);
	}
	
	@Override
	public void updateSelf() {
		delete.update();
	}
	
	@Override
	public void notifyButton(KeyEventData eventData) {
		if(eventData.getAction() != GLFW.GLFW_PRESS) return;
		if(eventData.getKey() == GLFW.GLFW_KEY_ESCAPE) {
			closeOthers(-1);
			
			for(int i = 0; i < buttonCount; i++) {
				buttons[i].setInUse(false);
			}
		}
	}
		
	@Override
	public void onClick() {	
		//closeAll();
//		for(int i = 0; i < buttonCount; i++) {
//			if(buttons[i].isInUse())
//			buttons[i].setInUse(false);
//		}
		
		//General
		if(buttons[0].isHovering()) {
			manager.show();
			buttons[0].setInUse(!(buttons[0].isInUse()));
			closeOthers(0);
		}
		//Building
		else if(buttons[1].isHovering()) {
			building.show();
			buttons[1].setInUse(!buttons[1].isInUse());
			closeOthers(1);
		}
		//Employees
		else if(buttons[2].isHovering()) {
			employee.show();
			buttons[2].setInUse(!buttons[2].isInUse());
			closeOthers(2);
		}
		//Paint
		else if(buttons[3].isHovering()) {
			paint.show();
			buttons[3].setInUse(!buttons[3].isInUse());
			closeOthers(3);
		}
		//Delete
		else if(buttons[4].isHovering()) {
			delete.show(buttons[4]);
			buttons[4].setInUse(!buttons[4].isInUse());
			closeOthers(4);
		}
		//Settings
		else if(buttons[5].isHovering()) {
			doPauseMenu();
		}
	}

	public void doPauseMenu() {
		pause.show();
		buttons[5].setInUse(!buttons[5].isInUse());
		closeOthers(5);
	}

	@Override
	public void onHover() {
		MainApp.menuFocused = true;
	}
	
	@Override
	public void stopHover() {
		MainApp.menuFocused = false;
	}
	
	public void closeOthers(int it) {
		for(int i = 0; i < buttonCount; i++) {
			if(i != it)
				buttons[i].setInUse(false);
		}
		if(manager.isOpen() & it != 0) {
			manager.show();
		}
		if(building.isOpen() & it != 1) {
			building.show();
		}
		if(employee.isOpen() & it != 2) {
			employee.show();
		}
		if(paint.isOpen() & it != 3) {
			paint.show();
		}
		if(delete.isDeleting() & it != 4) {
			delete.show(buttons[4]);
		}
		if(pause.isOpen() & it != 5) {
			pause.show();
		}
	}
	
	public void closeAll() {
		if(building.isOpen()) {
			building.show();
		}
		if(employee.isOpen()) {
			employee.show();
		}
		if(paint.isOpen()) {
			paint.show();
		}
		if(delete.isDeleting()) {
			delete.show(buttons[4]);
		}
		if(pause.isOpen()) {
			pause.show();
		}
	}

	private void addButtons(Engine engine) {
		this.buttons = new GeneralButton[buttonCount];
		for(int i = 0; i < buttonCount; i++) {
			//Button
			GeneralButton button = new GeneralButton(buttonIcons[i], engine);
			this.addComponent(button);
			buttons[i] = button;
		}
	}
	
	private void loadTextures(Engine engine) {
		this.buttonIcons = new int[buttonCount];
		this.buttonIcons[0] = engine.getTextureLoader().loadTexture("/uis/general");
		this.buttonIcons[1] = engine.getTextureLoader().loadTexture("/uis/building");
		this.buttonIcons[2] = engine.getTextureLoader().loadTexture("/uis/employees");
		this.buttonIcons[3] = engine.getTextureLoader().loadTexture("/uis/paint");
		this.buttonIcons[4] = engine.getTextureLoader().loadTexture("/uis/delete");
		this.buttonIcons[5] = engine.getTextureLoader().loadTexture("/uis/settings");
	}

	private void createBacking() {
		UiConstraints cons = new UiConstraints();
		cons.setX(new AlignX(XAlign.LEFT, 0.00625f));
		cons.setY(new AlignY(YAlign.TOP, 0.5f));
		cons.setWidth(new AspectRatio(0.20f));
		cons.setLayout(new VerticalLayout(-0.066f, 0.025f));
		this.setConstraints(cons);
		this.setScale(0.066f, 0.4f);
		this.setColor(0.15f);
		this.setAlpha(0.0f);
	}

	public UiComponent[] getButtons() {
		return buttons;
	}

	public ManagerMenu getManager() {
		return manager;
	}

	public void setManager(ManagerMenu manager) {
		this.manager = manager;
	}

	public BuildingMenu getBuilding() {
		return building;
	}

	public void setBuilding(BuildingMenu building) {
		this.building = building;
	}

	public EmployeeMenu getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeMenu employee) {
		this.employee = employee;
	}

	public PaintMenu getPaint() {
		return paint;
	}

	public void setPaint(PaintMenu paint) {
		this.paint = paint;
	}

	public DeleteTool getDelete() {
		return delete;
	}

	public void setDelete(DeleteTool delete) {
		this.delete = delete;
	}

	public PauseMenu getPause() {
		return pause;
	}

	public void setPause(PauseMenu pause) {
		this.pause = pause;
	}

}
