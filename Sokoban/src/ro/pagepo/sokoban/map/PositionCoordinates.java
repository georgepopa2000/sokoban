package ro.pagepo.sokoban.map;

/**
 * Coordinates of an element on the board
 *
 */
public class PositionCoordinates {
	public int x;
	public int y;
	public PositionCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PositionCoordinates)) return false;
		PositionCoordinates po = (PositionCoordinates) o;
		if ((po.x ==x)&&(po.y == y)) return true; 
		return false;
	}
}
