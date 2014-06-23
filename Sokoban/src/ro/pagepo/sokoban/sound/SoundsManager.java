package ro.pagepo.sokoban.sound;

import ro.pagepo.sokoban.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;

public class SoundsManager {
	static SoundsManager instance;
	
	Context context;
	
	int[] idSounds = new int[10];
	SoundPool sp;
	
	public static final String ACTION_WALK = "walk";
	public static final String ACTION_PUSH = "push";
	public static final String ACTION_BLOCK = "block";
	public static final String ACTION_UNDO = "pull";
	public static final String ACTION_REDO = "redo";
	public static final String ACTION_VICTORY = "victory";
	
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
		idSounds[2] = sp.load(context, R.raw.block, 1);
		idSounds[3] = sp.load(context, R.raw.undo, 1);
		idSounds[4] = sp.load(context, R.raw.victory, 1);
	}
	
	public void playSound(String action){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (!prefs.getBoolean("soundsenabled", true)) return;
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;	
		int soundId = idSounds[getIndexSoundByAction(action)];
		sp.play(soundId, volume, volume, 1, 0, 1);
	}
	
	
	private int getIndexSoundByAction(String action){
		switch (action) {
		case ACTION_WALK:
			return 0;
		case ACTION_PUSH:
			return 1;			
		case ACTION_BLOCK:			
			return 2;		
		case ACTION_UNDO:
			return 3;
		case ACTION_VICTORY:
			return 4;			

		default:
			break;
		}
		return -1;
	}
	
}
