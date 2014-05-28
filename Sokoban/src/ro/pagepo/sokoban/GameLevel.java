package ro.pagepo.sokoban;

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
		return true;
	}
	
}
