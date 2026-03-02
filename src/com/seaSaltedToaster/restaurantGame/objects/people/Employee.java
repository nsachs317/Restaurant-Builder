package com.seaSaltedToaster.restaurantGame.objects.people;

import com.seaSaltedToaster.simpleEngine.entity.componentArchitecture.Component;
import com.seaSaltedToaster.simpleEngine.models.Vao;

public abstract class Employee extends Component {

	//Basic
	public String name, status = "n/a";
	public Vao model;
	
	//Level
	private float progress;
	private int level;
	
	//Pay per day
	private int wage = 10;
	
	@Override
	public void init() {
		this.progress = 33.0f;
		this.level = 2;
	}

	@Override
	public void update() {
		increaseXP();
	}
	
	protected void increaseXP() {
		progress += Math.abs(Math.random()) / 100.0f;
		if(progress >= 100) {
			level++;
			progress = progress - 100.0f;
		}		
	}

	public void addExp(float exp) {
		this.progress += exp;
	}
	
	public abstract void fireEmployee();

	public String getName() {
		return name;
	}

	public Vao getModel() {
		return model;
	}

	public int getWage() {
		return wage;
	}

	public void setWage(int wage) {
		this.wage = wage;
	}

	public float getProgress() {
		return progress;
	}

	public int getLevel() {
		return level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModel(Vao model) {
		this.model = model;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public String getComponentType() {
		return "Employee";
	}

	@Override
	public boolean changesRenderer() {
		return false;
	}

	@Override
	public Component copyInstance() {
		return null;
	}

}
