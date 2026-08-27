package com.nsachs317.restaurantGame.menus.mainMenu.savesMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nsachs317.MenuManager;
import com.nsachs317.restaurantGame.menus.languages.LanguageManager;
import com.nsachs317.restaurantGame.menus.mainMenu.MenuSettings;
import com.nsachs317.restaurantGame.menus.mainMenu.savesMenu.create.DropdownBox;
import com.nsachs317.restaurantGame.menus.mainMenu.savesMenu.create.TextBox;
import com.nsachs317.restaurantGame.menus.mainMenu.settingsMenu.SettingsMenu;
import com.nsachs317.restaurantGame.save.LoadSystem;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.input.Mouse;
import com.nsachs317.simpleEngine.input.listeners.ScrollListener;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.UiConstraints;
import com.nsachs317.simpleEngine.uis.constraints.XAlign;
import com.nsachs317.simpleEngine.uis.constraints.YAlign;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignX;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignY;
import com.nsachs317.simpleEngine.uis.constraints.scale.AspectRatio;
import com.nsachs317.simpleEngine.uis.constraints.scale.RelativeScale;
import com.nsachs317.simpleEngine.uis.layouts.VerticalLayout;
import com.nsachs317.simpleEngine.uis.text.Fonts;
import com.nsachs317.simpleEngine.uis.text.Text;
import com.nsachs317.simpleEngine.utilities.SmoothFloat;

public class SavesMenu implements ScrollListener {
	
	//Other objects
	private MenuManager manager;
	private Engine engine;
	
	//Body and side panel
	private UiComponent mainBody, topBar, bottomBar;
	private Text title, title2;
	private SavesBack backButton;
	private UiComponent backer;
	
	//Save portion
	private List<SaveButton> buttons;
	private CreateButton createButton;
	
	//Create portion
	private Text saveName, groundTxt, modeTxt;
	private TextBox nameBox;
	private DropdownBox groundType, modeType;
	
	//Animations
	private SmoothFloat allX, titleSa, scroll;
	
	//Textures
	private int separatorTexture;
	
	public SavesMenu(MenuManager manager, Engine engine) {
		this.manager = manager;
		this.manager.setSavesMenu(this);
		this.engine = engine;
		createPanel(engine);
		addBarItems(engine);
		
		//
		this.separatorTexture = engine.getTextureLoader().loadTexture("/uis/separator");
		
		//Per
		addSaves();
		setState(false);
		engine.getMouse().addScrollListener(this);
	}
	
	public void update() {
		this.allX.update(Window.DeltaTime);
		this.mainBody.getPosition().setX(allX.getValue());
		
		this.titleSa.update(Window.DeltaTime);
		if(title.isHovering()) {
			this.titleSa.setTarget(1.25f);
		} else {
			this.titleSa.setTarget(1.0f);
		}
		this.title.setScaleMultiplier(titleSa.getValue());
		
		//Scroll pane
		this.scroll.update(Window.DeltaTime);
		VerticalLayout layout = (VerticalLayout) backer.getConstraints().getLayout();
		layout.setEdgeSpace(scroll.getValue()); 
		
		for(UiComponent child : backer.getChildren()) {
			child.setClippingBounds(0.0f, 0.2f, 1.0f, 0.6f);
		}
	}
	
	@Override
	public void notifyScrollChanged(float scrollValue) {
		this.scroll.increaseTarget(scrollValue * 0.10f);
	}
	
	public void setState(boolean state) {
		slide(state);
	}
		
	public void addSaves() {	
		//Get files
		String saveLocation = System.getProperty("user.home") + "/Desktop";
		File file = new File(saveLocation + "/RestaurantGame/saves");
		
		//Add buttons
		this.buttons = new ArrayList<SaveButton>();
		for(SaveButton button : buttons) {
			this.backButton.removeComponent(button);
			button = null;
		}
		this.buttons.clear();
		for(int i = 0; i < file.listFiles().length; i++) {
			File child = file.listFiles()[i];
			addSave(child);
		}		
		this.scroll.setValue(0.0f);
		
		Collections.sort(buttons, new Comparator<SaveButton>() {
	        public int compare(SaveButton o1, SaveButton o2) {
	            return o1.timeSince - o2.timeSince;
	        }
	    });
		
		for(SaveButton button : buttons) {
			this.backer.addComponent(button);
			
			UiComponent separator = new UiComponent(0);
			separator.setTexture(separatorTexture);
			UiConstraints separatorCons = separator.getConstraints();
			separatorCons.setWidth(new RelativeScale(0.8f));
//			separatorCons.setHeight(new AspectRatio(0.1f));
			separatorCons.setX(new AlignX(XAlign.CENTER));
			separator.setConstraints(separatorCons);
			this.backer.addComponent(separator);
		}
	}
	
