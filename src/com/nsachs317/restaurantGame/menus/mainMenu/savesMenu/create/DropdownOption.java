package com.nsachs317.restaurantGame.menus.mainMenu.savesMenu.create;

import com.nsachs317.MainApp;
import com.nsachs317.restaurantGame.menus.languages.LanguageManager;
import com.nsachs317.simpleEngine.uis.UiComponent;
import com.nsachs317.simpleEngine.uis.constraints.UiConstraints;
import com.nsachs317.simpleEngine.uis.constraints.XAlign;
import com.nsachs317.simpleEngine.uis.constraints.YAlign;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignX;
import com.nsachs317.simpleEngine.uis.constraints.position.AlignY;
import com.nsachs317.simpleEngine.uis.text.Text;

public class DropdownOption extends UiComponent {

	private DropdownBox parent;
	private Text display;
	
	public DropdownOption(String text, float stage, DropdownBox parent) {
		super(3);
		this.parent = parent;
		this.setScale(0.1f, 0.05f);
		this.setAlpha(0.0f);
		UiConstraints cons = this.getConstraints();
		cons.setX(new AlignX(XAlign.CENTER));
		cons.setY(new AlignY(YAlign.TOP, stage));
		
		this.display = new Text(text, 0.75f, 3);
		this.display.setColor(0.0f);
		this.addComponent(display);
		
		UiConstraints cons2 = this.display.getConstraints();
		cons2.setX(new AlignX(XAlign.CENTER));
		cons2.setY(new AlignY(YAlign.TOP, 0.4f));
		
		this.setInteractable(true, MainApp.restaurant.engine);
		LanguageManager.addText(text, display);
	}
	
	@Override
	public void onClick() {
		this.parent.setSelected(this);
	}

	public DropdownBox getParent() {
		return parent;
	}

	public Text getDisplay() {
		return display;
	}

}
