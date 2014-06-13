package ro.pagepo.sokoban.database.model;

public class Level {

	long id; //database id
	long pack_id; //levels pack id
	String name;
	/**
	 * 0 not solved, 1 solved
	 */
	int solved;
	int width;
	int height;
	/**
	 * contains elements of the level
	 */
	String content;
	
	
	
	public Level() {
		super();
		this.id = -1;
		this.solved = 0;
		this.pack_id = -1;
	}

	public Level(long id, long pack_id, String name, int solved, int width,
			int height, String content) {
		this.id = id;
		this.pack_id = pack_id;
		this.name = name;
		this.solved = solved;
		this.width = width;
		this.height = height;
		this.content = content;
	}
	
	
	public Level(long pack_id, String name, int solved, int width,
			int height, String content) {
		this.id = -1;
		this.pack_id = pack_id;
		this.name = name;
		this.solved = solved;
		this.width = width;
		this.height = height;
		this.content = content;
	}	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPack_id() {
		return pack_id;
	}

	public void setPack_id(long pack_id) {
		this.pack_id = pack_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSolved() {
		return solved;
	}

	public void setSolved(int solved) {
		this.solved = solved;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
		
}
