package ro.pagepo.sokoban.map;

import java.util.ArrayList;

import android.util.Log;

import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.map.state.StateElement;

/**
 * Contains a map of the game board for a level. Only has the StateElemetns not the board state 
  */
public class BoardMap {

	ArrayList<ArrayList<StateElement>> al ;
	
	/**
	 * initialize the map based on the level
	 * @param level
	 */
	public BoardMap(Level level) {
		al = new ArrayList<ArrayList<StateElement>>();
		ArrayList<StateElement> alRow;
		
		String string = level.getContent(); 
		
		int row =0;int column =0;
		for (int i=0;i<string.length();i++){
			column = i%level.getWidth();
			row = i/level.getWidth();
			if (column == 0){
				alRow = new ArrayList<StateElement>();
				al.add(alRow);
			}
			
			
			int state = -1;
			if (string.substring(i, i+1).equals("xxx ")) state = StateElement.STATE_EMPTY; else
				if (string.substring(i, i+1).equals("#")) state = StateElement.STATE_WALL; else
					if ((string.substring(i, i+1).equals("."))||(string.substring(i, i+1).equals("+"))||(string.substring(i, i+1).equals("*"))) state = StateElement.STATE_DOOR; else
						if (string.substring(i, i+1).equals(" ")||(string.substring(i, i+1).equals("@"))||(string.substring(i, i+1).equals("$"))) state = StateElement.STATE_FLOOR;
			
			al.get(row).add(new StateElement(state));
			
		}
	}
	
	public int getStateElement(int i,int j){
		return al.get(i).get(j).getBoardElementState();
	}
	
	/**
	 * get size x of the board
	 * @return the count of the horizontal elements
	 */
	public int getSizeX(){
		return al.size();
	}
	
	/**
	 * get size y of the board
	 * @return the max count of the vertical elements
	 */	
	public int getSizeY(){
		int max = 0;
		for (int i=0;i<al.size();i++){
			ArrayList<StateElement> alx = al.get(i);
			if (alx.size() > max) max = alx.size();					
		}		
		return max;
	}
}
