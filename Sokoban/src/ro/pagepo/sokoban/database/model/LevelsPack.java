package ro.pagepo.sokoban.database.model;

import java.security.InvalidParameterException;

public class LevelsPack {
	/**
	 * pack id in database, -1 if not in the database
	 */
	long id;
	/**
	 * level pack name
	 */
	String name;
	/**
	 * a level pack can be an original pack or imported by user
	 */
	int type; 
	
	public static final int TYPE_ORIGINAL = 1; //level pack type original
	public static final int TYPE_IMPORTED = 2; //level pack type imported
	
	
	public LevelsPack(long id) {
		super();
		this.id = id;
		this.name = Long.toString(id);
		this.type = TYPE_ORIGINAL;
	}
	
	
	
	public LevelsPack(long id, String name, int type) {
		this(name,type);
		this.id = id;
	}
	
	public LevelsPack(String name, int type) {
		this(name);
		this.type = type;
	}	
	
	public LevelsPack(String name) {
		this.name = name;
		this.type = TYPE_ORIGINAL;
		this.id = -1;
	}	


	public void setId(long id){
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	
	/**
	 * set type
	 * @param type - level pack type can only be one of the TYPE_ORIGINAL or TYPE_IMPORTED
	 */
	public void setType(int type) {
		if ((type != TYPE_ORIGINAL) && (type != TYPE_IMPORTED)) throw new InvalidParameterException("Parameter type must be one of {TYPE_ORIGINAL,TYPE_IMPORTED} ");
		this.type = type;
	}
	
	
	
	
}
