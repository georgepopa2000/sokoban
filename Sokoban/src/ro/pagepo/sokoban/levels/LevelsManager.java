package ro.pagepo.sokoban.levels;

import java.util.ArrayList;
import java.util.List;

import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;
import android.content.Context;

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
}
