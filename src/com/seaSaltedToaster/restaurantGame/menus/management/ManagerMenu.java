package com.seaSaltedToaster.restaurantGame.menus.management;

import org.lwjgl.glfw.GLFW;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.tools.ColorPalette;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.input.listeners.KeyEventData;
import com.seaSaltedToaster.simpleEngine.input.listeners.KeyListener;
import com.seaSaltedToaster.simpleEngine.input.listeners.ScrollListener;
import com.seaSaltedToaster.simpleEngine.renderer.window.Window;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;
import com.seaSaltedToaster.simpleEngine.uis.constraints.UiConstraints;
import com.seaSaltedToaster.simpleEngine.uis.constraints.XAlign;
import com.seaSaltedToaster.simpleEngine.uis.constraints.YAlign;
import com.seaSaltedToaster.simpleEngine.uis.constraints.position.AlignX;
import com.seaSaltedToaster.simpleEngine.uis.constraints.position.AlignY;
import com.seaSaltedToaster.simpleEngine.uis.constraints.scale.AspectRatio;
import com.seaSaltedToaster.simpleEngine.uis.constraints.scale.RelativeScale;
import com.seaSaltedToaster.simpleEngine.uis.layouts.VerticalLayout;
import com.seaSaltedToaster.simpleEngine.uis.text.Text;
import com.seaSaltedToaster.simpleEngine.uis.textBox.TextBox;
import com.seaSaltedToaster.simpleEngine.utilities.SmoothFloat;

public class ManagerMenu extends UiComponent implements ScrollListener {
	
	//Animation
	private boolean isOpen = false;
	private final float hideFactor = -2.0f;
	private SmoothFloat yValue;
	
	//Title
	private Text title;
	private UiComponent titleBacker;
	private TextBox titleTextBox;
	
	//Comps (Open / Close)
	private Text openText;
	private OpenButton openButton;
	
	//Open / Close Section
	private UiComponent openBacker;
		
	//Settings
	private SmoothFloat smoothScroll;
	private float scroll = -0.25f;
	
	public ManagerMenu(int level) {
		super(0);
		this.yValue = new SmoothFloat(hideFactor);
		this.smoothScroll = new SmoothFloat(scroll);
		
		Engine engine = MainApp.restaurant.engine;
		this.setInteractable(true, engine);
		engine.getMouse().addScrollListener(this);
//		engine.getKeyboard().addKeyListener(this);
		
		this.createPanel(engine);
		this.createOpenCloseSection();
	}
	
	private void createOpenCloseSection() {
		this.openBacker = new UiComponent(0);
		this.openBacker.setColor(0.35f);
		this.addComponent(openBacker);
		UiConstraints cons2 = this.openBacker.getConstraints();
		cons2.setWidth(new RelativeScale(0.95f));
		cons2.setHeight(new RelativeScale(0.125f));
		cons2.setX(new AlignX(XAlign.CENTER, 0.0f));
		this.openBacker.setConstraints(cons2);
		
		this.openButton = new OpenButton(MainApp.restaurant.engine);
		this.openBacker.addComponent(openButton);
		UiConstraints cons1 = this.openButton.getConstraints();
		cons1.setWidth(new RelativeScale(0.15f));
		cons1.setHeight(new AspectRatio(0.5f));
		cons1.setX(new AlignX(XAlign.RIGHT, 0.05f));
		cons1.setY(new AlignY(YAlign.MIDDLE, 0.f));
		this.openButton.setConstraints(cons1);
		
		this.openText = new Text("Open / Close Restaurant", 0.75f, 1);
		this.openBacker.addComponent(openText);
		UiConstraints cons3 = this.openText.getConstraints();
		cons3.setX(new AlignX(XAlign.LEFT, 0.05f));
		cons3.setY(new AlignY(YAlign.MIDDLE, 0.f));
		this.openText.setConstraints(cons3);
	}

	private void createPanel(Engine engine) {
		UiConstraints cons = this.getConstraints();
		cons.setWidth(new RelativeScale(0.5f));
		cons.setHeight(new RelativeScale(0.66f));
		cons.setLayout(new VerticalLayout(0.025f, 0.05f));
		this.setColor(ColorPalette.MAIN_LIGHT);
		
		
		UiConstraints titleBackCons = new UiConstraints();
		titleBackCons.setWidth(new RelativeScale(1.0f));
		titleBackCons.setHeight(new RelativeScale(0.1f));
		titleBackCons.setX(new AlignX(XAlign.CENTER));
		titleBackCons.setY(new AlignY(YAlign.TOP, 0.0f));
		this.titleBacker = new UiComponent(5);
		this.titleBacker.setConstraints(titleBackCons);
		this.titleBacker.setColor(ColorPalette.MAIN_SHADE);
		this.addComponent(titleBacker);
		
		UiConstraints textCons = new UiConstraints();
		textCons.setX(new AlignX(XAlign.CENTER, 0.f));
		textCons.setY(new AlignY(YAlign.MIDDLE, 0f));
		textCons.setWidth(new RelativeScale(0.35f));
		textCons.setHeight(new RelativeScale(0.75f));
		
		titleTextBox = new TextBox(engine, MainApp.restaurant.name);
		titleTextBox.setConstraints(textCons);
		titleBacker.addComponent(titleTextBox);
	}
	
	@Override
	public void updateSelf() {
		yValue.update(Window.DeltaTime);
		float newY = yValue.getValue();
		this.getPosition().setY(newY);
				
		smoothScroll.update(Window.DeltaTime);
		
		//Change to text box listener TODO
		MainApp.restaurant.name = titleTextBox.getText();
	}
	
	@Override
	public void onHover() {
		MainApp.menuFocused = true;
	}
	
	@Override
	public void stopHover() {
		MainApp.menuFocused = false;
	}
	
	@Override
	public void notifyScrollChanged(float scrollValue) {
		this.smoothScroll.increaseTarget(scrollValue / 10.0f);
		if(smoothScroll.getTarget() >= 0) {
			smoothScroll.setTarget(0.0f);
		}
	}
	
	public void show() {
		if(isOpen) {
			this.yValue.setTarget(hideFactor);
			this.isOpen = false;
			smoothScroll.setTarget(0.0f);
			MainApp.menuFocused = false;
			titleTextBox.onClickOff();
		} else if(!isOpen) {
			this.yValue.setTarget(0.0f);
			this.isOpen = true;
			smoothScroll.setTarget(0.0f);
		}
	}

	public boolean isOpen() {
		return isOpen;
	}
	
}
