package com.seaSaltedToaster.restaurantGame.menus;

import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.input.Mouse;
import com.seaSaltedToaster.simpleEngine.input.listeners.MouseEventData;
import com.seaSaltedToaster.simpleEngine.input.listeners.MouseListener;
import com.seaSaltedToaster.simpleEngine.renderer.window.Window;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;
import com.seaSaltedToaster.simpleEngine.uis.constraints.XAlign;
import com.seaSaltedToaster.simpleEngine.uis.constraints.UiConstraints;
import com.seaSaltedToaster.simpleEngine.uis.constraints.YAlign;
import com.seaSaltedToaster.simpleEngine.uis.constraints.position.AlignX;
import com.seaSaltedToaster.simpleEngine.uis.constraints.position.AlignY;
import com.seaSaltedToaster.simpleEngine.uis.constraints.scale.AspectRatio;
import com.seaSaltedToaster.simpleEngine.uis.constraints.scale.RelativeScale;
import com.seaSaltedToaster.simpleEngine.uis.text.Text;
import com.seaSaltedToaster.simpleEngine.utilities.SmoothFloat;
import com.seaSaltedToaster.simpleEngine.utilities.skybox.TimeHandler;

public class TimeDisplay extends UiComponent implements MouseListener {

	//Time text
	private Text display;
	
	//Speed buttons
	private UiComponent speedButton;
	private SmoothFloat bSize;
	private boolean clicked = false;
	
	//Memory saver
	public int lastMinute = 0;
	
	public TimeDisplay(Engine engine) {
		super(5);
		createPanel();
		addText();
		addButton(engine);
		engine.getMouse().getMouseButtonCallback().addListener(this);
	}
	
	private void addButton(Engine engine) {
		this.speedButton = new UiComponent(0);
		UiConstraints cons = this.speedButton.getConstraints();
		cons.setX(new AlignX(XAlign.RIGHT, -0.33f));
		cons.setY(new AlignY(YAlign.MIDDLE));
		cons.setHeight(new RelativeScale(0.67f));
		cons.setWidth(new AspectRatio(1.0f));
		this.addComponent(speedButton);
		this.speedButton.setColor(0.15f);
		
		int icon = engine.getTextureLoader().loadTexture("/uis/fastforward");
		this.speedButton.setTexture(icon);
		this.speedButton.setInteractable(true, engine);
		
		this.bSize = new SmoothFloat(1.0f);
	}
	
	@Override
	public void notifyButton(MouseEventData data) {
		if(data.getKey() == Mouse.LEFT_BUTTON && data.getAction() == Mouse.PRESSED && speedButton.isHovering()) {
			this.clicked = !clicked;
		}
	}

	@Override
	public void updateSelf() {
		int minute = (int) TimeHandler.minute;
		if(minute != lastMinute) {
			this.display.setTextString(getTimeString());
		}
		
		this.bSize.update(Window.DeltaTime);
		this.speedButton.setScaleMultiplier(bSize.getValue());
		
		if(speedButton.isHovering()) {
			this.bSize.setTarget(1.15f);
			if(clicked) {
				TimeHandler.timeSpeed = 5; 
				this.speedButton.setColor(0.75f);
			}
			else
				TimeHandler.timeSpeed = 1;
		}
		else if(!clicked) {
			this.bSize.setTarget(1.0f);
			TimeHandler.timeSpeed = 1;
			this.speedButton.setColor(0.15f);
		}
	}
	
	private String getTimeString() {
		String hour = ((int) TimeHandler.hour) + "";
		int minute = (int) Math.floor(TimeHandler.minute);
		String minuteString = (minute + "");
		if(minute < 10) {
			minuteString = ("0" + minute);
		}
		String dayString = "Day " + TimeHandler.dayNumber;
		this.lastMinute = minute;
		return dayString + " - " + hour + ":" + minuteString;
	}

	private void addText() {
		this.display = new Text(getTimeString(), 1.0f, 6);
		this.display.setColor(1.0f);
		UiConstraints cons = display.getConstraints();
		cons.setX(new AlignX(XAlign.CENTER));
		cons.setY(new AlignY(YAlign.TOP));
		this.addComponent(display);
	}

	private void createPanel() {
		UiConstraints cons = this.getConstraints();
		cons.setX(new AlignX(XAlign.CENTER));
		cons.setY(new AlignY(YAlign.TOP));
		cons.setWidth(new RelativeScale(0.1125f));
		cons.setHeight(new AspectRatio(0.2f));
		this.setColor(0.15f);
	}

}
