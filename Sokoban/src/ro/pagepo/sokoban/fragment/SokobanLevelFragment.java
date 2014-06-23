package ro.pagepo.sokoban.fragment;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.activities.SokobanActivity;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.fragment.view.SokoBoardView;
import ro.pagepo.sokoban.levels.GameLevel;
import ro.pagepo.sokoban.levels.LevelsManager;
import ro.pagepo.sokoban.map.state.BoardState;
import ro.pagepo.sokoban.sound.SoundsManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
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
			float origx, origy;
			float dif = Math.max(50, sbv.getBrickSize());

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
					if (action != 0) {
						if (gl.isLevelFinished())
							return true;
						if (gl.canMove(action)) {
							
							if (gl.isNextMovePush(action)){
								SoundsManager.getInstance().playSound(SoundsManager.ACTION_PUSH);
							} else SoundsManager.getInstance().playSound(SoundsManager.ACTION_WALK);
							gl.move(action);
							sbv.invalidate();
							if (gl.isLevelFinished()) {
								levelFinished();
							}
							getActivity().invalidateOptionsMenu();
							updateInfo();
						}
					}
					// return true;
				}
				return false;
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateInfo();		
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
		switch (item.getItemId()) {
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
			if (gl.getStateManager().canUndo()) {
				gl.getStateManager().undo();
				sbv.invalidate();
				getActivity().invalidateOptionsMenu();
				updateInfo();
			}
			return true;
		case R.id.action_redo:// action redo
			if (gl.getStateManager().canRedo()) {
				gl.getStateManager().redo();
				sbv.invalidate();
				getActivity().invalidateOptionsMenu();
				updateInfo();
			}
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

	public void levelFinished() {
		Log.d("currentlvl", lvl.getName() + " " + lvl.getId());
		LevelsManager.getInstance().levelFinished(lvl);
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
		if (nextLevel != null) {
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

	public void startNewLevel(Level level) {
		//Log.d("lfinished", level.getName() + "level finished " + level.getId());
		gl = new GameLevel(level);
		SokoBoardView sbv = (SokoBoardView) getView().findViewById(
				R.id.sokoBoardView1);
		sbv.setGameLevel(gl);
		// sbv.invalidate();
		this.lvl = level;
		getActivity().invalidateOptionsMenu();
		updateInfo();
	}

	public void onBackPressed() {
		if (gl.getStateManager().canUndo()) {
			gl.getStateManager().undo();
			sbv.invalidate();
			getActivity().invalidateOptionsMenu();
			updateInfo();
		}
	}

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
