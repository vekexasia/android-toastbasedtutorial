package com.andreabaccega.toastutorials;

import com.andreabaccega.toastutorials.ToastView.ArrowPosition;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class ToastBuilder {

	private static int getStatusBarHeight(Context ctx) {
		return (int) Math.ceil(25 * ctx.getResources().getDisplayMetrics().density);
	}

	@SuppressLint("NewApi")
	private static Point getDisplaySize(Display display) {
		Point size = new Point();
		if (Build.VERSION.SDK_INT < 13) {
			size.x = display.getWidth();
			size.y = display.getHeight();
		} else {
			display.getSize(size);
		}

		return size;
	}
	
	public static Toast xYpointerToast(Context ctx, ToastView.ArrowPosition pos, int x, int y, String text) {
		Toast toRet = new Toast(ctx);
		toRet.setDuration(Toast.LENGTH_LONG);
		

		ToastView v = new ToastView(ctx, pos);
		v.setText(text);


		toRet.setView(v);
		int gravity;
		if (pos == ArrowPosition.LEFT) {
			gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
		} else if (pos == ArrowPosition.TOP) {
			gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		} else if (pos == ArrowPosition.RIGHT) {
			gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		} else  {
			gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		}
		toRet.setGravity(gravity, x, y);

		return toRet;
	}


	public static Toast bottomPointerToast(Context ctx, View viewToPoint, String text) {

		int[] viewLocation = new int[2];
		viewToPoint.getLocationOnScreen(viewLocation);

		Point displaySize = getDisplaySize(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());

		return xYpointerToast(ctx, ArrowPosition.BOTTOM, (viewLocation[0]+viewToPoint.getWidth()/2)-displaySize.x/2, displaySize.y- viewLocation[1], text);
	}
	
	public static Toast topPointerToast(Context ctx, View viewToPoint, String text) {
		int[] viewLocation = new int[2];
		viewToPoint.getLocationOnScreen(viewLocation);

		Point displaySize = getDisplaySize(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());

		return xYpointerToast(ctx, ArrowPosition.TOP, (viewLocation[0]+viewToPoint.getWidth()/2)-displaySize.x/2, viewLocation[1]+viewToPoint.getHeight() - getStatusBarHeight(ctx), text);
	}

	public static Toast leftPointerToast(Context ctx, View viewToPoint, String text) {
		int[] viewLocation = new int[2];
		viewToPoint.getLocationOnScreen(viewLocation);

		Point displaySize = getDisplaySize(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());

		return xYpointerToast(ctx, ArrowPosition.LEFT, viewLocation[0] + viewToPoint.getWidth(), viewLocation[1] + viewToPoint.getHeight()/2 - getStatusBarHeight(ctx)/2 - displaySize.y/2, text);
	}

	public static Toast rightPointerToast(Context ctx, View viewToPoint, String text) {
		int[] viewLocation = new int[2];
		viewToPoint.getLocationOnScreen(viewLocation);

		Point displaySize = getDisplaySize(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());

		
		return xYpointerToast(ctx, ArrowPosition.RIGHT, displaySize.x - viewLocation[0], viewLocation[1] + viewToPoint.getHeight()/2 - getStatusBarHeight(ctx)/2 - displaySize.y/2, text);
	}

}
