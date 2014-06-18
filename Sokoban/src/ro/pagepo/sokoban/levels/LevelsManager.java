package ro.pagepo.sokoban.levels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;
import android.content.Context;
import android.util.Log;

public class LevelsManager {

	static LevelsManager instance;
	
	LevelsDataSource datasource;
	
	public static LevelsManager getInstance(Context context){
		if (instance == null){
			instance= new LevelsManager(context);
		}
		return instance;
	}
	
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
	
	public List<LevelsPack> getAllLevelsPack(){
		return datasource.getAllLevelsPack();
	}
	
	public List<Level> getAllLevelsFromPack(String packName){
		LevelsPack lp = datasource.getLevelPackByName(packName);		
		return datasource.getLevelsFromPack(lp.getId());
	}
	
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
	
	public void levelFinished(Level lvl){
		lvl.setSolved(1);
		Log.d("lm", "id "+lvl.getId());
		int i= datasource.updateLevel(lvl);
		Log.d("lm", "affected "+i);
	}
}
