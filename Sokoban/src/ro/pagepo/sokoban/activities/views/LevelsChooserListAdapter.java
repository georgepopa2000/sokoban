package ro.pagepo.sokoban.activities.views;

import java.util.List;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.database.model.Level;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LevelsChooserListAdapter extends ArrayAdapter<Level> {
	
	Drawable drawSolved,drawNotSolved;

	
	
	public LevelsChooserListAdapter(Context context, int resource,
			List<Level> objects) {		
		super(context, resource, objects);
		
		drawSolved = context.getResources().getDrawable(R.drawable.ic_checked);
		drawNotSolved = context.getResources().getDrawable(R.drawable.ic_unsolved);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View cview = super.getView(position,convertView,parent);
		
		if (cview instanceof TextView) {
			TextView tv = (TextView) cview;
			Level lvl = getItem(position);
			tv.setText("Level "+lvl.getName());
			tv.setCompoundDrawablesWithIntrinsicBounds((lvl.getSolved()==0)?R.drawable.ic_unsolved:R.drawable.ic_checked, 0, 0, 0);			
		} 
		return cview;
	};

}
