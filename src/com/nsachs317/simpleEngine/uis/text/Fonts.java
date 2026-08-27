package com.nsachs317.simpleEngine.uis.text;

import com.nsachs317.simpleEngine.Engine;

public class Fonts {

	public static FontType ARIAL, FANCY;
	
	public static void loadFonts(Engine engine) {
		ARIAL = new FontType(engine.getTextureLoader().loadTexture("/uis/berlin", -0.0f), "/uis/berlin");
		FANCY = new FontType(engine.getTextureLoader().loadTexture("/uis/fancy", -0.0f), "/uis/fancy");
	}
	
}
