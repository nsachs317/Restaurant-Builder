package com.nsachs317.restaurantGame.save;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.WorldCamera;
import com.nsachs317.restaurantGame.ai.person.Action;
import com.nsachs317.restaurantGame.ai.person.ActionComponent;
import com.nsachs317.restaurantGame.ai.person.GoToAction;
import com.nsachs317.restaurantGame.ai.person.WaitAction;
import com.nsachs317.restaurantGame.ai.person.chef.FindChefStation;
import com.nsachs317.restaurantGame.ai.person.chef.FinishCooking;
import com.nsachs317.restaurantGame.ai.person.chef.WaitToCook;
import com.nsachs317.restaurantGame.ai.person.customer.CreateParty;
import com.nsachs317.restaurantGame.ai.person.customer.FindTable;
import com.nsachs317.restaurantGame.ai.person.customer.HoverParty;
import com.nsachs317.restaurantGame.ai.person.customer.IdleStay;
import com.nsachs317.restaurantGame.ai.person.customer.LeaveRestaurant;
import com.nsachs317.restaurantGame.ai.person.customer.LoadSeating;
import com.nsachs317.restaurantGame.ai.person.customer.OrderFood;
import com.nsachs317.restaurantGame.ai.person.customer.SitDownAction;
import com.nsachs317.restaurantGame.ai.person.customer.WaitForOrderCustomer;
import com.nsachs317.restaurantGame.ai.person.waiter.FindHostStation;
import com.nsachs317.restaurantGame.ai.person.waiter.GiveChefOrder;
import com.nsachs317.restaurantGame.ai.person.waiter.GiveTableOrder;
import com.nsachs317.restaurantGame.ai.person.waiter.TakeOrder;
import com.nsachs317.restaurantGame.ai.person.waiter.WaitForOrder;
import com.nsachs317.restaurantGame.building.Building;
import com.nsachs317.restaurantGame.building.BuildingId;
import com.nsachs317.restaurantGame.building.BuildingManager;
import com.nsachs317.restaurantGame.building.BuildingType;
import com.nsachs317.restaurantGame.building.categories.BuildingList;
import com.nsachs317.restaurantGame.building.layers.BuildLayer;
import com.nsachs317.restaurantGame.objects.FloorComponent;
import com.nsachs317.restaurantGame.objects.WallComponent;
import com.nsachs317.restaurantGame.objects.people.Employee;
import com.nsachs317.restaurantGame.objects.seating.TableComponent;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.entity.Entity;
import com.nsachs317.simpleEngine.utilities.Vector3f;
import com.nsachs317.simpleEngine.utilities.skybox.TimeHandler;

public class LoadSystem {
	
	//Location of saves
	private static String saveLocation = System.getProperty("user.home") + "/Desktop";
	private static File file = new File(saveLocation + "/RestaurantGame/saves");
	
	//Ais
	private List<String> actions;
	
	//Save current
	private String curSave;
	
	public LoadSystem(String curSave) {
		this.curSave = curSave;
		this.actions = new ArrayList<String>();
		if(!file.exists())
			LoadSystem.file.mkdirs();
	}
	
	public String getGroundType() {
		File ground = new File(file.getAbsolutePath() + "/" + curSave + "/ground.rf");
		String allData = readFile(ground);
		if(!file.exists() || file.getPath() == "" || allData == "NO_FILE")
			return "Grassy";
		
		return allData.trim();
	}
		
	public static int getSaveIcon(String save, Engine engine) {
		File iconFile = new File(file.getAbsolutePath() + "/" + save + "/icon.png");
		if(iconFile.exists()) {
			File iconFileGo = new File(file.getAbsolutePath() + "/" + save + "/icon");
			return engine.getTextureLoader().loadTexture(iconFileGo.getAbsolutePath(), false);
		}
		return -1;
	}
		
	public void loadInfo() {
		File infoFile = new File(file.getAbsolutePath() + "/" + curSave + "/info.rf");
		if(!infoFile.exists()) return;
		
		//Get data
		String allData = readFile(infoFile);
		String[] data = allData.split(",");
		
		float min = Float.parseFloat(data[0]);
		TimeHandler.minute = min;
		
		float hour = Float.parseFloat(data[1]);
		TimeHandler.hour = hour;

		float day = Float.parseFloat(data[2]);
		TimeHandler.dayNumber = (int) day;

		int money = Integer.parseInt(data[3]);
		MainApp.restaurant.money = money;
		
		String name = data[4];
		MainApp.restaurant.name = name;
		
		boolean open = Boolean.parseBoolean(data[5]);
		MainApp.restaurant.isOpen = open;
	}
	
