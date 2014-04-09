package ro.pagepo.sokoban;

import java.util.ArrayList;

import android.util.Log;

//should contain the board state at one moment
public class BoardState {
	ArrayList<PositionCoordinates> brickList = new ArrayList<PositionCoordinates>();	
	PositionCoordinates pakPosition;
	
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



	//add a movable brick to the list
	public void addBrick(int x,int y){
		brickList.add(new PositionCoordinates(x, y));
	}
	
	
	//sets position of the element to be controlled
	public void setPakPosition(int x,int y){
		pakPosition = new PositionCoordinates(x,y);
	}
	
	public int getPakPositionX(){
		return pakPosition.x;
	}
	
	public int getPakPositionY(){
		return pakPosition.y;
	}
	
	public int getBrickPositionXAt(int index){
		return brickList.get(index).x;
	}
	
	public int getBrickPositionYAt(int index){
		return brickList.get(index).y;
	}
	
	public int countBricks(){
		return brickList.size();
	}
	
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
	
}
