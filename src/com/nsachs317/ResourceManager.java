package com.nsachs317;

import java.util.List;

import com.nsachs317.restaurantGame.audio.AudioTracks;
import com.nsachs317.restaurantGame.building.categories.BuildingList;
import com.nsachs317.restaurantGame.menus.languages.Language;
import com.nsachs317.restaurantGame.menus.languages.LanguageManager;
import com.nsachs317.restaurantGame.menus.languages.LanguageReader;
import com.nsachs317.restaurantGame.objects.BuildingRepository;
import com.nsachs317.simpleEngine.Engine;
import com.nsachs317.simpleEngine.audio.management.AudioMaster;
import com.nsachs317.simpleEngine.uis.text.Fonts;

public class ResourceManager {
	
	/*
	 * RESOURCES USED IN GAME
	 */
	//Languages
	private LanguageReader languageReader;
	private List<Language> languages;
	
	/*
	 * LOAD STATUS
	 */
	//Whether we loaded the intial resources yet
	private boolean initialLoad = false, gameLoad = false;
	
	public void doInitialLoad(Engine engine) {
		//Check if these resources are already loaded
		if(initialLoad) return;
		
		//Load all fonts
		System.out.println("Loading Text Fonts");
		Fonts.loadFonts(engine);
		
		//Load the audio system into play
		System.out.println("Loading Audio System");
		AudioTracks.loadSounds(engine);
		
		//Languages
		System.out.println("Loading Language Files");
		this.languageReader = new LanguageReader("" + "/lang/");
		this.languages = this.languageReader.loadLanguageFiles("example", "english", "deutsch", "espanol");
		LanguageManager.start(this.languages, "english");
		
		//Make sure we say we are done
		System.out.println("Completed Startup Loading");
		this.initialLoad = true;
	}
	
	public void doGameLoad(Engine engine) {
		//Check if these resources are already loaded
//		if(gameLoad) return;
		
		/*
		 * BUILDINGS
		 */
		//Buildings and foods list
		System.out.println("Loading Building Files");
		BuildingList.create();
		
		//Buildings and foods files
		BuildingRepository repository = new BuildingRepository();
		repository.registerFoods(engine);
		repository.registerBuildings(engine);
		
		//Make sure we say we are done
		System.out.println("Completed Game Asset Load");
		this.gameLoad = true;
	}

}
