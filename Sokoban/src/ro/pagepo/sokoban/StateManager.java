package ro.pagepo.sokoban;

import java.util.Stack;

//undo redo mechanism and current board state
public class StateManager {

	private Stack<BoardState> undoStack;
	private Stack<BoardState> redoStack;
	private BoardState currentState;
	
	public StateManager() {
		undoStack = new Stack<BoardState>();
		redoStack = new Stack<BoardState>();
		currentState = null;
	}
	
	public void setCurrentState(BoardState state){
		if (currentState!=null) undoStack.push(currentState);
		redoStack.empty();
		currentState = state;
	}
	
	public BoardState getCurrentState(){
		return currentState;
	}
	
	public BoardState redo(){
		if (redoStack.isEmpty()) return currentState;
		undoStack.push(currentState);
		currentState = redoStack.pop();
		return currentState;
	}
	
	public boolean canRedo(){
		return !redoStack.isEmpty();
	}
	
	public BoardState undo(){
		if (undoStack.isEmpty()) return currentState;
		redoStack.push(currentState);
		currentState = undoStack.pop();
		return currentState;
	}
	
	public boolean canUndo(){
		return !undoStack.isEmpty();
	}
}
