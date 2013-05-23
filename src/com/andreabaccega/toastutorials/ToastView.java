package com.andreabaccega.toastutorials;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Canvas.VertexMode;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

public class ToastView extends TextView {
	public static enum ArrowPosition {
		LEFT,
		TOP,
		RIGHT,
		BOTTOM
	}

	private ArrowPosition aPos = ArrowPosition.BOTTOM;
	private Bitmap mBackground;
	private Canvas mCanvas = new Canvas();
	private RectF mRect = new RectF(), tmpRectf = new RectF();
	private Point[] vertices = new Point[3];
	private int pointerSize;
	private int normalPadding;
	private int roundedCornerRadius;
	private Paint bgPaint;
	
	public ToastView(Context context, ArrowPosition position) {
		super(context);
		aPos = position;
		pointerSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, getResources().getDisplayMetrics());
		normalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
		roundedCornerRadius = normalPadding/2;
		
		bgPaint = new Paint();
		bgPaint.setStyle(Paint.Style.FILL);
		// radius=10, y-offset=2, color=black  
		bgPaint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);  
		bgPaint.setColor(Color.YELLOW);
		
		
		vertices[0] = new Point();
		vertices[1] = new Point();
		vertices[2] = new Point();
		setArrowPosition(aPos);
	}

	/**
	 * Sets the position of the pointer/arrow
	 * @param aPos2
	 */
	public void setArrowPosition(ArrowPosition aPos2) {
		if (aPos == null || (aPos2!= null && !aPos.equals(aPos2)) ) {
			aPos = aPos2;

			invalidate();
		}
	}



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		

		switch (aPos) {
		case LEFT: 
			setPadding(pointerSize+normalPadding, normalPadding, normalPadding, normalPadding);
			break;
		case TOP:
			setPadding(normalPadding, pointerSize+normalPadding, normalPadding, normalPadding);
			break;
		case RIGHT:
			setPadding(normalPadding, normalPadding, pointerSize+normalPadding, normalPadding);
			break;
		case BOTTOM:
			setPadding(normalPadding, normalPadding, normalPadding, pointerSize+normalPadding);
			break;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mRect.left   = 0;
		mRect.top    = 0;
		mRect.right  = getMeasuredWidth();
		mRect.bottom = getMeasuredHeight();

		tmpRectf.left   = 0;
		tmpRectf.top    = 0;
		tmpRectf.right  = getMeasuredWidth();
		tmpRectf.bottom = getMeasuredHeight();
		// Fix rect
		switch (aPos) {
		case LEFT: 
			mRect.left     = pointerSize;
			tmpRectf.right = pointerSize;
			break;
		case TOP:
			mRect.top       = pointerSize;
			tmpRectf.bottom = pointerSize;
			break;
		case RIGHT:
			mRect.right  = mRect.right - pointerSize;
			tmpRectf.left = tmpRectf.right - pointerSize;
			break;
		case BOTTOM:
			mRect.bottom  = mRect.bottom - pointerSize;
			tmpRectf.top = tmpRectf.bottom - pointerSize;
			break;
		}

		mBackground = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);


		mCanvas.setBitmap(mBackground);
		
		mCanvas.drawRoundRect(mRect, roundedCornerRadius, roundedCornerRadius, bgPaint);

		mCanvas.clipRect(tmpRectf, Region.Op.REPLACE);
		
		// Building Vertixes for the pointer.
		fixVertices();
		
		
		drawArrows(vertices, mCanvas, bgPaint);

		

		setBackgroundDrawable(new BitmapDrawable(mBackground));
		Log.d("ANTONI", "Width: "+getMeasuredWidth()+" height:"+getMeasuredHeight());


	}

	private void fixVertices() {
		switch (aPos) {
			case BOTTOM: {
				vertices[0].x = (int) (mRect.centerX() - pointerSize/2);
				vertices[0].y = (int) tmpRectf.top;
				
				vertices[1].x = (int) (mRect.centerX() + pointerSize/2);
				vertices[1].y = (int) tmpRectf.top;
				
				vertices[2].x = (int) (mRect.centerX());
				vertices[2].y = (int) tmpRectf.bottom;
				
			} 
			break;
			case TOP: {
				vertices[0].x = (int) (mRect.centerX());
				vertices[0].y = (int) tmpRectf.top;
				
				vertices[1].x = (int) (mRect.centerX() + pointerSize/2);
				vertices[1].y = (int) tmpRectf.bottom;
				
				vertices[2].x = (int) (mRect.centerX() - pointerSize/2);
				vertices[2].y = (int) tmpRectf.bottom;
			} 
			break;
			case LEFT: {
				vertices[0].x = (int) (tmpRectf.left);
				vertices[0].y = (int) tmpRectf.centerY();
				
				vertices[1].x = (int) tmpRectf.right;
				vertices[1].y = (int) tmpRectf.centerY() - pointerSize/2;
				
				vertices[2].x = (int) tmpRectf.right;
				vertices[2].y = (int) tmpRectf.centerY() + pointerSize/2;
			} 
			break;
			case RIGHT: {
				vertices[0].x = (int) (tmpRectf.right);
				vertices[0].y = (int) tmpRectf.centerY();
				
				vertices[1].x = (int) tmpRectf.left;
				vertices[1].y = (int) tmpRectf.centerY() - pointerSize/2;
				
				vertices[2].x = (int) tmpRectf.left;
				vertices[2].y = (int) tmpRectf.centerY() + pointerSize/2;
			} 
			break;
				
		}
		
	}

	private void drawArrows(Point[] point, Canvas canvas, Paint paint) {

		float [] points  = new float[8];

		points[0] = point[0].x;

		points[1] = point[0].y;

		points[2] = point[1].x;

		points[3] = point[1].y;  

		points[4] = point[2].x;

		points[5] = point[2].y;


		points[6] = point[0].x;

		points[7] = point[0].y;

		canvas.drawVertices(VertexMode.TRIANGLES, 8, points, 0, null, 0, null, 0, null, 0, 0, paint);
		Path path = new Path();
		path.moveTo(point[0].x , point[0].y);
		path.lineTo(point[1].x,point[1].y);
		path.lineTo(point[2].x,point[2].y);
		canvas.drawPath(path,paint);

	}







}
