package ro.pagepo.sokoban.fragment;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.activities.SettingsActivity;
import ro.pagepo.sokoban.activities.SokobanActivity;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.fragment.view.SokoBoardView;
import ro.pagepo.sokoban.levels.GameLevel;
import ro.pagepo.sokoban.levels.LevelsManager;
import ro.pagepo.sokoban.map.state.BoardState;
import ro.pagepo.sokoban.sound.SoundsManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ScrollView;
import android.widget.TextView;

public class SokobanLevelFragment extends Fragment {

	GameLevel gl;
	SokoBoardView sbv;
	Level lvl;
	String packName;

	public SokobanLevelFragment() {
				
	}

	public static SokobanLevelFragment newInstance(Level lvl, String packName) {
		SokobanLevelFragment fragment = new SokobanLevelFragment();
		Bundle args = new Bundle();
		args.putSerializable(SokobanActivity.PUT_LEVEL, lvl);
		args.putString(SokobanActivity.PUT_LEVELPACK_NAME, packName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (getArguments() != null) {
			lvl = (Level) getArguments().getSerializable(
					SokobanActivity.PUT_LEVEL);
			packName = getArguments().getString(
					SokobanActivity.PUT_LEVELPACK_NAME);
		}
		gl = new GameLevel(lvl);
		SoundsManager.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		Chronometer timer = (Chronometer) rootView.findViewById(R.id.chrTimer);
		timer.setBase(SystemClock.elapsedRealtime());
		timer.start();
		
		sbv = (SokoBoardView) rootView.findViewById(R.id.sokoBoardView1);

		sbv.setGameLevel(gl);

		sbv.setOnTouchListener(new View.OnTouchListener() {
			//handle the moves on the sokoview based on the touches
			/**
			 * the last touched pos from where we shpould compute if there's move or not 
			 */
			float origx, origy;
			/**
			 * the distance "swiped" when should move
			 * is the size of a brick but no less than 50
			 */
			float dif = Math.max(50, sbv.getBrickSize());

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//on action down save
					origx = event.getAxisValue(MotionEvent.AXIS_X);
					origy = event.getAxisValue(MotionEvent.AXIS_Y);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					int action = 0;
					float offsetx = origx
							- event.getAxisValue(MotionEvent.AXIS_X);
					if (Math.abs(offsetx) > dif) {
						if (offsetx > 0)
							action = BoardState.MOVE_TOP;
						else
							action = BoardState.MOVE_BOTTOM;
						origx = event.getAxisValue(MotionEvent.AXIS_X);
					}
					float offsety = origy
							- event.getAxisValue(MotionEvent.AXIS_Y);
					if (Math.abs(offsety) > dif) {
						if (offsety > 0)
							action = BoardState.MOVE_LEFT;
						else
							action = BoardState.MOVE_RIGHT;
						origy = event.getAxisValue(MotionEvent.AXIS_Y);
					}
					onMove(action);
					// return true;
				}
				return false;
			}
		});

