package ro.pagepo.sokoban.map.state;

import java.util.ArrayList;

import ro.pagepo.sokoban.map.PositionCoordinates;

/**
 * Contains the board state at some given moment. Handles the brick (boxes) position and the pack (sokoban man) position 
  *
 */
public class BoardState {
	ArrayList<PositionCoordinates> brickList = new ArrayList<PositionCoordinates>();	
	PositionCoordinates pakPosition;
	
	//aliases for possible moves
	public static final int MOVE_LEFT = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int MOVE_TOP = 3;
	public static final int MOVE_BOTTOM = 4;
	
	
	
	public BoardState(){
		brickList = new ArrayList<PositionCoordinates>();
		pakPosition = null;
	}
	
	
	
	public BoardState(ArrayList<PositionCoordinates> brickList,
			PositionCoordinates pakPosition) {
		super();
		this.brickList = new ArrayList<PositionCoordinates>();
		for (PositionCoordinates pc : brickList ){
			addBrick(pc.x, pc.y);
		}
		this.pakPosition = new PositionCoordinates(pakPosition.x, pakPosition.y);
	}



	/**
	 * add a brick
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public void addBrick(int x,int y){
		brickList.add(new PositionCoordinates(x, y));
	}
	
	
	/**
	 * position of the sokoban
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public void setPakPosition(int x,int y){
		pakPosition = new PositionCoordinates(x,y);
	}
	
	public int getPakPositionX(){
		return pakPosition.x;
	}
	
	public int getPakPositionY(){
		return pakPosition.y;
	}
	
	/**
	 * returns X coordinate for the brick at the index index
	 * @param index - brick index
	 * @return x coordinate of the brick
	 */
	public int getBrickPositionXAt(int index){
		return brickList.get(index).x;
	}

	/**
	 * returns Y coordinate for the brick at the index index
	 * @param index - brick index
	 * @return y coordinate of the brick
	 */
	
	public int getBrickPositionYAt(int index){
		return brickList.get(index).y;
	}
	
	/**
	 * number of bricks
	 * @return the number of bricks
	 */
	public int countBricks(){
		return brickList.size();
	}
	
	/**
	 * Tries to move to a new position based on the move type and returns the new BoardState after the move
	 * @param moveType - can be MOVE_LEFT, MOVE_RIGHT etc.
	 * @return the new BoardState aftre the move
	 */
	public BoardState move(int moveType){
		PositionCoordinates newPakPosition = new PositionCoordinates(pakPosition.x, pakPosition.y);
		PositionCoordinates newBrickPosition = new PositionCoordinates(pakPosition.x, pakPosition.y);
		BoardState newBoardState = new BoardState();
		switch (moveType){
			case MOVE_LEFT:
				newPakPosition.x = newPakPosition.x - 1;
				newBrickPosition.x = newPakPosition.x - 1; 
				break;
			case MOVE_RIGHT:
				newPakPosition.x = newPakPosition.x + 1;
				newBrickPosition.x = newPakPosition.x + 1;
				break;
			case MOVE_TOP:
				newPakPosition.y = newPakPosition.y - 1;
				newBrickPosition.y = newPakPosition.y - 1;
				break;
			case MOVE_BOTTOM:
				newPakPosition.y = newPakPosition.y + 1;
				newBrickPosition.y = newPakPosition.y + 1;
				break;
			default: return this;	
		}
		newBoardState.setPakPosition(newPakPosition.x, newPakPosition.y);
		for (PositionCoordinates pc : brickList){
			if ((pc.x == newPakPosition.x) &&(pc.y == newPakPosition.y)){
				newBoardState.addBrick(newBrickPosition.x, newBrickPosition.y);
			} else newBoardState.addBrick(pc.x, pc.y);
		}
		return newBoardState;
	}
	
	/**
	 * check if the pak can move to the moveType direction
	 * @param moveType int can be move_left, move_right, move_top, move_bottom
	 * @return true if there aren't two bricks in front of the pak to block the move
	 */
	public boolean canMove(int moveType){
		int offsetx = 0;
		int offsetxx = 0;
		int offsety = 0;
		int offsetyy = 0;
		switch (moveType){
		case MOVE_LEFT:
			offsetx = -1;
			offsetxx = -2; 
			break;
		case MOVE_RIGHT:
			offsetx = 1;
			offsetxx = 2;
			break;
		case MOVE_TOP:
			offsety = -1;
			offsetyy = -2;
			break;
		case MOVE_BOTTOM:
			offsety = 1;
			offsetyy = 2;
			break;
	}
		if (isBrickAt(new PositionCoordinates(pakPosition.x+offsetx, pakPosition.y+offsety))){
			if (isBrickAt(new PositionCoordinates(pakPosition.x+offsetxx, pakPosition.y+offsetyy))){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * checks if there's a brick at the given position
	 * @param position PositionCoordinates to check if there's a brick there
	 * @return true if there's a brick at the position coordinates false otherwise
	 */
	public boolean isBrickAt(PositionCoordinates position){
		if (brickList.indexOf(position)>=0) return true;
		return false;
	}
	
}
