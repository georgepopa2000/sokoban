package ro.pagepo.sokoban.activities;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.fragment.SokobanLevelFragment;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class SokobanActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sokoban);

		if (savedInstanceState == null) {
			Fragment f = SokobanLevelFragment.newInstance();
			f.setHasOptionsMenu(true);
			getFragmentManager().beginTransaction()
					.add(R.id.container, f).commit();
		}
		
	}
	


}
