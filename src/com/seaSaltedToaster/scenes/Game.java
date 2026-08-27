package com.seaSaltedToaster.scenes;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.ResourceManager;
import com.seaSaltedToaster.Scene;
import com.seaSaltedToaster.restaurantGame.WorldCamera;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.ai.person.NodeViewer;
import com.seaSaltedToaster.restaurantGame.ai.person.customer.PartyLeader;
import com.seaSaltedToaster.restaurantGame.ai.person.customer.PartySeating;
import com.seaSaltedToaster.restaurantGame.building.BuildingManager;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingList;
import com.seaSaltedToaster.restaurantGame.building.grid.PlacementGrid;
import com.seaSaltedToaster.restaurantGame.building.layers.BuildLayer;
import com.seaSaltedToaster.restaurantGame.ground.Ground;
import com.seaSaltedToaster.restaurantGame.menus.DeleteTool;
import com.seaSaltedToaster.restaurantGame.menus.GeneralMenu;
import com.seaSaltedToaster.restaurantGame.menus.MoneyCounter;
import com.seaSaltedToaster.restaurantGame.menus.PaintMenu;
import com.seaSaltedToaster.restaurantGame.menus.TimeDisplay;
import com.seaSaltedToaster.restaurantGame.menus.buildingSelector.BuildingMenu;
import com.seaSaltedToaster.restaurantGame.menus.debug.DebugMenu;
import com.seaSaltedToaster.restaurantGame.menus.employees.EmployeeMenu;
import com.seaSaltedToaster.restaurantGame.menus.layers.LayerMenu;
import com.seaSaltedToaster.restaurantGame.menus.mainMenu.loadingScreen.IntroFade;
import com.seaSaltedToaster.restaurantGame.menus.management.ManagerMenu;
import com.seaSaltedToaster.restaurantGame.menus.pauseMenu.PauseMenu;
import com.seaSaltedToaster.restaurantGame.objects.food.ItemOrder;
import com.seaSaltedToaster.restaurantGame.save.LoadSystem;
import com.seaSaltedToaster.restaurantGame.save.SaveSystem;
import com.seaSaltedToaster.restaurantGame.tools.Raycaster;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.entity.Entity;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;

public class Game extends Scene {

	//Objects in the game world
	private Ground ground;
	private Raycaster raycaster;
	private BuildingManager manager;
	
	//Menus in the game world
	private GeneralMenu general;
	private ManagerMenu managerMenu;
	private BuildingMenu building;
	private EmployeeMenu employee;
	private PaintMenu paint;
	private DeleteTool delete;
	private PauseMenu pause;
	
	//Other objects
	private TimeDisplay timeDisplay;
	private MoneyCounter moneyCounter;
	private LayerMenu layerMenu;
	private IntroFade introFade;
	private DebugMenu debug;
	
	//Save system
	private SaveSystem saveSystem;
	private LoadSystem loadSystem;
	
	private PlacementGrid grid;
	
	UiComponent comp;
	
	public Game(ResourceManager manager) {
		super(manager);
	}
	
