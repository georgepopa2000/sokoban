package ro.pagepo.sokoban.levels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;
import ro.pagepo.sokoban.levels.exception.InvalidXMLLevelPackException;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class ImportLevelsPack {
	
	AssetManager am;
	LevelsDataSource datasource;

	public ImportLevelsPack(Context context) {
		super();
		this.am = context.getAssets();
		this.datasource = new LevelsDataSource(context);		
	}
	
	public void importLevelsFromAssets(){
		try {
			this.datasource.open();
			String[] lists = am.list("slc");
			for (int i = 0; i < lists.length; i++) {
				importLevelPackFromInputStream(am.open("slc/"+lists[i]), LevelsPack.TYPE_ORIGINAL);
			}
			this.datasource.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidXMLLevelPackException e) {
			e.printStackTrace();
		}
		
	}
	
	public void importLevelsFromFile(String filepath) throws InvalidXMLLevelPackException{
		try {
			this.datasource.open();
			importLevelPackFromInputStream(new FileInputStream(filepath), LevelsPack.TYPE_IMPORTED);
			this.datasource.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void importLevelPackFromInputStream(InputStream is,int type) throws InvalidXMLLevelPackException{
		ParseXMLLevelsPack plp = ParseXMLLevelsPack.parseDocument(is);
		plp.getLevelsPack().setType(type);
		LevelsPack lp = datasource.getLevelPackByName(plp.getLevelsPack().getName());
		if (lp != null) return; //if levels pack already inserted skip it
		long levelPackId = datasource.insertLevelPack(plp.getLevelsPack());
		ArrayList<Level> all = plp.getListLevels();
		for (Iterator<Level> iterator = all.iterator(); iterator.hasNext();) {
			Level level = (Level) iterator.next();
			level.setPack_id(levelPackId);			
			Log.d("contentlength",level.getContent().length()+" "+level.getWidth()*level.getHeight());
			datasource.insertLevel(level);
		}		
	}
	
}
