package ro.pagepo.sokoban.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import ro.pagepo.sokoban.database.helper.SQLDatabaseHelper;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;

public class LevelsDataSource {
	
	SQLDatabaseHelper helper;
	SQLiteDatabase database;

	public LevelsDataSource(Context context) {
		helper = new SQLDatabaseHelper(context);
	}
	
	public void open() throws SQLException {
	    database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}
	
	/**
	 * insert a new LevelsPack
	 * @param lp
	 * @return the id of the levelpack inserted
	 */
	public long insertLevelPack(LevelsPack lp){
		ContentValues cv = new ContentValues();
		cv.put(SQLDatabaseHelper.KEY_LP_NAME, lp.getName());
		cv.put(SQLDatabaseHelper.KEY_LP_TYPE, lp.getType());
		long id = database.insert(SQLDatabaseHelper.TABLE_LEVELSPACK, null, cv);
		return id;
	}
	
	/**
	 * delete levelpack
	 * @param id - the id of the levels pack to be deleted
	 * @return number of levels pack deleted
	 */
	public int deleteLevelPack(long id){
		return database.delete(SQLDatabaseHelper.TABLE_LEVELSPACK, SQLDatabaseHelper.KEY_ID +" = "+ id, null);
	}
	
	
	/**
	 * get the level pack with a specific id
	 * @param id - the id of the level pack to be returned
	 * @return returns an instance of LevelsPack populated with the value in the database or null if id doesn't exists
	 */
	public LevelsPack getLevelPackById(long id){
		Cursor cursor = database.query(SQLDatabaseHelper.TABLE_LEVELSPACK, null, SQLDatabaseHelper.KEY_ID +" = "+ id, null, null, null, null);		
		cursor.moveToFirst();
		if (cursor.isAfterLast()) return null;
		LevelsPack lp = new LevelsPack(cursor.getLong(cursor.getColumnIndex(SQLDatabaseHelper.KEY_ID)), 
				cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LP_NAME)), 
				cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LP_TYPE)));
		cursor.close();
		return lp;
	}

	/**
	 * get the level pack with a specific name
	 * @param name - the name of the level pack to be returned
	 * @return returns an instance of LevelsPack populated with the value in the database or null if name doesn't exists
	 */	
	public LevelsPack getLevelPackByName(String name){
		Cursor cursor = database.query(SQLDatabaseHelper.TABLE_LEVELSPACK, null, SQLDatabaseHelper.KEY_LP_NAME +" = '"+ name + "'", null, null, null, null);		
		cursor.moveToFirst();
		if (cursor.isAfterLast()) return null;
		LevelsPack lp = new LevelsPack(cursor.getLong(cursor.getColumnIndex(SQLDatabaseHelper.KEY_ID)), 
				cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LP_NAME)), 
				cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LP_TYPE)));
		cursor.close();
		return lp;
	}
	
	/**
	 * gets all the levels pack in the database 
	 * @return a list of LevelsPack instances with the values from the database
	 */
	public List<LevelsPack> getAllLevelsPack(){
		List<LevelsPack> llp = new ArrayList<LevelsPack>();
		Cursor cursor = database.query(SQLDatabaseHelper.TABLE_LEVELSPACK, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			LevelsPack lp = new LevelsPack(cursor.getLong(cursor.getColumnIndex(SQLDatabaseHelper.KEY_ID)), 
					cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LP_NAME)), 
					cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LP_TYPE)));
			llp.add(lp);
			cursor.moveToNext();
		}
		cursor.close();
		return llp;
	}
	
	/**
	 * insert level
	 * @param level - level to be inserted into database
	 * @return the id of the new level inserted or -1 on error
	 */
	public long insertLevel(Level level){
		ContentValues cv = new ContentValues();
		cv.put(SQLDatabaseHelper.KEY_PACK, level.getPack_id());
		cv.put(SQLDatabaseHelper.KEY_LVL_NAME, level.getName());
		cv.put(SQLDatabaseHelper.KEY_LVL_SOLVED, level.getSolved());
		cv.put(SQLDatabaseHelper.KEY_LVL_HEIGHT, level.getHeight());
		cv.put(SQLDatabaseHelper.KEY_LVL_WIDTH, level.getWidth());
		cv.put(SQLDatabaseHelper.KEY_LVL_CONTENT, level.getContent());
		
		long ret = database.insert(SQLDatabaseHelper.TABLE_LEVEL, null, cv);
		return ret;
	}
	
	
	/**
	 * delete level
	 * @param id - the id of the level to be deleted
	 * @return the number of levels deleted
	 */
	public int deleteLevel(long id){		
		return database.delete(SQLDatabaseHelper.TABLE_LEVEL, SQLDatabaseHelper.KEY_ID + " = " + id, null);
	}
	
	/**
	 * get level with a specific id
	 * @param id - the id of the level to be retrieved
	 * @return an instance of Level with the values populated from the database, null if the id doesn't exists
	 */
	public Level getLevelByID(long id){
		Cursor cursor = database.query(SQLDatabaseHelper.TABLE_LEVEL, null, SQLDatabaseHelper.KEY_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast()) return null;
		Level lvl = new Level(cursor.getLong(cursor.getColumnIndex(SQLDatabaseHelper.KEY_PACK)), 
				cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_NAME)), 
				cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_SOLVED)), 
				cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_WIDTH)), 
				cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_HEIGHT)), 
				cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_CONTENT)));
		cursor.close();
		return lvl;
	}
	
	/**
	 * get all levels from a levels pack
	 * @param packId - the id of the levels pack
	 * @return return a list of instances of the levels from pack populated with data from the database
	 */
	public List<Level> getLevelsFromPack(long packId){
		Cursor cursor = database.query(SQLDatabaseHelper.TABLE_LEVEL, null, SQLDatabaseHelper.KEY_PACK + " = " + packId, null, null, null, null);
		List<Level> llvl = new ArrayList<Level>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Level lvl = new Level(cursor.getLong(cursor.getColumnIndex(SQLDatabaseHelper.KEY_PACK)), 
					cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_NAME)), 
					cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_SOLVED)), 
					cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_WIDTH)), 
					cursor.getInt(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_HEIGHT)), 
					cursor.getString(cursor.getColumnIndex(SQLDatabaseHelper.KEY_LVL_CONTENT)));			
			llvl.add(lvl);
			cursor.moveToNext();
		}
		cursor.close();
		return llvl;
	}
	
}
