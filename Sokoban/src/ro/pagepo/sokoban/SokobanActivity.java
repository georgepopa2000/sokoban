package ro.pagepo.sokoban;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class SokobanActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sokoban);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sokoban, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			final GameLevel gl = new GameLevel();
			final SokoBoardView sbv = (SokoBoardView) rootView.findViewById(R.id.sokoBoardView1);
		
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
										if (!gl.canMove(action)) Log.d("xxx", "no can do mister"); else 
										{
										gl.move(action);
										sbv.invalidate();
										}
									}
									//return true;
								}
						return false;
					}
				});				
				
				

			return rootView;
		}
	}

}
