package ro.pagepo.sokoban.levels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;
import ro.pagepo.sokoban.levels.exception.InvalidXMLLevelPackException;
import android.content.Context;
import android.content.res.AssetManager;

public class ImportLevelsPackFromAssets {
	
	AssetManager am;
	LevelsDataSource datasource;

	public ImportLevelsPackFromAssets(Context context) {
		super();
		this.am = context.getAssets();
		this.datasource = new LevelsDataSource(context);
	}
	
	public void importLevels(){
		try {
			String[] lists = am.list("slc");
			for (int i = 0; i < lists.length; i++) {
				ParseXMLLevelsPack plp = ParseXMLLevelsPack.parseDocument(am.open("slc/"+lists[i]));
				LevelsPack lp = datasource.getLevelPackByName(plp.getLevelsPack().getName());
				if (lp == null) continue; //if levels pack already inserted skip it
				long levelPackId = datasource.insertLevelPack(plp.getLevelsPack());
				ArrayList<Level> all = plp.getListLevels();
				for (Iterator<Level> iterator = all.iterator(); iterator.hasNext();) {
					Level level = (Level) iterator.next();
					level.setPack_id(levelPackId);
					datasource.insertLevel(level);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidXMLLevelPackException e) {
			e.printStackTrace();
		}
		
	}
	
}
