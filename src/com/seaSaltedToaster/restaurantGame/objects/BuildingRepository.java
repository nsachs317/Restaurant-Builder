package com.seaSaltedToaster.restaurantGame.objects;

import com.seaSaltedToaster.restaurantGame.ai.PathfinderComponent;
import com.seaSaltedToaster.restaurantGame.ai.person.ActionComponent;
import com.seaSaltedToaster.restaurantGame.building.Building;
import com.seaSaltedToaster.restaurantGame.building.BuildingType;
import com.seaSaltedToaster.restaurantGame.building.ObjectLoader;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingCategory;
import com.seaSaltedToaster.restaurantGame.building.categories.BuildingList;
import com.seaSaltedToaster.restaurantGame.objects.food.Food;
import com.seaSaltedToaster.restaurantGame.objects.food.FoodRegistry;
import com.seaSaltedToaster.restaurantGame.objects.people.ChefComponent;
import com.seaSaltedToaster.restaurantGame.objects.people.ChefStation;
import com.seaSaltedToaster.restaurantGame.objects.people.CustomerComponent;
import com.seaSaltedToaster.restaurantGame.objects.people.HostStand;
import com.seaSaltedToaster.restaurantGame.objects.people.ServerComponent;
import com.seaSaltedToaster.restaurantGame.objects.seating.SeatComponent;
import com.seaSaltedToaster.restaurantGame.objects.seating.TableComponent;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.entity.Entity;
import com.seaSaltedToaster.simpleEngine.entity.componentArchitecture.ModelComponent;
import com.seaSaltedToaster.simpleEngine.models.Vao;
import com.seaSaltedToaster.simpleEngine.utilities.Vector2f;
import com.seaSaltedToaster.simpleEngine.utilities.Vector3f;

public class BuildingRepository {
	
	private ObjectLoader loader;
	
	public void registerBuildings(Engine engine) {
		this.loader = new ObjectLoader();
		
		//Categories
		loadCategory("Furniture", "/uis/furniture", engine);
		loadCategory("Garden", "/uis/plant", engine);
		loadCategory("Workstations", "/uis/workstations", engine);
		loadCategory("Employees", "/uis/employees", engine);
		loadCategory("Event", "/uis/spooky", engine);
		loadCategory("Building", "/uis/building", engine);
		
		/*
		 * FINISHED BUILDINGS
		 */
		Building basicWall = this.loader.loadObject("basicWall", engine);
		Building basicWindow = this.loader.loadObject("basicWindow", engine);
		Building basicDoor = this.loader.loadObject("basicDoor", engine);
		basicDoor.setWalkThrough(true);
		basicDoor.getBuildingComponents().add(new DoorComponent());
		
		Building basicDoorframe = this.loader.loadObject("basicDoorframe", engine);
		basicDoorframe.setWalkThrough(true);

		Building brickWall = this.loader.loadObject("brickWall", engine);

		Building trimmedWall = this.loader.loadObject("trimmedWall", engine);		
		Building trimmedWallWindow = this.loader.loadObject("trimmedWallWindow", engine);
		
		/*
		 * FLOORING
		 */
		
		Building tiledFloor = this.loader.loadObject("tiledFloor", engine);
		tiledFloor.getBuildingComponents().add(new FloorComponent("Tiled Floor", "Tiled", null));
		
		Building patio = this.loader.loadObject("patio", engine);
		patio.getBuildingComponents().add(new FloorComponent("Patio", "Flat", null));
		
		Building squareTile = this.loader.loadObject("squareTileFloor", engine);
		squareTile.getBuildingComponents().add(new FloorComponent("Square Tile", "Repeating", null));
		
		Building striped = this.loader.loadObject("stripedFloor", engine);
		striped.getBuildingComponents().add(new FloorComponent("Striped Floor", "Repeating", null));
		
		/*
		 * GARDEN
		 */
		Building pottedPlant = this.loader.loadObject("pottedPlant", engine);
		Building pottedHedge = this.loader.loadObject("pottedHedge", engine);
		Building largeHedge = this.loader.loadObject("largeHedge", engine);
		
		Building tileHedge = this.loader.loadObject("tileHedge", engine);
		tileHedge.getBuildingComponents().add(new TileHitboxComponent());
		
		Building borderedFence = this.loader.loadObject("fences/borderedFence", engine);
		Building vinylFence = this.loader.loadObject("fences/vinylFence", engine);
		
		Building pillar = this.loader.loadObject("pillars/pillar", engine);
//		
		Building brickFence = this.loader.loadObject("fences/brickFence", engine);
		
		Building brickHalfPillar = this.loader.loadObject("pillars/brickHalfPillar", engine);
		Building brickPillar = this.loader.loadObject("pillars/brickPillar", engine);
		
		/*
		 * EVENT / SEASONAL
		 */
		Building pumpkin = this.loader.loadObject("pumpkin", engine);
		Building christmasTree = this.loader.loadObject("furniture/christmasTree", engine);
		Building stocking = this.loader.loadObject("furniture/stocking", engine);
		Building gift = this.loader.loadObject("furniture/gift", engine);
		Building tree = this.loader.loadObject("furniture/tree", engine);
		Building rock = this.loader.loadObject("furniture/rock", engine);
//		Building cactus = this.loader.loadObject("furniture/cactus", engine);

		Building wallObject = this.loader.loadObject("wallObjects/wallObject", engine);
		Building door = this.loader.loadObject("wallObjects/door", engine);
		door.getBuildingComponents().add(new DoorComponent());
		Building window = this.loader.loadObject("wallObjects/window", engine);
		
		/*
		 * FURNITURE
		 */		
		Building table = this.loader.loadObject("furniture/table", engine);
		table.getBuildingComponents().add(new TableComponent());
		
		Building chair = this.loader.loadObject("furniture/chair", engine);
		chair.getBuildingComponents().add(new SeatComponent(new Vector2f(0.0f, 0.0f), 0.25f));
		
		Building booth = this.loader.loadObject("basicBooth", engine);
		booth.getBuildingComponents().add(new TileHitboxComponent());
		booth.getBuildingComponents().add(new SeatComponent(new Vector2f(0.2f, 0.3f), 0.25f));
		booth.getBuildingComponents().add(new SeatComponent(new Vector2f(-0.2f, 0.3f), 0.25f));
		booth.getBuildingComponents().add(new SeatComponent(new Vector2f(0.2f, -0.3f), 0.25f));
		booth.getBuildingComponents().add(new SeatComponent(new Vector2f(-0.2f, -0.3f), 0.25f));
		booth.getBuildingComponents().add(new TableComponent());

		/*
		 * WORKSTATIONS
		 */
		Building chefStation = this.loader.loadObject("chefStation", engine);
		chefStation.getBuildingComponents().add(new ChefStation());
		
		Building waiterStation = this.loader.loadObject("waiterStation", engine);
		waiterStation.getBuildingComponents().add(new HostStand());
		
		/*
		 * Dev Items
		 */
		Building ball = new Building(new Entity());
		ball.getEntity().addComponent(new ModelComponent(engine.getObjLoader().loadObjModel("models/basic/ball")));
		ball.name = "Ball";
		ball.show = false;
		BuildingList.getRoot().addChild(ball);
		
		/*
		 * WORK IN PROGRESS
		 */
		Building waiter1 = loadBuilding("waiter1", BuildingType.Person, engine);
		waiter1.name = "Waiter";
		waiter1.setPrice(300);
		waiter1.setObstructive(false);
		waiter1.setIconZoom(0.5f);
		waiter1.getBuildingComponents().add(new ServerComponent());
		waiter1.getBuildingComponents().add(new PathfinderComponent());
		waiter1.getBuildingComponents().add(new ActionComponent("Waiter", true));
		BuildingList.register(waiter1, "Employees");
		
		Building chef1 = loadBuilding("chef1", BuildingType.Person, engine);
		chef1.name = "Chef";
		chef1.setPrice(500);
		chef1.setObstructive(false);
		chef1.setIconZoom(0.5f);
		chef1.getBuildingComponents().add(new ChefComponent());
		chef1.getBuildingComponents().add(new PathfinderComponent());
		chef1.getBuildingComponents().add(new ActionComponent("Chef", true));
		BuildingList.register(chef1, "Employees");
		
		Building person1 = loadBuilding("person1", BuildingType.Person, engine);
		person1.name = "Customer_TEST";
		person1.setObstructive(false);
		person1.setIconZoom(0.5f);
		person1.getBuildingComponents().add(new PathfinderComponent());
		person1.getBuildingComponents().add(new CustomerComponent());
		person1.getBuildingComponents().add(new ActionComponent("Customer", false));
		person1.setDefPrimary(new Vector3f(0.585, 0.54, 0.3225));
		person1.setDefSecondary(new Vector3f(0.78, 0.72, 0.43));
		person1.show = false;
		BuildingList.register(person1, "Employees");
	}
	
