package ro.pagepo.sokoban;

public class StateElement {
	public static final int STATE_WALL = 1;
	public static final int STATE_EMPTY = 0;
	public static final int STATE_FLOOR = 2;
	
	int boardElementState = -1;

	public StateElement( ) {
		boardElementState = STATE_WALL;
	}

	public StateElement(int boardElementState) {
		this.boardElementState = boardElementState;
	}

	public int getBoardElementState() {
		return boardElementState;
	}

	public void setBoardElementState(int boardElementState) {
		this.boardElementState = boardElementState;
	}
	
	

}
