package ro.pagepo.sokoban.levels;

import java.util.Iterator;
import java.util.List;

import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;
import android.content.Context;

/**
 * Handles all the operations with the levels management
 * @author VS
 *
 */
public class LevelsManager {

	static LevelsManager instance;
	
	LevelsDataSource datasource;
	
	/**
	 * returns the static instance of this class. If the instance is null it will be instantiated
	 * @param context - the app context
	 * @return an instance of this class
	 */
	public static LevelsManager getInstance(Context context){
		if (instance == null){
			instance= new LevelsManager(context);
		}
		return instance;
	}
	
	/**
	 * must be called after getInstance(context)
	 * @return an instance of this class
	 */
	public static LevelsManager getInstance(){
		if (instance == null){
			throw new NullPointerException("Instance is null. Call getInstance with a context first.");
		}		
		return instance;
	}
	
	public LevelsManager(Context context) {
		datasource = new LevelsDataSource(context);
		datasource.open();
	}
	
	/**
	 * get a list with all the levels packs existing in the app (original + imported)
	 * @return list of levels packs
	 */
	public List<LevelsPack> getAllLevelsPack(){
		return datasource.getAllLevelsPack();
	}
	
	/**
	 * get a list with all the levels from a specified pack by name
	 * @param packName the name of the pack that contains the levels 
	 * @return a list with all the levels from pack
	 */
	public List<Level> getAllLevelsFromPack(String packName){
		LevelsPack lp = datasource.getLevelPackByName(packName);		
		return datasource.getLevelsFromPack(lp.getId());
	}
	
	
	/**
	 * gets the level after lvl from the same pack as lvl
	 * @param lvl the level after which is the returned level
	 * @return the next level in pack or null if lvl is the last one
	 */
	public Level getNextLevel(Level lvl){
		LevelsPack lp = datasource.getLevelPackById(lvl.getPack_id());
		List<Level> all = getAllLevelsFromPack(lp.getName());
		Iterator<Level> it = all.iterator();
		while (it.hasNext()) {
			Level level = it.next();
			if (level.getId() == lvl.getId()){
				if (!it.hasNext()) return null;
				return it.next();
			}
		}
		return null;
	}
	
	/**
	 * mark level as solved
	 * @param lvl - level to be marked as solved
	 */
	public void levelFinished(Level lvl){
		lvl.setSolved(1);
		datasource.updateLevel(lvl);
	}
	
	public int deleteLevelPack(LevelsPack lp){
		return datasource.deleteLevelPack(lp.getId());
	}
}
