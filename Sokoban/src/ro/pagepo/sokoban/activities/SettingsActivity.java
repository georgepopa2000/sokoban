package ro.pagepo.sokoban.activities;

import ro.pagepo.sokoban.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

public class SettingsActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment()).commit();

		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
				    	
				        Intent upIntent = NavUtils.getParentActivityIntent(SettingsActivity.this);
				        if (NavUtils.shouldUpRecreateTask(SettingsActivity.this, upIntent)) {
				            // This activity is NOT part of this app's task, so create a new task
				            // when navigating up, with a synthesized back stack.
				            TaskStackBuilder.create(SettingsActivity.this)
				                    // Add all of this activity's parents to the back stack
				                    .addNextIntentWithParentStack(upIntent)
				                    // Navigate up to the closest parent
				                    .startActivities();
				        } else {
				            // This activity is part of this app's task, so simply
				            // navigate up to the logical parent activity.
				            NavUtils.navigateUpTo(SettingsActivity.this, upIntent);
				        }
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            dialog.dismiss();
			            break;
			        }
			    }
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit level? Your progress will be lost.").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
	        return true;
	    }		
		return super.onOptionsItemSelected(item);
	}
	
	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);
		}
	}

}