	public void loadEmployees(BuildingManager manager) {
		File employeeFile = new File(file.getAbsolutePath() + "/" + curSave + "/employees.rf");
		if(!employeeFile.exists()) return;
		
		//Get data
		String allData = readFile(employeeFile);
		if(allData.equalsIgnoreCase("")) {
			return;
		}
		String[] allLines = allData.replaceAll("\n", "").split(";");
		
		for(String line : allLines) {
			
			String[] lineDissected = line.split(",");
			int id = Integer.parseInt(lineDissected[0]);
			String name = lineDissected[1];
			float progress = Float.parseFloat(lineDissected[2]);
			float level = Float.parseFloat(lineDissected[3]);
			float wage = Float.parseFloat(lineDissected[4]);
			
			Entity entityFound = BuildingManager.getBuildingWithID(id);
			if(entityFound == null) continue;			
			System.out.println(entityFound.getComponents());
			Employee employee = (Employee) entityFound.getComponent("Chef");
			if(employee == null) employee = (Employee) entityFound.getComponent("Server");
			if(employee == null) continue;
			
			System.err.println("booty");

			employee.setName(name);
			employee.setProgress(progress);
			employee.setLevel((int)level);
			employee.setWage((int)wage);
		}
	}
	
	public void loadActions() {
		//File
		File bldFile = new File(file.getAbsolutePath() + "/" + curSave + "/actions.rf");
		if(!bldFile.exists()) return;
		
		//Get data
		String allData = readFile(bldFile);
		String[] allLines = allData.split(System.getProperty("line.separator"));
		
		//Load
		for(String line : allLines)
			this.actions.add(line);
	}
	
	public void loadBuildings(BuildingManager manager) {
		File bldFile = new File(file.getAbsolutePath() + "/" + curSave + "/buildings.rf");
		if(!bldFile.exists()) return;
		
		//Get data
		String allData = readFile(bldFile);
		String[] allLines = allData.split(System.getProperty("line.separator"));
		
		//Go through all lines\
		for(String line : allLines)	{	
			if(line.startsWith("entities") || line.startsWith("}")) continue;
			String[] parts = line.split(";");
			
			//Get type and name
			String typeData = parts[0];
			String[] typeAll = typeData.split(",");
			
			String name = typeAll[0];
			if(typeAll.length <= 1)
				continue;
			String category = typeAll[1];
			
			String type = typeAll[2];
			
			int layer = Integer.parseInt(typeAll[3]);
			int id = Integer.parseInt(typeAll[4].replace("ID:", "").trim());

			Building bld = BuildingList.getBuilding(name);
			BuildingType typeEnum = BuildingType.valueOf(type);
			
			Entity entity = null;
			
			switch(typeEnum) {
			case Wall:
				/*
				 * LOAD WALL OBJECTS
				 */
				entity = loadWall(bld, manager, parts, layer, id);
				break;
			case Floor :
				/*
				 * FLOOR
				 */
				entity = loadFloor(bld, manager, parts, layer, id);
				break;
			case Edge :
				entity = loadObject(bld, manager, parts, layer, id);
				break;
			default:
				/*
				 * NORMAL OBJECTS LOADING
				 */
				entity = loadObject(bld, manager, parts, layer, id);
				break;
			}
		}
		
		for(BuildLayer layer : manager.getLayers()) {
			layer.updateLayer();
			for(Entity entity : layer.getBuildings()) {
				//ID
				BuildingId id = (BuildingId) entity.getComponent("BuildingId");

				//load actions
				findActions(id.getId(), entity);
				
				if(entity.hasComponent("Table")) {
					TableComponent table = (TableComponent) entity.getComponent("Table");
					table.checkExisting();
				}

			}
		}

	}	
	
	private void findActions(int id, Entity entity) {	
		if(!entity.hasComponent("Action")) return;
		
		ActionComponent comp = (ActionComponent) entity.getComponent("Action");
		for(String line : actions) {
			if(line.startsWith(""+id)) {
				comp.getActions().add(processAction(line, entity));
				comp.getActions().remove(null);
				
				System.out.println(comp.getActions().size());

			}
		}
		if(comp != null)
			comp.getActions().remove(null);
	}

