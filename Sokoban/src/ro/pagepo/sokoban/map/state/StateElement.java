package ro.pagepo.sokoban.map.state;

/**
 * An element on the board
 * Can be a wall, empty space, floor, door (target)
 * It just handles boardmap elements.
 */
public class StateElement {
	public static final int STATE_WALL = 1;
	public static final int STATE_EMPTY = 0;
	public static final int STATE_FLOOR = 2;
	public static final int STATE_DOOR = 3;
	
	int boardElementState = -1;

	public StateElement( ) {
		boardElementState = STATE_EMPTY;
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
