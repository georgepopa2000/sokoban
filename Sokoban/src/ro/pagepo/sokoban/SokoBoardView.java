package ro.pagepo.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


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
	
	Paint p,xx;
	Bitmap wallBitmap,floorBitmap,doorBitmap,pakBitmap,brickBitmap;
	Rect r;

	private void init(AttributeSet attrs, int defStyle) {
		p=new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.rgb(10, 18, 17));
		xx=new Paint(Paint.ANTI_ALIAS_FLAG);
		xx.setColor(Color.rgb(255, 0, 0));
		wallBitmap =BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_wall);
		floorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_floor);
		doorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_door);
		pakBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_pak);
		brickBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_brick);
		
		r = new Rect(0,0,0,0);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int d=Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));
		
		setMeasuredDimension(d,d);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int size = getBrickSize();	
		//Log.d("xxx",getWidth()+"");
		//Log.d("xxx", size+"");
		canvas.drawColor(p.getColor());
		if (gameLevel!=null){
			BoardState boardState = gameLevel.getCurrentBoardState();
			BoardMap boardMap = gameLevel.getBoardMap();
			for (int i=0;i<=15;i++){
				for (int j=0;j<=15;j++){
					r.set(size*i, size*j, size*i + size, size*j + size);
					
					int state =boardMap.getStateElement(i, j);
					Bitmap b =null;
					Bitmap bt = null;
					if (state == StateElement.STATE_FLOOR) b = floorBitmap; else
					if (state == StateElement.STATE_WALL) b = wallBitmap; else 
						if (state == StateElement.STATE_DOOR) {
							b = floorBitmap;
							bt = doorBitmap;
						}
					
					if (b!= null) {canvas.drawRect(r, xx);canvas.drawBitmap(b, null, r, null);}
					if (bt!= null) canvas.drawBitmap(bt, null, r, null);
				}
			}
			
			for (int i=0;i<boardState.countBricks();i++){
				int x = boardState.getBrickPositionXAt(i);
				int y = boardState.getBrickPositionYAt(i);
				r.set(size*x, size*y, size*x + size, size*y + size);
				canvas.drawBitmap(brickBitmap, null, r, null);
			}
			int x = boardState.getPakPositionX();
			int y = boardState.getPakPositionY();
			r.set(size*x, size*y, size*x + size, size*y + size);
			canvas.drawBitmap(pakBitmap, null, r, null);			
		}
		
		
	}
	
		
	private int measure(int measureSpec){
		
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		
		int result = 0;
		if (mode == MeasureSpec.UNSPECIFIED){
			result = 300;
		} else result = size;
		
		return result;
	}
	
	public void setGameLevel(GameLevel gl){
		this.gameLevel = gl;
		this.invalidate();
	}
	
	/**
	 * gets the size of one brick 
	 * @return size in pixels
	 */
	public int getBrickSize(){
		return (int) Math.round(((double)getWidth())/16);
	}
	
}