		return rootView;
	}

	/**
	 * move the sokoban on the current board and based on the action and update the result on the board view
	 * @param action the action to be taken,can be BoardState.MOVE_RIGHT,BoardState.MOVE_LEFT etc.
	 * @return true if the move was made
	 */
	public boolean onMove(int action){
		if (action != 0) {
			if (gl.isLevelFinished())
				return true;
			if (gl.canMove(action)) {		
				//play a sound on move
				if (gl.isNextMovePush(action)){//if is push
					SoundsManager.getInstance().playSound(SoundsManager.ACTION_PUSH);
					//on push vibrate
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					if (prefs.getBoolean("vibrations", true)){//check if vibrate is enabled in settings
						Vibrator v = (Vibrator) this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
						v.vibrate(200);
					}
										
				} else SoundsManager.getInstance().playSound(SoundsManager.ACTION_WALK);
				

				gl.move(action);
				sbv.invalidate();//update the sokoview
				if (gl.isLevelFinished()) {
					levelFinished();
				}
				getActivity().invalidateOptionsMenu();
				updateInfo();
			} else {
				SoundsManager.getInstance().playSound(SoundsManager.ACTION_BLOCK);
			}
		}
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateInfo();		
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		
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
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this.getActivity(), SettingsActivity.class);
			startActivity(intent);			
			return true;	
		case R.id.action_exit: // action exit level to menu
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						SokobanLevelFragment.this.getActivity().finish();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						dialog.dismiss();
						break;
					}
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(
					this.getActivity());
			builder.setMessage(
					"Are you sure you want to exit level? Your progress will be lost.")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
			return true;
		case R.id.action_back: // action undo
			doUndo();
			return true;
		case R.id.action_redo:// action redo
			doRedo();
			return true;
		case R.id.action_restart:// action restart level
			dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						gl = new GameLevel(lvl);
						SokoBoardView sbv = (SokoBoardView) SokobanLevelFragment.this
								.getView().findViewById(R.id.sokoBoardView1);
						sbv.setGameLevel(gl);
						getActivity().invalidateOptionsMenu();
						updateInfo();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						dialog.dismiss();
						break;
					}
				}
			};
			builder = new AlertDialog.Builder(this.getActivity());
			builder.setMessage(
					"Are you sure you want to restart level? Your progress will be lost.")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * redo the state of the board if it's possible and update the results
	 */
	private void doRedo(){
		if (gl.getStateManager().canRedo()) {
			gl.getStateManager().redo();
			sbv.invalidate();
			getActivity().invalidateOptionsMenu();
			updateInfo();
			SoundsManager.getInstance().playSound(SoundsManager.ACTION_UNDO);
		}		
	}
	
	/**
	 * undo the state of the board if it's possible and update the results
	 */
	private void doUndo(){
		if (gl.getStateManager().canUndo()) {
			gl.getStateManager().undo();
			sbv.invalidate();
			getActivity().invalidateOptionsMenu();
			updateInfo();
			SoundsManager.getInstance().playSound(SoundsManager.ACTION_UNDO);
		}
	}
	

	/**
	 * on level finished disoplay dialog for the next level + info about the current finished level
	 * set the level as solved in the LevelsManager
	 */
	public void levelFinished() {
		//set level as solved
		LevelsManager.getInstance().levelFinished(lvl);
		//[;ay a sound for finished
		SoundsManager.getInstance().playSound(SoundsManager.ACTION_VICTORY);
		//diaplsy dialog for the next level or go to menu
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		Chronometer chr = (Chronometer) getView().findViewById(R.id.chrTimer);
		chr.stop();
		builder.setMessage("Moves:  "+ gl.getMovesNumber() + "\nTotal time: " + chr.getText())
				.setTitle("Level completed")
				.setCancelable(false)
				.setPositiveButton("Go to menu",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SokobanLevelFragment.this.getActivity()
										.finish();

							}
						});
		final Level nextLevel = LevelsManager.getInstance().getNextLevel(lvl);
		if (nextLevel != null) {//only add next level button if there is a next level in the pack
			builder.setNeutralButton("Next level",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SokobanLevelFragment.this.startNewLevel(nextLevel);
						}
					});
		}

		AlertDialog ad = builder.create();
		ad.show();
		// builder.create().show();
	}

	/**
	 * starts a new level
	 * @param level - the new level to be started
	 */
	public void startNewLevel(Level level) {
		gl = new GameLevel(level);
		SokoBoardView sbv = (SokoBoardView) getView().findViewById(
				R.id.sokoBoardView1);
		sbv.setGameLevel(gl);
		this.lvl = level;
		getActivity().invalidateOptionsMenu();
		updateInfo();
	}

	/**
	 * on back button undo. this is called from the parrent activity
	 */
	public void onBackPressed() {
		doUndo();
	}

	/**
	 * update the info table with number of moves, current level, current levels pack
	 */
	public void updateInfo() {
		View view = getView();
		TextView txtMoves = (TextView) view.findViewById(R.id.txtMoves);
		TextView txtPack = (TextView) view.findViewById(R.id.txtPack);
		TextView txtLevel = (TextView) view.findViewById(R.id.txtLevel);

		txtMoves.setText("" + gl.getMovesNumber());
		txtPack.setText(packName);
		txtLevel.setText(lvl.getName());
	}

}