	public void addSave(File child) {
		int icon = LoadSystem.getSaveIcon(child.getName(), engine);
		
		SaveButton btn = new SaveButton(child, icon, this);
		btn.setInteractable(true, engine);
		
		this.buttons.add(btn);
	}

	private void addBarItems(Engine engine) {
		this.backButton = new SavesBack(this, engine);
		this.backButton.setInteractable(true, engine);
		this.topBar.addComponent(backButton);
		
		this.createButton = new CreateButton(this, null);
		this.createButton.setInteractable(true, engine);
		this.bottomBar.addComponent(createButton);
		
		this.title = new Text("saves_title", 2.25f, 1, Fonts.FANCY);
		this.title.setInteractable(true, engine);
		UiConstraints titleCons = title.getConstraints();
		titleCons.setWidth(new RelativeScale(0.5f));
		titleCons.setHeight(new RelativeScale(0.75f));
		titleCons.setX(new AlignX(XAlign.CENTER));
		titleCons.setY(new AlignY(YAlign.TOP, -0.125f));
		this.topBar.addComponent(title);
		LanguageManager.addText(title.getTextString(), title);
		
		this.title2 = new Text("saves_create_new", 2.25f, 1, Fonts.FANCY);
		this.title2.setInteractable(true, engine);
		UiConstraints titleCons2 = title2.getConstraints();
		titleCons2.setWidth(new RelativeScale(0.5f));
		titleCons2.setHeight(new RelativeScale(0.75f));
		titleCons2.setX(new AlignX(XAlign.CENTER));
		titleCons2.setY(new AlignY(YAlign.TOP, -0.125f));
		this.topBar.addComponent(title2);
		LanguageManager.addText(title2.getTextString(), title2);
		this.title2.setActive(false);
		
		this.titleSa = new SmoothFloat(1.0f);
		this.titleSa.setValue(1.0f);
		
		//Backer
		this.backer = new UiComponent(1);
		this.backer.setColor(1.0f);
		this.backer.setAlpha(1.0f);
		UiConstraints backerCons = backer.getConstraints();
		backerCons.setWidth(new RelativeScale(1.0f));
		backerCons.setHeight(new RelativeScale(0.85f));
		backerCons.setX(new AlignX(XAlign.CENTER));
		backerCons.setY(new AlignY(YAlign.BOTTOM));
		backerCons.setLayout(new VerticalLayout(0.0f, 0.0f));
		this.mainBody.addComponent(backer);
		
		//Scroll pane
		this.scroll = new SmoothFloat(-0.15f);
		this.scroll.setValue(-0.15f);
	}
	
	public void slide(boolean in) {
		if(in) {
			this.allX.setTarget(0.0f);
		} else {
			this.allX.setTarget(2.5f);
		}
	}

	private void createPanel(Engine engine) {
		this.mainBody = new UiComponent(1);
		this.mainBody.setScale(0.33f, 0.85f);
		this.mainBody.setColor(0.0f);
		//this.mainBody.setAlpha(MenuSettings.TRANSP);
		this.mainBody.setTexture(engine.getTextureLoader().loadTexture("/uis/recipeBookBackground"));
		UiConstraints cons = mainBody.getConstraints();
		cons.setLayout(new VerticalLayout(0.05f, 0.05f));
		engine.addUi(mainBody);
		
		this.topBar = createBar(YAlign.TOP);
		this.mainBody.addComponent(topBar);
		
		this.bottomBar = createBar(YAlign.BOTTOM);
		this.mainBody.addComponent(bottomBar);
		
		float startAt = 2.5f;
		this.allX = new SmoothFloat(startAt);
		this.allX.setValue(startAt);
	}

	private UiComponent createBar(YAlign align) {
		UiComponent bar = new UiComponent(1);
		bar.setAlpha(MenuSettings.S_TRANSP);
		bar.setColor(MenuSettings.S_COLOR);
		UiConstraints sidePanelCons = bar.getConstraints();
		sidePanelCons.setWidth(new RelativeScale(0.966f));
		sidePanelCons.setHeight(new RelativeScale(0.125f));
		sidePanelCons.setX(new AlignX(XAlign.CENTER));
		sidePanelCons.setY(new AlignY(align, 0.033f));
		return bar;
	}

	public DropdownBox getGroundType() {
		return groundType;
	}

	public DropdownBox getModeType() {
		return modeType;
	}

	public TextBox getNameBox() {
		return nameBox;
	}

	public MenuManager getManager() {
		return manager;
	}

	public Engine getEngine() {
		return engine;
	}

	public UiComponent getMainBody() {
		return mainBody;
	}

	public SmoothFloat getAllX() {
		return allX;
	}
	
}
