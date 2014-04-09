package ro.pagepo.sokoban;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
			SimpleGestureFilter sgf = new SimpleGestureFilter(sbv, new SimpleGestureFilter.SimpleGestureListener() {
				
				@Override
				public void onSwipe(int direction) {
					Toast.makeText(getActivity(), "swipe "+direction, Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onDoubleTap() {
				}
			});
			
				sbv.setGameLevel(gl);
				
				GestureListener.GestureCallbackInterface callback = new GestureListener.GestureCallbackInterface(){
					@Override
					public void onSwipe(int swipeMode) {
						gl.move(swipeMode);
						sbv.invalidate();
					}
				}; 
				
				final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureListener(callback));
				 
		        sbv.setOnTouchListener(new OnTouchListener() {
		            public boolean onTouch(View v, MotionEvent event) {
		 
		                if (gestureDetector.onTouchEvent(event)) {
		                    return true;
		                }
		                return false;
		            }
		        });				
				
				

			return rootView;
		}
	}

}
