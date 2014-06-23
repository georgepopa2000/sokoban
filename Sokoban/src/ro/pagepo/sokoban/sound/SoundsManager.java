package ro.pagepo.sokoban.sound;

import java.util.ArrayList;
import java.util.Iterator;

import ro.pagepo.sokoban.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundsManager {
	static SoundsManager instance;
	
	Context context;
	
	int[] idSounds = new int[10];
	SoundPool sp;
	
	public static final String ACTION_WALK = "walk";
	public static final String ACTION_PUSH = "push";
	public static final String ACTION_UNDO = "pull";
	public static final String ACTION_REDO = "redo";
	
	public static SoundsManager getInstance(Context context){
		if (instance == null){
			instance= new SoundsManager(context);
		}
		return instance;
	}
	
	public static SoundsManager getInstance(){
		if (instance == null){
			throw new NullPointerException("Instance is null. Call getInstance with a context first.");
		}		
		return instance;
	}
	
	SoundsManager(Context context){
		this.context = context;
		
	
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		
		idSounds[0] = sp.load(context, R.raw.foot1, 1);
		idSounds[1] = sp.load(context, R.raw.push1, 1);
	}
	
	public void playSound(String action){
		AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;	
		int soundId = idSounds[getMediaPlayerByAction(action)];
		sp.play(soundId, volume, volume, 1, 0, 1);
	}
	
	
	private int getMediaPlayerByAction(String action){
		switch (action) {
		case ACTION_WALK:
			return 0;
		case ACTION_PUSH:
			return 1;			

		default:
			break;
		}
		return -1;
	}
	
}
