package ro.pagepo.sokoban.fragment;

import java.util.List;

import ro.pagepo.sokoban.database.model.LevelsPack;
import ro.pagepo.sokoban.levels.LevelsManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 */
public class LevelsPackFragment extends ListFragment {

	List<LevelsPack> llp;
	public LevelsPackFragment() {
		LevelsManager.getInstance(getActivity());
		llp = LevelsManager.getInstance().getAllLevelsPack();		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	

		setListAdapter(new ArrayAdapter<LevelsPack>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				llp));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		LevelsPack lp = llp.get(position);
		Log.d("onclick", lp.getName()+" ");

	}


}