	private Action processAction(String line, Entity entity) {
		String[] vals = line.split(",");
		String type = vals[2];
		
		Action action = null;
		
		switch(type.trim()) {
		//Chef Actions
		case "FindChefStation" :
			action = new FindChefStation();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "FinishCooking" : 
			action = new FinishCooking(-32767);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "WaitToCook" : 
			action = new WaitToCook();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		//General Actions
		case "GoTo" :
			action = new GoToAction(null, null, false);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "Wait" :
			action = new WaitAction(0);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		//Customer
		case "CreateParty" :
			action = new CreateParty();
			action.object = entity;
			action.loadAction(vals[3]);
			action = null;
			break;
		case "FindTable" :
			action = new FindTable();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "Idle" :
			action = new IdleStay();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "HoverParty" :
			action = new HoverParty();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "SitDown" :
			action = new SitDownAction(null);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "LoadSeating" :
			action = new LoadSeating(null);
			action.object = entity;
			action.loadAction(vals[3]);
			action = null;
			break;
		case "OrderFood" : 
			action = new OrderFood();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "HostStation" : 
			action = new FindHostStation();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "WaitForOrderCustomer" :
			action = new WaitForOrderCustomer();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "Leave" : 
			action = new LeaveRestaurant(null);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		//Waiter
		case "FindHostStation" :
			action = new FindHostStation();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "GiveChefOrder" : 
			action = new GiveChefOrder(null);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "GiveTableOrder" : 
			action = new GiveTableOrder(null);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "TakeOrder" : 
			action = new TakeOrder(null);
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		case "WaitForOrder" : 
			action = new WaitForOrder();
			action.object = entity;
			action.loadAction(vals[3]);
			break;
		}
		//System.out.println(action + " " + type); //GiveTableOrder
		
		return action;
	}

	private Entity loadFloor(Building bld, BuildingManager manager, String[] parts, int layer, int index) {			
		//Floor parts
		String section = parts[2];
		String[] floorParts = section.split(">");
		
		int count = Integer.parseInt(floorParts[0]);
		int ix = 1;
		
		List<Vector3f> points = new ArrayList<Vector3f>();
		for(int i = 0; i < count; i++) {
			String vector = floorParts[ix];
			String[] vectorParts = vector.split(",");
			float vx = Float.parseFloat(vectorParts[0]);
			float vy = Float.parseFloat(vectorParts[1]);
			float vz = Float.parseFloat(vectorParts[2]);
			points.add(new Vector3f(vx,vy,vz));
			ix++;
		}
		
		String type = floorParts[ix++];
		String generator = floorParts[ix++];
		
		FloorComponent comp = new FloorComponent(type, generator, points);
		Entity entity = manager.getFloorBuilder().getMeshBuilder().buildFloor(points, comp, true, false);
		
		//Get position
		String transData = parts[1];
		String[] posParts = transData.split(",");
		float x = Float.parseFloat(posParts[0]);
		float y = Float.parseFloat(posParts[1]);
		float z = Float.parseFloat(posParts[2]);
		entity.getTransform().getPosition().set(x, y, z);
		
		float dx = Float.parseFloat(posParts[3]);
		entity.getTransform().getRotation().x = dx;
		float dy = Float.parseFloat(posParts[4]);
		entity.getTransform().getRotation().y = dy;
		float dz = Float.parseFloat(posParts[5]);
		entity.getTransform().getRotation().z = dz;
		
		float sx = Float.parseFloat(posParts[6]);
		float sy = Float.parseFloat(posParts[7]);
		float sz = Float.parseFloat(posParts[8]);
		entity.getTransform().setScale(new Vector3f(sx, sy, sz));
		
		//Get colors
		String prim = parts[3];
		String[] primParts = prim.split(",");
		float pr = Float.parseFloat(primParts[0]);
		float pg = Float.parseFloat(primParts[1]);
		float pb = Float.parseFloat(primParts[2]);
		Vector3f primary = new Vector3f(pr, pg, pb);
		
		String second = parts[4];
		String[] secondParts = second.split(",");
		float sr = Float.parseFloat(secondParts[0]);
		float sg = Float.parseFloat(secondParts[1]);
		float sb = Float.parseFloat(secondParts[2]);
		Vector3f secondary = new Vector3f(sr, sg, sb);
		
		//Add		
		BuildingId id = (BuildingId) entity.getComponent("BuildingId");
		id.setPrimary(primary);
		id.setSecondary(secondary);	
		return entity;
	}

