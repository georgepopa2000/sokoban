package ro.pagepo.sokoban.levels;

public class LevelsManager {

	static LevelsManager instance;
	public static LevelsManager getInstance(){
		if (instance == null){
			instance= new LevelsManager();
		}
		return instance;
	}
	
	public LevelsManager() {
		
	}
}
