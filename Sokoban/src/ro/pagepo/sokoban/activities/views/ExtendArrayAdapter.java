package ro.pagepo.sokoban.activities.views;
import java.util.List;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.database.model.Level;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ExtendArrayAdapter extends ArrayAdapter<Level> {
	
	Bitmap bis,bit;

	public ExtendArrayAdapter(Context context, int resource, List<Level> objects) {
		super(context, resource, objects);
		bis = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_checked);
		bit = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_unsolved);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout newLayout;
		if (convertView == null){
			newLayout = new LinearLayout(getContext());
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.levels_list, newLayout,true);
		} else {
			newLayout = (LinearLayout) convertView;
		}
		
		TextView lbl = (TextView) newLayout.findViewById(R.id.lblTextLevelName);
		ImageView is = (ImageView) newLayout.findViewById(R.id.imgSolved);
		
		Level lvl = getItem(position);
		if (lvl.getSolved() == 0){
			is.setImageBitmap(bit);
		} else {
			is.setImageBitmap(bis);			
		}
		lbl.setText("Level " + lvl.getName());
		
		return newLayout;
	}

}
