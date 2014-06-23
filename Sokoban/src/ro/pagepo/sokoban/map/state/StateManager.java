package ro.pagepo.sokoban.map.state;

import java.util.Stack;


/**
 * Manages all the states that pass on the game, also handles undo and redo states 
 *
 */
public class StateManager {

	private Stack<BoardState> undoStack;
	private Stack<BoardState> redoStack;
	private BoardState currentState;
	
	public StateManager() {
		undoStack = new Stack<BoardState>();
		redoStack = new Stack<BoardState>();
		currentState = null;
	}
	
	/**
	 * Set a new current state
	 * @param state - the new current state
	 */
	public void setCurrentState(BoardState state){
		if (currentState!=null) undoStack.push(currentState);
		redoStack.clear();
		currentState = state;
	}
	
	public BoardState getCurrentState(){
		return currentState;
	}
	
	
	/**
	 * Pull a redo state from the stack if there is one
	 * @return the state pulled from the redo stack
	 */
	public BoardState redo(){
		if (redoStack.isEmpty()) return currentState;
		undoStack.push(currentState);
		currentState = redoStack.pop();
		return currentState;
	}
	
	/**
	 * Checks if there are any states on the redo stack
	 * @return true if there are states on the stack
	 */
	public boolean canRedo(){
		return !redoStack.isEmpty();
	}
	
	/**
	 * Pull a undo state from the stack if there is one
	 * @return the state pulled from the undo stack
	 */

	public BoardState undo(){
		if (undoStack.isEmpty()) return currentState;
		redoStack.push(currentState);
		currentState = undoStack.pop();
		return currentState;
	}
	
	/**
	 * Checks if there are more states to undo
	 * @return true if the undo is possible
	 */
	public boolean canUndo(){
		return !undoStack.isEmpty();
	}
	
	
	/**
	 * number of states in undo stack
	 * @return the number of states
	 */
	public int getNumberOfStates(){
		return undoStack.size();
	}
}
