package com.nsachs317.restaurantGame.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nsachs317.restaurantGame.ai.PathfinderComponent;
import com.nsachs317.restaurantGame.building.layers.BuildLayer;
import com.nsachs317.restaurantGame.ground.Ground;
import com.nsachs317.restaurantGame.objects.food.ItemOrder;
import com.nsachs317.restaurantGame.objects.people.ChefComponent;
import com.nsachs317.restaurantGame.objects.people.ServerComponent;
import com.nsachs317.restaurantGame.objects.seating.SeatComponent;
import com.nsachs317.restaurantGame.objects.seating.TableComponent;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.entity.Entity;

public class Restaurant {
	
	//Static variables
	public static int eatTime = 10;
	public static int maxTableSize = 8;
	public static float GET_ORDER_WAIT = 1;

	//Customers
	public List<TableComponent> tables;
	public List<SeatComponent> chair;

	//Servers
	public List<ServerComponent> servers;
	public List<ItemOrder> orders;
	
	//Chefs
	public List<ChefComponent> chefs;
	public List<ItemOrder> chefOrders;
	
	//Layers
	public List<BuildLayer> layers;
	public Ground ground;
	public PathfinderComponent cmp;
	
	//Money
	public int money = 0;
	public String name = "My Restaurant";
	public boolean isOpen = false;
	
	//Locations
	public Engine engine;
	public Random random;
	
	public Restaurant(Engine engine) {
		this.layers = new ArrayList<BuildLayer>();
		
		this.tables = new ArrayList<TableComponent>();
		this.chair = new ArrayList<SeatComponent>();
		
		this.servers = new ArrayList<ServerComponent>();
		this.orders = new ArrayList<ItemOrder>();
		this.chefs = new ArrayList<ChefComponent>();
		this.chefOrders = new ArrayList<ItemOrder>();
		
		this.money = 5000;
		
		this.engine = engine;
		this.random = new Random((int) (Math.random() * 100.0f));
	}

	public void update() {
		for(ChefComponent chef : chefs) {
			chef.update();
		}
		for(ServerComponent server : servers) {
			server.update();
		}
	}
	
	public boolean isBankrupt() {
		return money < 0;
	}
	
	public Entity getChef() {
		return chefs.get(random.nextInt(chefs.size())).getEntity();
	}
	
	public ItemOrder getOrder(int id) {
		for(ItemOrder order : orders) {
			if(order.getId() == id)
				return order;
		}
		return null;
	}
	
	public ItemOrder getChefOrder(int id) {
		for(ItemOrder order : chefOrders) {
			if(order.getId() == id)
				return order;
		}
		return null;
	}

	public boolean atCapacity() {
		for(TableComponent table : tables) {
			if(!table.isTaken()) {
				return false;
			}
		}
		return true;
	}

	public int capacity() {
		for(TableComponent table : tables) {
			if(!table.isTaken() && table.capacity() > 0)
				return table.capacity();
		}
		return 0;
	}

//	public ItemOrder[] getAllTableOrder(ItemOrder order) {
//		ItemOrder[] tOrders = new ItemOrder[maxTableSize];
//		int i = 0;
//		for(ItemOrder rOrder : orders) {
//			if(rOrder.getTableId() == order.getTableId()) {
//				tOrders[i] = rOrder; i++;
//			}
//		}
//		return tOrders;
//	}

}
