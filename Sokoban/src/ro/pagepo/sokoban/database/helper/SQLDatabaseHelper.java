package ro.pagepo.sokoban.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLDatabaseHelper extends SQLiteOpenHelper{

    // Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "sokobanLevelsManager";
    
    //tables names
    public static final String TABLE_LEVELSPACK = "levelspack";
    public static final String TABLE_LEVEL = "level";
    
    //levelspack table's columns names
    public static final String KEY_ID = "id";
    public static final String KEY_LP_NAME = "name";
    public static final String KEY_LP_TYPE ="type";
    
    //level table's columns names
    public static final String KEY_PACK = "pack_id";
    public static final String KEY_LVL_NAME = "name";
    public static final String KEY_LVL_SOLVED ="solved";
    public static final String KEY_LVL_HEIGHT ="height";
    public static final String KEY_LVL_WIDTH ="width";
    public static final String KEY_LVL_CONTENT ="content";
    
    
    //create tables statements
    private static final String CREATE_TABLE_LEVELSPACK ="CREATE TABLE "
    			+ TABLE_LEVELSPACK 
    			+ "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
    			+ KEY_LP_NAME +" TEXT, " 
    			+ KEY_LP_TYPE + " INTEGER )";
    private static final String CREATE_TABLE_LEVEL = "CREATE TABLE "
			+ TABLE_LEVEL 
			+ "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_PACK + " INTEGER,"
			+ KEY_LVL_NAME +" TEXT, " 
			+ KEY_LVL_SOLVED + " INTEGER,"
			+ KEY_LVL_HEIGHT + " INTEGER,"
			+ KEY_LVL_WIDTH + " INTEGER,"
			+ KEY_LVL_CONTENT +" TEXT,"
			+ "FOREIGN KEY("+KEY_PACK+") REFERENCES "+TABLE_LEVELSPACK+"("+KEY_ID+"))";
    
    
    
	
	

	public SQLDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG,CREATE_TABLE_LEVEL+" ----- "+CREATE_TABLE_LEVELSPACK);
		db.execSQL(CREATE_TABLE_LEVELSPACK);
		db.execSQL(CREATE_TABLE_LEVEL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELSPACK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVEL);
		onCreate(db);
	}
	
	

}
