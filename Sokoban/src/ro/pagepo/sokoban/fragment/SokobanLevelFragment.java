package ro.pagepo.sokoban.fragment;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.activities.SokobanActivity;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.fragment.view.SokoBoardView;
import ro.pagepo.sokoban.levels.GameLevel;
import ro.pagepo.sokoban.levels.LevelsManager;
import ro.pagepo.sokoban.map.state.BoardState;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SokobanLevelFragment extends Fragment {

	GameLevel gl;
	SokoBoardView sbv;
	Level lvl;
	String packName;
	public SokobanLevelFragment() {
		
	}
	
	public static SokobanLevelFragment newInstance(Level lvl,String packName) {
		SokobanLevelFragment fragment = new SokobanLevelFragment();
		Bundle args = new Bundle();
		args.putSerializable(SokobanActivity.PUT_LEVEL,lvl);
		args.putString(SokobanActivity.PUT_LEVELPACK_NAME, packName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (getArguments() != null) {
			lvl = (Level) getArguments().getSerializable(SokobanActivity.PUT_LEVEL);
			packName = getArguments().getString(SokobanActivity.PUT_LEVELPACK_NAME);
		}
		gl = new GameLevel(lvl);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		
		sbv = (SokoBoardView) rootView.findViewById(R.id.sokoBoardView1);
	
			sbv.setGameLevel(gl);
			
	        sbv.setOnTouchListener(new View.OnTouchListener() {
				float origx,origy;
				float dif = Math.max(50,sbv.getBrickSize());
				
				/*
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction()==MotionEvent.ACTION_DOWN){
						origx = event.getAxisValue(MotionEvent.AXIS_X);
						origy = event.getAxisValue(MotionEvent.AXIS_Y);
						return true;
					} else 
						if (event.getAction() == MotionEvent.ACTION_UP){
						} else 
							if (event.getAction() == MotionEvent.ACTION_MOVE){	
								int action = 0;
								float offsetx = origx-event.getAxisValue(MotionEvent.AXIS_X);
								if (Math.abs(offsetx) > dif){
									if (offsetx > 0) action = BoardState.MOVE_LEFT;
										else action = BoardState.MOVE_RIGHT;
									origx = event.getAxisValue(MotionEvent.AXIS_X);
								}
								float offsety = origy-event.getAxisValue(MotionEvent.AXIS_Y);
								if (Math.abs(offsety) > dif){
									if (offsety > 0) action = BoardState.MOVE_TOP;
										else action = BoardState.MOVE_BOTTOM;
									origy = event.getAxisValue(MotionEvent.AXIS_Y);
								}							
								if (action != 0 ){
									if (gl.isLevelFinished()) return true;
									if (gl.canMove(action))  
									{
									gl.move(action);
									sbv.invalidate();									
									if (gl.isLevelFinished()){
										showFinishedDialog();
									}
									getActivity().invalidateOptionsMenu();
									}
								}
								//return true;
							}
					return false;
				}
				//*/
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction()==MotionEvent.ACTION_DOWN){
						origx = event.getAxisValue(MotionEvent.AXIS_X);
						origy = event.getAxisValue(MotionEvent.AXIS_Y);
						return true;
					} else 
						if (event.getAction() == MotionEvent.ACTION_UP){
						} else 
							if (event.getAction() == MotionEvent.ACTION_MOVE){	
								int action = 0;
								float offsetx = origx-event.getAxisValue(MotionEvent.AXIS_X);
								if (Math.abs(offsetx) > dif){
									if (offsetx > 0) action = BoardState.MOVE_TOP;
										else action = BoardState.MOVE_BOTTOM;
									origx = event.getAxisValue(MotionEvent.AXIS_X);
								}
								float offsety = origy-event.getAxisValue(MotionEvent.AXIS_Y);
								if (Math.abs(offsety) > dif){
									if (offsety > 0) action = BoardState.MOVE_LEFT;
										else action = BoardState.MOVE_RIGHT;
									origy = event.getAxisValue(MotionEvent.AXIS_Y);
								}							
								if (action != 0 ){
									if (gl.isLevelFinished()) return true;
									if (gl.canMove(action))  
									{
									gl.move(action);
									sbv.invalidate();									
									if (gl.isLevelFinished()){
										levelFinished();
									}
									getActivity().invalidateOptionsMenu();
									}
								}
								//return true;
							}
					return false;
				}				
			});				
			
			

		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.level_sokoban, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem mi = menu.findItem(R.id.action_redo);
		mi.setEnabled(gl.getStateManager().canRedo());
		mi = menu.findItem(R.id.action_back);
		mi.setEnabled(gl.getStateManager().canUndo());		
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
    /*/ 
		if (id == android.R.id.home) {
        // app icon in action bar clicked; go home
        Intent intentHome = new Intent(this.getActivity(), StartActivity.class);
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
        return true;		
    } //*/
		if (id == R.id.action_exit) {
			this.getActivity().finish();
			return true;
		}
		if (id == R.id.action_back) {
			if (gl.getStateManager().canUndo()){
				gl.getStateManager().undo();
				sbv.invalidate();
				getActivity().invalidateOptionsMenu();
			}
			return true;
		}		
		if (id == R.id.action_redo) {
			if (gl.getStateManager().canRedo()){
				gl.getStateManager().redo();
				sbv.invalidate();
				getActivity().invalidateOptionsMenu();
			}
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public void levelFinished(){	
		Log.d("currentlvl",lvl.getName() + " "+lvl.getId());
		LevelsManager.getInstance().levelFinished(lvl);
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setMessage("Victory! You solved it! Oauu! I can't believe it!")
		.setTitle("Level Solved")
		.setPositiveButton("Go to menu", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SokobanLevelFragment.this.getActivity().finish();
				
			}
		});
		final Level nextLevel = LevelsManager.getInstance().getNextLevel(lvl);		
		if (nextLevel != null){
		builder.setNeutralButton("Next level", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SokobanLevelFragment.this.startNewLevel(nextLevel);				
			}
		});
		}
		
	
		AlertDialog ad = builder.create();
		ad.show();
		//builder.create().show();
	}
	
	public void startNewLevel(Level level){
		Log.d("lfinished",level.getName() + "level finished "+level.getId());
		gl = new GameLevel(level);
		SokoBoardView sbv = (SokoBoardView) getView().findViewById(R.id.sokoBoardView1);
		sbv.setGameLevel(gl);
		//sbv.invalidate();		
		this.lvl = level;
		getActivity().invalidateOptionsMenu();
	}
	
}
