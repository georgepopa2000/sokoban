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
	
}
