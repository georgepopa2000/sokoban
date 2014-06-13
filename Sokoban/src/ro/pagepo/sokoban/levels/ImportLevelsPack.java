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

/**
 * Class to import levels pack files from the assest folder or an input file from user
 * @author VS
 *
 */
public class ImportLevelsPack {
	
	AssetManager am;
	LevelsDataSource datasource;

	public ImportLevelsPack(Context context) {
		super();
		this.am = context.getAssets();
		this.datasource = new LevelsDataSource(context);		
	}
	
	/**
	 * import levels pack from the file existing in the folder assets/slc
	 */
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
	
	/**
	 * import a level pack from an xml file from disk
	 * @param filepath - path to the xml file to import
	 * @throws InvalidXMLLevelPackException - if the file doesn't have the required format;
	 */
	public void importLevelsFromFile(String filepath) throws InvalidXMLLevelPackException{
		try {
			this.datasource.open();
			importLevelPackFromInputStream(new FileInputStream(filepath), LevelsPack.TYPE_IMPORTED);
			this.datasource.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * import level pack to database from an input stream
	 * @param is - the input stream to import from
	 * @param type - the type of the level pack can be <code>LevelsPack.TYPE_IMPORTED</code> or  <code>LevelsPack.TYPE_ORIGINAL</code>
	 * @throws InvalidXMLLevelPackException - if the stream doesn't contain a properly XML file
	 */
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
			datasource.insertLevel(level);
		}		
	}
	
}
