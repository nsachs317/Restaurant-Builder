package com.seaSaltedToaster.restaurantGame.menus.management;

import com.seaSaltedToaster.MainApp;
import com.seaSaltedToaster.restaurantGame.objects.Restaurant;
import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.uis.UiComponent;
import com.seaSaltedToaster.simpleEngine.uis.constraints.UiConstraints;
import com.seaSaltedToaster.simpleEngine.uis.constraints.XAlign;
import com.seaSaltedToaster.simpleEngine.uis.constraints.YAlign;
import com.seaSaltedToaster.simpleEngine.uis.constraints.position.AlignX;
import com.seaSaltedToaster.simpleEngine.uis.constraints.position.AlignY;
import com.seaSaltedToaster.simpleEngine.uis.constraints.scale.RelativeScale;
import com.seaSaltedToaster.simpleEngine.uis.text.Text;

public class OpenButton extends UiComponent {

	private Text text;
	
	public OpenButton(Engine engine) {
		super(0);
		this.setInteractable(true, engine);
		
		this.text = new Text("Open", 0);
		UiConstraints cons1 = this.text.getConstraints();
		cons1.setX(new AlignX(XAlign.CENTER, 0.f));
		cons1.setY(new AlignY(YAlign.TOP, 0.5f));
		this.text.setConstraints(cons1);
		this.addComponent(text);
		
		determineColor();
	}
	
	@Override
	public void onClick() {				
		Restaurant rst = MainApp.restaurant;
		rst.isOpen = !rst.isOpen;
		
		determineColor();
	}
	
	private void determineColor() {
		Restaurant rst = MainApp.restaurant;
		if(!rst.isOpen) {
			this.setColor(1, 0, 0);
			this.text.setTextString("Closed");
		}
		else if(rst.isOpen) {
			this.setColor(0, 1, 0);
			this.text.setTextString("Open");
		}
	}

}
