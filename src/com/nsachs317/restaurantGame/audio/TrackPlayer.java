package com.nsachs317.restaurantGame.audio;

import java.util.Random;

import org.lwjgl.openal.AL10;

import com.nsachs317.simpleEngine.audio.AudioSource;
import com.nsachs317.simpleEngine.audio.management.AudioMaster;
import com.nsachs317.simpleEngine.renderer.window.Window;
import com.nsachs317.simpleEngine.utilities.SmoothFloat;

public class TrackPlayer {
	
	//Player of the music
	public AudioSource player;
	
	//Music list
	private String[] tracks;
	private final int MAX_TRACKS = 1;
	
	//Volume
	private float vol = 0.0625f * 2;
	private SmoothFloat volSmooth;
	
	//Track
	private int curTrack = -1;
	private String lastTrack;
	
	public TrackPlayer() {
		this.tracks = new String[MAX_TRACKS];
		addTracks();
		
		this.player = new AudioSource();
		this.player.setGain(0.0f);
		
		this.volSmooth = new SmoothFloat(0.0f);
		this.volSmooth.setTarget(vol);
		this.volSmooth.setValue(0.0f);
		this.volSmooth.setAmountPer(vol / 10.0f);
	}
	
	public void update() {
		boolean isPlaying = AL10.alGetSourcei(curTrack, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
				
		if(!isPlaying) {
//			playNextTrack();
		}
		else {
			this.volSmooth.update(Window.DeltaTime);
			this.player.setGain(volSmooth.getValue());
		}
	}
	
	public void playNextTrack() {
	      AL10.alDeleteBuffers(curTrack);
	      
	      String nextName = getRandomTrack();
	      lastTrack = nextName;
	      
	      int track = AudioMaster.loadSound(nextName);
	      curTrack = track;
	      
	      player.Play(track);
	}

	private String getRandomTrack() {
		Random random = new Random();
		int track = random.nextInt(MAX_TRACKS);
		String newTrack = tracks[track];
//		if(newTrack == lastTrack)
//			return getRandomTrack();
		return newTrack;
	}

	private void addTracks() {
		String ambition = "sounds/bgm";
		this.tracks[0] = ambition;
	}

}
