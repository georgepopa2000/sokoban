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


public class SokoBoardView extends View {
	
	private BoardMap boardState;
	
	
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
	Bitmap wallBitmap,floorBitmap,doorBitmap;
	Rect r;

	private void init(AttributeSet attrs, int defStyle) {
		p=new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.rgb(10, 18, 17));
		wallBitmap =BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_wall);
		floorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_floor);
		doorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_draw_door);
		
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
		int size = (int)getWidth()/16;
		canvas.drawColor(p.getColor());
		if (boardState!=null){
			for (int i=0;i<=15;i++){
				for (int j=0;j<=15;j++){
					r.set(size*i, size*j, size*i + size, size*j + size);
					int state =boardState.getStateElement(i, j);
					Bitmap b =null;
					Bitmap bt = null;
					if (state == StateElement.STATE_FLOOR) b = floorBitmap; else
					if (state == StateElement.STATE_WALL) b = wallBitmap; else 
						if (state == StateElement.STATE_DOOR) {
							b = wallBitmap;
							bt = doorBitmap;
						}
					Log.d("xxx","bitmap is "+b);
					if (b!= null) canvas.drawBitmap(b, null, r, null);
					if (bt!= null) canvas.drawBitmap(bt, null, r, null);
				}
			}
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
	
	public void setBoardState(BoardMap bs){
		this.boardState = bs;
		this.invalidate();
	}
	

}
