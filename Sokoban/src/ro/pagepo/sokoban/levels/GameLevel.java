package ro.pagepo.sokoban.levels;

import ro.pagepo.sokoban.map.BoardMap;
import ro.pagepo.sokoban.map.PositionCoordinates;
import ro.pagepo.sokoban.map.state.BoardState;
import ro.pagepo.sokoban.map.state.StateElement;
import ro.pagepo.sokoban.map.state.StateManager;

//should contain map, state, state manager
public class GameLevel {

	StateManager stateManager;
	BoardMap boardMap;
	
	public GameLevel(){
		boardMap = new BoardMap();
		stateManager = new StateManager();
		BoardState bs = new BoardState();
		bs.addBrick(3, 3);
		bs.addBrick(4, 4);
		bs.setPakPosition(5, 5);
		stateManager.setCurrentState(bs);
	}
	
	public BoardState getCurrentBoardState(){
		return stateManager.getCurrentState();
	}
	
	public BoardMap getBoardMap(){
		return boardMap;
	}
	
	public StateManager getStateManager(){
		return stateManager;
	}
	
	public void move(int mode){
		BoardState bs = stateManager.getCurrentState();
		bs = bs.move(mode);
		stateManager.setCurrentState(bs);
	}
	
	/**
	 * check if the pak can move to the new position
	 * @param mode where should the pak move relative to the current position left,right,up,down BoardState.MOVE_LEFT,BoardState.MOVE_RIGHT ....
	 * @return true if the pak can move, false otherwise
	 */
	public boolean canMove(int mode){
		BoardState bs = stateManager.getCurrentState();
		if (!bs.canMove(mode)) return false;
		int offsetx = 0;
		int offsetxx = 0;
		int offsety = 0;
		int offsetyy = 0;
		switch (mode){
		case BoardState.MOVE_LEFT:
			offsetx = -1;
			offsetxx = -2; 
			break;
		case BoardState.MOVE_RIGHT:
			offsetx = 1;
			offsetxx = 2;
			break;
		case BoardState.MOVE_TOP:
			offsety = -1;
			offsetyy = -2;
			break;
		case BoardState.MOVE_BOTTOM:
			offsety = 1;
			offsetyy = 2;
			break;
		}
		
		//wants to exit the table board
		if ((bs.getPakPositionX()+offsetx<0)||(bs.getPakPositionY()+offsety<0)||(bs.getPakPositionX()+offsetx>=boardMap.getSizeX())||(bs.getPakPositionY()+offsety>=boardMap.getSizeY())) return false; 
		
		//state of the board element near
		int state = boardMap.getStateElement(bs.getPakPositionX()+offsetx, bs.getPakPositionY()+offsety);
		
		//if has wall or empty space
		if ((state == StateElement.STATE_WALL)||(state==StateElement.STATE_EMPTY)) return false;
		
		//if has brick
		if (bs.isBrickAt(new PositionCoordinates(bs.getPakPositionX()+offsetx, bs.getPakPositionY()+offsety))){
			//wants to exit the table board
			if ((bs.getPakPositionX()+offsetxx<0)||(bs.getPakPositionY()+offsetyy<0)||(bs.getPakPositionX()+offsetxx>=boardMap.getSizeX())||(bs.getPakPositionY()+offsetyy>=boardMap.getSizeY())) return false;
			//state of the board element second near
			int statex = boardMap.getStateElement(bs.getPakPositionX()+offsetxx, bs.getPakPositionY()+offsetyy);			
			if ((statex == StateElement.STATE_WALL)||(statex==StateElement.STATE_EMPTY)) return false;
		}
		
		
		return true;
	}
	
	/**
	 * check if the level is solved
	 * @return true if every door has a brick
	 */
	public boolean isLevelFinished(){
		for (int i =0; i< this.getBoardMap().getSizeX();i++){
			for (int j=0; j<this.getBoardMap().getSizeY();j++){
				if (this.getBoardMap().getStateElement(i, j) == StateElement.STATE_DOOR){
					if (!this.getCurrentBoardState().isBrickAt(new PositionCoordinates(i, j))) return false;
				}
			}
		}
		return true;
	}
}