package ro.pagepo.sokoban.fragment;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.fragment.view.SokoBoardView;
import ro.pagepo.sokoban.levels.GameLevel;
import ro.pagepo.sokoban.map.state.BoardState;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
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
	public SokobanLevelFragment() {
		gl = new GameLevel();
	}
	
	public static SokobanLevelFragment newInstance() {
		SokobanLevelFragment fragment = new SokobanLevelFragment();
		Bundle args = new Bundle();
		//args.putString(ARG_PARAM1, param1);
		//args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			//mParam1 = getArguments().getString(ARG_PARAM1);
			//mParam2 = getArguments().getString(ARG_PARAM2);
		}
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_exit) {
			this.getActivity().finish();
			return true;
		}
		if (id == R.id.action_back) {
			if (gl.getStateManager().canUndo()){
				gl.getStateManager().undo();
				sbv.invalidate();
			}
			return true;
		}		
		if (id == R.id.action_redo) {
			if (gl.getStateManager().canRedo()){
				gl.getStateManager().redo();
				sbv.invalidate();
			}
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public void showFinishedDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setMessage("Victory! You solved it! Oauu! I can't believe it!")
		.setTitle("Level Solved")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		})
		.setNeutralButton("Next level", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
	
		AlertDialog ad = builder.create();
		ad.show();
		//builder.create().show();
	}
	
}
