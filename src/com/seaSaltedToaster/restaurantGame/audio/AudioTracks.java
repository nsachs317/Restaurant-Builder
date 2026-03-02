package com.seaSaltedToaster.restaurantGame.audio;

import com.seaSaltedToaster.simpleEngine.Engine;
import com.seaSaltedToaster.simpleEngine.audio.management.AudioMaster;

public class AudioTracks {
	
	public static int BUTTON_HOVER;
	public static int BUTTON_CLICK;

	public static void loadSounds(Engine engine) {
		AudioTracks.BUTTON_HOVER = AudioMaster.loadSound("sounds/click");
		AudioTracks.BUTTON_CLICK = AudioMaster.loadSound("sounds/select");
	}
	
}