	@Override
	public void loadScene(Engine engine) {
		/*
		 * GAME RESOURCES
		 */
		String curSave = MainApp.curSave;
		
		//Load all game assets
		resourceManager.doGameLoad(engine);
		engine.setCamera(new WorldCamera(engine));
		
		this.saveSystem = new SaveSystem(curSave);
		
		/*
		 * GAME OBJECTS
		 */
		//Layers
		this.ground = new Ground(25.0f, 0.25f, engine); 
		MainApp.restaurant.ground = ground;
		
		this.raycaster = new Raycaster(engine);
		this.raycaster.ground = ground;
		
		this.manager = new BuildingManager(engine, 25.0f, raycaster, BuildingList.getRoot().getChildBuildings().get(0));
		
		//Ground the world is on
		this.loadSystem = new LoadSystem(curSave);
		this.loadSystem.loadInfo();
		
		this.ground.generateGround(loadSystem, engine);
		
		this.grid = new PlacementGrid(engine, 25.0f);

		//Raycaster for building
		
		//Debug models
		NodeViewer.vao = engine.getObjLoader().loadObjModel("cube");
		
		//Building manager
		this.raycaster.builder = manager;
						
		//Uis
		this.building = new BuildingMenu(manager, engine);
		engine.addUi(building);
		
		this.employee = new EmployeeMenu();
		engine.addUi(employee);
		
		this.managerMenu = new ManagerMenu(0);
		engine.addUi(managerMenu);
		
		this.layerMenu = new LayerMenu(manager, engine);
		engine.addUi(layerMenu);
		
		this.general = new GeneralMenu(engine);
		this.general.setBuilding(building);
		this.general.setEmployee(employee);
		this.general.setManager(managerMenu);
		engine.addUi(general);
		
		this.paint = new PaintMenu();
		this.raycaster.paint = paint;
		this.general.setPaint(paint);
		this.general.getButtons()[3].addComponent(paint);
		
		this.delete = new DeleteTool();
		this.general.setDelete(delete);
		
		this.pause = new PauseMenu(engine, general);
		this.general.setPause(pause);
		engine.addUi(pause);
		
		//Other
		this.timeDisplay = new TimeDisplay(engine);
		engine.addUi(timeDisplay);
		
		this.moneyCounter = new MoneyCounter();
		engine.addUi(moneyCounter);
		
		this.introFade = new IntroFade(engine);
		this.introFade.slide(true);
		
		//Load save data
		this.loadSystem.loadCamera((WorldCamera) engine.getCamera());
		this.loadSystem.loadActions();
		this.loadSystem.loadBuildings(manager);
		this.loadSystem.loadEmployees(manager);
		
		this.saveSystem.loadOrders(MainApp.restaurant);
		this.saveSystem.loadChefOrders(MainApp.restaurant);
		
		this.debug = new DebugMenu(engine);
		
		//engine.addUi(new WelcomePanel(engine));
		
		this.comp = new UiComponent(0);
		this.comp.setScale(0.5f, 0.5f);
		//this.addComponent(comp);
		
		MainApp.menuFocused = false;
	}

	@Override
	public void renderScene(Engine engine) {
		ground.update(engine);
		
		manager.updateFrame();
						
		introFade.update();
		
		grid.setShowGrid(building.isOpen());
		grid.renderGrid(engine);
	}

	@Override
	public void updateScene(Engine engine) {
		manager.renderSelection();
		
		comp.setTexture(engine.getShadowRenderer().getShadowMap());			
	}

	@Override
	public void unloadScene(Engine engine) {
		this.general.closeAll();
		
		//Save normal stuff
		saveSystem.save(engine.getCamera(), timeDisplay, ground, manager);
		
		//Save Ai actions
		saveSystem.openTo("actions");
		for(BuildLayer layer : manager.getLayers()) {
			for(Entity entity : layer.getBuildings()) {
				if(entity == null) continue;
				PartyLeader leader = (PartyLeader) entity.getComponent("PartyLeader");
				if(leader != null)
					leader.save();
				
				PartySeating seating = (PartySeating) entity.getComponent("PartySeating");
				if(seating != null)
					seating.saveSeating();
				
				ActionComponent action = (ActionComponent) entity.getComponent("Action");
				if(action != null)
					action.save(saveSystem);
			}
		}
		saveSystem.closeWriter();
		
		//Save Restaurant Data
		saveSystem.openTo("orders");
		for(ItemOrder order : MainApp.restaurant.orders) 
			saveSystem.saveOrder(order);
		saveSystem.closeWriter();
		
		saveSystem.openTo("chefOrders");
		for(ItemOrder order : MainApp.restaurant.chefOrders) 
			saveSystem.saveChefOrder(order);
		saveSystem.closeWriter();
	}

}