	private Entity loadWall(Building bld, BuildingManager manager, String[] parts, int layer, int index) {
		//Get position
		String transData = parts[1];
		String[] posParts = transData.split(",");
		float x = Float.parseFloat(posParts[0]);
		float y = Float.parseFloat(posParts[1]);
		float z = Float.parseFloat(posParts[2]);
		Vector3f start = new Vector3f(x, y, z);
		
		float dx = Float.parseFloat(posParts[3]);
		float dy = Float.parseFloat(posParts[4]);
		float dz = Float.parseFloat(posParts[5]);
		Vector3f end = new Vector3f(dx, dy, dz);
		
		//Get colors
		String prim = parts[3];
		String[] primParts = prim.split(",");
		float pr = Float.parseFloat(primParts[0]);
		float pg = Float.parseFloat(primParts[1]);
		float pb = Float.parseFloat(primParts[2]);
		Vector3f primary = new Vector3f(pr, pg, pb);
		
		String second = parts[4];
		String[] secondParts = second.split(",");
		float sr = Float.parseFloat(secondParts[0]);
		float sg = Float.parseFloat(secondParts[1]);
		float sb = Float.parseFloat(secondParts[2]);
		Vector3f secondary = new Vector3f(sr, sg, sb);
		
		String types = parts[2];
		String[] typesParts = types.split(":");
		String type = typesParts[0];
		String generator = typesParts[1];
		
		WallComponent comp = new WallComponent(type, generator);
		comp.setStart(start);
		comp.setEnd(end);
		Entity entity = manager.getWallBuilder().getBuilder().createMesh(start, end, comp).get(0);

		//Add
		BuildingId id = (BuildingId) entity.getComponent("BuildingId");
		id.setPrimary(primary);
		id.setSecondary(secondary);
		return entity;
	}

	private Entity loadObject(Building bld, BuildingManager manager, String[] parts, int layer, int index) {
		Entity entity = bld.getEntity().copyEntity();
		
		//Get position
		String transData = parts[1];
		String[] posParts = transData.split(",");
		float x = Float.parseFloat(posParts[0]);
		float y = Float.parseFloat(posParts[1]);
		float z = Float.parseFloat(posParts[2]);
		entity.getTransform().getPosition().set(x, y, z);
		
		float dx = Float.parseFloat(posParts[3]);
		entity.getTransform().getRotation().x = dx;
		float dy = Float.parseFloat(posParts[4]);
		entity.getTransform().getRotation().y = dy;
		float dz = Float.parseFloat(posParts[5]);
		entity.getTransform().getRotation().z = dz;
		
		float sx = Float.parseFloat(posParts[6]);
		float sy = Float.parseFloat(posParts[7]);
		float sz = Float.parseFloat(posParts[8]);
		entity.getTransform().setScale(new Vector3f(sx, sy, sz));
		
		//Get colors
		String prim = parts[2];
		String[] primParts = prim.split(",");
		float pr = Float.parseFloat(primParts[0]);
		float pg = Float.parseFloat(primParts[1]);
		float pb = Float.parseFloat(primParts[2]);
		Vector3f primary = new Vector3f(pr, pg, pb);
		
		String second = parts[3];
		String[] secondParts = second.split(",");
		float sr = Float.parseFloat(secondParts[0]);
		float sg = Float.parseFloat(secondParts[1]);
		float sb = Float.parseFloat(secondParts[2]);
		Vector3f secondary = new Vector3f(sr, sg, sb);
		
		//Add
		manager.getLayers().get(layer).addBuilding(entity, bld, index, false);
		BuildingId id = (BuildingId) entity.getComponent("BuildingId");
		id.setPrimary(primary);
		id.setSecondary(secondary);	
		ActionComponent a = (ActionComponent) entity.getComponent("Action");
		if(a != null) {
			a.getActions().clear();
		}
		return entity;
	}

	public void loadCamera(WorldCamera camera) {
		//Camera file
		File camFile = new File(file.getAbsolutePath() + "/" + curSave + "/camera.rf");
		if(!camFile.exists()) return;
		
		//Get data
		String cameraData = readFile(camFile);
		String[] pieces = cameraData.split(";");
		
		//Get position
		String[] pos = pieces[0].split(",");
		float x = Float.parseFloat(pos[0]);
		float y = Float.parseFloat(pos[1]);
		float z = Float.parseFloat(pos[2]);
		camera.getPosition().set(x, y, z);
		
		//Get rotation
		String[] rotation = pieces[1].split(",");
		float yaw = Float.parseFloat(rotation[0]);
		camera.setYaw(yaw);
		float pitch = Float.parseFloat(rotation[1]);
		camera.setPitch(pitch);
		float roll = Float.parseFloat(rotation[2]);
		camera.setRoll(roll);
		
		//Get other
		String[] other = pieces[2].split(",");
		float camDist = Float.parseFloat(other[0]);
		camera.setCamDist(camDist);
		float outerAngle = Float.parseFloat(other[1]);
		camera.setOuterAngle(outerAngle);
	}
	
	public String readFile(File path)
	{
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(path.toPath());
		} catch (IOException e) {
			return "NO_FILE";
		}
		return new String(encoded, StandardCharsets.US_ASCII);
	}

	public boolean hasData() {
		File bldFile = new File(file.getAbsolutePath() + "/" + curSave + "/buildings.rf");
		return !bldFile.exists();
	}

}
