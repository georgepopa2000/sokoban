package ro.pagepo.sokoban.activities;

import java.util.Iterator;
import java.util.List;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.LevelsPack;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StartActivity extends Activity {

	LevelsDataSource datasource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		datasource = new LevelsDataSource(this);	
		datasource.open();
		
		datasource.insertLevelPack(new LevelsPack("original"));
		//datasource.insertLevelPack(new LevelsPack("original + extra"));
		
		List<LevelsPack> llp = datasource.getAllLevelsPack();
		String tot = new String();
		for (Iterator<LevelsPack> iterator = llp.iterator(); iterator.hasNext();) {
			LevelsPack levelsPack = iterator.next();
			tot+=levelsPack.getName()+" ";
			datasource.deleteLevelPack(levelsPack.getId());
		}
		Log.d("xxxxxx",tot+"asd");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_start,
					container, false);	
			Button butStartApp = (Button) rootView.findViewById(R.id.butStartApp);
			butStartApp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PlaceholderFragment.this.startActivity(new Intent(PlaceholderFragment.this.getActivity(), SokobanActivity.class));
				}
			});
			
			Button butss = (Button) rootView.findViewById(R.id.butNewGame);
			butss.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				}
			});
			return rootView;
		}
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}
