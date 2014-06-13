package ro.pagepo.sokoban.activities;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.fragment.SokobanLevelFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

public class SokobanActivity extends Activity {
	
	public static final String PUT_LEVEL = "put_level";
	public static final String PUT_LEVELPACK_NAME = "put_level_pack_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sokoban);
		
		Level lvl = (Level) getIntent().getSerializableExtra(PUT_LEVEL);
		String packName = getIntent().getStringExtra(PUT_LEVELPACK_NAME);

		if (savedInstanceState == null) {
			Fragment f = SokobanLevelFragment.newInstance(lvl,packName);
			f.setHasOptionsMenu(true);
			getFragmentManager().beginTransaction()
					.add(R.id.container, f).commit();
		}
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setDisplayShowTitleEnabled(false);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
	        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
	            // This activity is NOT part of this app's task, so create a new task
	            // when navigating up, with a synthesized back stack.
	            TaskStackBuilder.create(this)
	                    // Add all of this activity's parents to the back stack
	                    .addNextIntentWithParentStack(upIntent)
	                    // Navigate up to the closest parent
	                    .startActivities();
	        } else {
	            // This activity is part of this app's task, so simply
	            // navigate up to the logical parent activity.
	            NavUtils.navigateUpTo(this, upIntent);
	        }
	        return true;
	    }		
		return super.onOptionsItemSelected(item);
	}


}
