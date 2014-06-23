package ro.pagepo.sokoban.activities;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.fragment.SokobanLevelFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

public class SokobanActivity extends Activity {
	
	public static final String PUT_LEVEL = "put_level";
	public static final String PUT_LEVELPACK_NAME = "put_level_pack_name";
	
	public static final String TAG_FRAGMENT_SOKO = "fragmentsokoban";

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
					.add(R.id.container, f, TAG_FRAGMENT_SOKO).commit();
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
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
				    	
				        Intent upIntent = NavUtils.getParentActivityIntent(SokobanActivity.this);
				        if (NavUtils.shouldUpRecreateTask(SokobanActivity.this, upIntent)) {
				            // This activity is NOT part of this app's task, so create a new task
				            // when navigating up, with a synthesized back stack.
				            TaskStackBuilder.create(SokobanActivity.this)
				                    // Add all of this activity's parents to the back stack
				                    .addNextIntentWithParentStack(upIntent)
				                    // Navigate up to the closest parent
				                    .startActivities();
				        } else {
				            // This activity is part of this app's task, so simply
				            // navigate up to the logical parent activity.
				            NavUtils.navigateUpTo(SokobanActivity.this, upIntent);
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
	
	@Override
	public void onBackPressed() {
		SokobanLevelFragment slf = (SokobanLevelFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT_SOKO);
		slf.onBackPressed();
		//super.onBackPressed();
	}


}
