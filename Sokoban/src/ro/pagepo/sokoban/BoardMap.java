package ro.pagepo.sokoban;

import java.util.ArrayList;

//contains map of the board
public class BoardMap {
	String string = "xxxxxppppppxxxxxzzzzzzpppzzzzzzzzppppppfpppppppzzppppppppppppppzzppppppfppppzzzzzzzpppppppppzxxxxxzpppppppppzxxxxxzppppppzzzzxxxzzzppppppzxxxxxxzppppppppzzzzzzzzppppppppppppppzzppppppppppppppzzzzzzzzzzppppppzxxxxxxxxzppppppzxxxxxxxxzpppzzzzxxxxxxxxpppppxxx";

	ArrayList<ArrayList<StateElement>> al ;
	public BoardMap() {
		al = new ArrayList<ArrayList<StateElement>>();
		ArrayList<StateElement> alRow;
		
		int row =0;int column =0;
		for (int i=0;i<string.length();i++){
			column = i%16;
			row = i/16;
			if (column == 0){
				alRow = new ArrayList<StateElement>();
				al.add(alRow);
			}
			
			
			int state = -1;
			if (string.substring(i, i+1).equals("x")) state = StateElement.STATE_EMPTY; else
				if (string.substring(i, i+1).equals("z")) state = StateElement.STATE_WALL; else
					if (string.substring(i, i+1).equals("f")) state = StateElement.STATE_DOOR; else
						if (string.substring(i, i+1).equals("p")) state = StateElement.STATE_FLOOR;
			
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
