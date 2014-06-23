package ro.pagepo.sokoban.activities;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.activities.views.LevelsChooserListAdapter;
import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.LevelsPack;
import ro.pagepo.sokoban.levels.ImportLevelsPack;
import ro.pagepo.sokoban.levels.LevelsManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

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

		ImportLevelsPack il = new ImportLevelsPack(this);
		il.importLevelsFromAssets();
		
		
		//on first run sound and vibrate set to on for settings
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = sp.getBoolean("firstrun", true);
		if (isFirstRun){
			Editor editor = sp.edit();
			editor.putBoolean("soundsenabled", true);
			editor.putBoolean("vibrations", true);
			editor.apply();
		}

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
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);			
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
			
			Button butNewHame = (Button) rootView.findViewById(R.id.butNewGame);
			butNewHame.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((StartActivity)(PlaceholderFragment.this.getActivity())).showDialogLevelsPack();
				}
			});
			
			Button butSettings = (Button) rootView.findViewById(R.id.butSettings);
			butSettings.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), SettingsActivity.class);
					startActivity(intent);
				}
			});
			
			return rootView;
		}
	}
	
	protected void showDialogLevelsPack(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true)
		.setTitle("Choose Level Pack")
		.setAdapter(new ArrayAdapter<LevelsPack>(this, android.R.layout.simple_list_item_1, android.R.id.text1, LevelsManager.getInstance(StartActivity.this).getAllLevelsPack()), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LevelsPack lp = LevelsManager.getInstance(StartActivity.this).getAllLevelsPack().get(which);
				StartActivity.this.showDialogLevels(lp);
				Log.d("onclick", lp.getName()+" ");							
			}
			

		})
		.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	protected void showDialogLevels(LevelsPack lp){
		final String lpName = lp.getName();
		ListAdapter la;		
		la= new LevelsChooserListAdapter(this, android.R.layout.simple_list_item_1, LevelsManager.getInstance().getAllLevelsFromPack(lp.getName()));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true)
		.setTitle("Choose Level")
		.setAdapter(la, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//StartActivity.this.showDialogLevels(lp);
				Intent intent = new Intent(StartActivity.this, SokobanActivity.class);
				intent.putExtra(SokobanActivity.PUT_LEVEL, LevelsManager.getInstance().getAllLevelsFromPack(lpName).get(which));
				intent.putExtra(SokobanActivity.PUT_LEVELPACK_NAME, lpName);		
				StartActivity.this.startActivity(intent);
			}
			

		})
		.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		
		builder.create().show();		
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