	public void registerFoods(Engine engine) {
		FoodRegistry.create();
		
		Food ribs = loadFood("foods/ribs", 0, engine);
		FoodRegistry.registerFood(ribs, 0);
		
		Food jamToast = loadFood("foods/jamToast", 1, engine);
		FoodRegistry.registerFood(jamToast, 1);
		
		Food chickenLegs = loadFood("foods/chickenLegs", 2, engine);
		FoodRegistry.registerFood(chickenLegs, 2);
		
		Food beefTacos = loadFood("foods/beefTacos", 3, engine);
		FoodRegistry.registerFood(beefTacos, 3);
		
		Food simpleStew = loadFood("foods/simpleStew", 4, engine);
		FoodRegistry.registerFood(simpleStew, 4);
		
		Food fish = loadFood("foods/fish", 5, engine);
		FoodRegistry.registerFood(fish, 5);
		
		Food steak = loadFood("foods/steak", 6, engine);
		FoodRegistry.registerFood(steak, 6);
		
		Food burger = loadFood("foods/burger", 7, engine);
		FoodRegistry.registerFood(burger, 7);
		
		Food dish1 = loadFood("foods/dish1", -1, engine);
		FoodRegistry.registerFood(dish1, -1);
		
		Food check = loadFood("foods/check", -10, engine);
		FoodRegistry.registerFood(check, -10);
	}
	
	private Food loadFood(String name, int id, Engine engine) {
		Vao model = engine.getObjLoader().loadObjModel(name);
		Food food = new Food(model, id);
		food.name = name;
		return food;
	}
	
	private Building loadBuilding(String name, BuildingType type, Engine engine) {
		Vao model = engine.getObjLoader().loadObjModel("models/" + name);
		Entity entity = new Entity();
		entity.addComponent(new ModelComponent(model));
		Building bld = new Building(entity);
		bld.type = type;
		bld.name = name;
		bld.model = model;
		bld.modelPath = "models/"+name;
		return bld;
	}
	
	private void loadCategory(String name, String categoryIcon, Engine engine) {
		BuildingCategory category = new BuildingCategory(name);
		int icon = engine.getTextureLoader().loadTexture(categoryIcon);
		category.setIcon(icon);
		BuildingList.getRoot().addChild(category);
	}
}
