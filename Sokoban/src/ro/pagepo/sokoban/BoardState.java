package ro.pagepo.sokoban;

import java.util.ArrayList;

import android.util.Log;

public class BoardState {
	String string = "xxxxxxxxxxxxxxxxzzzzzzzzzzzzzzzzzppppppppppppppzzppppppppppppppzzpppppppppppzzzzzzzpppppppppzxxxxxzpppppppppzxxxxxzppppppzzzzxxxzzzppppppzxxxxxxzppppppppzzzzzzzzppppppppppppppzzppppppppppppppzzzzzzzzzzppppppzxxxxxxxxzppppppzxxxxxxxxzpppzzzzxxxxxxxxzzzzzxxx";

	ArrayList<ArrayList<StateElement>> al ;
	public BoardState() {
		al = new ArrayList<ArrayList<StateElement>>();
		ArrayList<StateElement> alRow ;
		
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
					if (string.substring(i, i+1).equals("p")) state = StateElement.STATE_FLOOR;
			
			al.get(row).add(new StateElement(state));
			
		}
	}
	
	public int getStateElement(int i,int j){
		return al.get(i).get(j).getBoardElementState();
	}

}
