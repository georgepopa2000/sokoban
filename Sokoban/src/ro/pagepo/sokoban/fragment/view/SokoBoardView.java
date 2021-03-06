package ro.pagepo.sokoban.fragment.view;

import ro.pagepo.sokoban.R;
import ro.pagepo.sokoban.levels.GameLevel;
import ro.pagepo.sokoban.map.BoardMap;
import ro.pagepo.sokoban.map.state.BoardState;
import ro.pagepo.sokoban.map.state.StateElement;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Component View to display the Sokoban game board
 * @author VS
 *
 */
public class SokoBoardView extends View{
	
	private GameLevel gameLevel;
	
	
	public SokoBoardView(Context context) {
		super(context);
		init(null, 0);
	}

	public SokoBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public SokoBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}
	
	Paint p;
	Bitmap wallBitmap,floorBitmap,doorBitmap,pakBitmap,brickBitmap,doorBrickBitmap;
	Rect r;

	/**
	 * initialize all the drawable needed in onDraw
	 * @param attrs
	 * @param defStyle
	 */
	private void init(AttributeSet attrs, int defStyle) {
		p=new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.rgb(10, 18, 17));
		wallBitmap =BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_wall);
		floorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_floor);
		doorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_door);
		pakBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_pak);
		brickBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_brick);
		doorBrickBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_door_brick);
		
		r = new Rect(0,0,0,0);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		
		
		int width = measure(widthMeasureSpec);
		int height = measure(heightMeasureSpec);
		int d=Math.min(width,height);
		//int newWidth = d;
		int newHeight = d;
		
		if (gameLevel != null) {
			int wtile = width/gameLevel.getBoardMap().getSizeY();
			int htile = height/gameLevel.getBoardMap().getSizeX();
			int tile = Math.min(wtile, htile);
			//newWidth = tile*gameLevel.getBoardMap().getSizeY();
			newHeight = tile*gameLevel.getBoardMap().getSizeX();
		}
			
		
		
		setMeasuredDimension(width,newHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//if the gamelevel not initialised or canvas null then akip draw.
		if (gameLevel == null) return;
		if (canvas == null) return;
		super.onDraw(canvas);
		
		int size = getBrickSize();		
		int leftMargins = getLeftMargins();
		if (gameLevel!=null){
			BoardState boardState = gameLevel.getCurrentBoardState();
			BoardMap boardMap = gameLevel.getBoardMap();
			
			//draw board map based on every ElementState
			for (int i=0;i<boardMap.getSizeX();i++){
				for (int j=0;j<boardMap.getSizeY();j++){
					r.set(leftMargins+size*j, size*i, leftMargins +size*j + size, size*i + size);
					
					int state =boardMap.getStateElement(i, j);
					Bitmap b =null;
					Bitmap bt = null;
					if (state == StateElement.STATE_FLOOR) b = floorBitmap; else
					if (state == StateElement.STATE_WALL) b = wallBitmap; else 
						if (state == StateElement.STATE_DOOR) {
							b = floorBitmap;
							bt = doorBitmap;
						}
					
					if (b!= null) {canvas.drawBitmap(b, null, r, null);}
					if (bt!= null) canvas.drawBitmap(bt, null, r, null);
				}
			}
			
			//draw the board state (bricks)
			for (int i=0;i<boardState.countBricks();i++){
				int x = boardState.getBrickPositionXAt(i);
				int y = boardState.getBrickPositionYAt(i);
				int state =boardMap.getStateElement(x, y);
				r.set(leftMargins +size*y, size*x, leftMargins + size*y + size, size*x + size);
				if (state == StateElement.STATE_DOOR)	 canvas.drawBitmap(doorBrickBitmap, null, r, null); 
					else canvas.drawBitmap(brickBitmap, null, r, null);
			}
			//draw the sokoban man
			int x = boardState.getPakPositionX();
			int y = boardState.getPakPositionY();
			r.set(leftMargins + size*y, size*x, leftMargins + size*y + size, size*x + size);
			canvas.drawBitmap(pakBitmap, null, r, null);			
		}
		
		
	}
	
		
	private int measure(int measureSpec){
		
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		
		int result = 0;
		if (mode == MeasureSpec.UNSPECIFIED){
			result = 100;
		} else result = size;
		
		return result;
	}
	
	/**
	 * set the current game level for the component and asks for a redraw.
	 * @param gl = the new GameLevel
	 */
	public void setGameLevel(GameLevel gl){
		this.gameLevel = gl;
		this.invalidate();
	}
	
	/**
	 * gets the size of one brick 
	 * @return size in pixels
	 */
	public int getBrickSize(){		
		return Math.min((int) Math.round(((double)getWidth())/gameLevel.getBoardMap().getSizeY()),(int) Math.round(((double)getHeight())/gameLevel.getBoardMap().getSizeX()));
	}
	
	/**
	 * the remaining space that's not used by the board 
	 * because the last column doesn't fit (rounding) should be split evenly left and right 
	 * @return the size half of the space remained not used by the board
	 */
	private int getLeftMargins(){
		return Math.round((getWidth()-getBrickSize()*gameLevel.getBoardMap().getSizeY())/2);
	}
	
}
