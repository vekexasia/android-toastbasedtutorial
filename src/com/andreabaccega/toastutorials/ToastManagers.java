package com.andreabaccega.toastutorials;

import com.andreabaccega.toastutorials.ToastView.ArrowPosition;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Toast;

public class ToastManagers {
	private static final String SP_NAME = "tut_toasts";
	
	
	private final SharedPreferences sp;
	private final int defaultMaxViews;
	public ToastManagers(Context ctx) {
		this(ctx, 3);
	}
	
	public ToastManagers(Context ctx, int defaultMaxViews) {
		sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		this.defaultMaxViews = defaultMaxViews;
	}
	
	private String getSpToastKey(int idToShow) {
		return String.format("views_%d",idToShow);
	}
	
	public boolean isGoingToBeShown(int idToShow) {
		return isGoingToBeShown(idToShow, defaultMaxViews);
	}
	
	public boolean isGoingToBeShown(int idToShow, int maxViews) {
		return sp.getInt(getSpToastKey(idToShow), 0) < maxViews;
	}

	
	
	private void doIncrement(int idHelp) {
		int curValue = sp.getInt(getSpToastKey(idHelp), 0);
		curValue++;
		Editor e = sp.edit();
		e.putInt(getSpToastKey(idHelp), curValue);
		e.commit();
	}
	
	
	public Toast showHelpToast( Context ctx, int idHelp, String helpText, ToastView.ArrowPosition pos, View viewToBind ) {
		return showHelpToast(ctx, idHelp, defaultMaxViews, helpText, pos, viewToBind);
	}
	
	public Toast showHelpToast( Context ctx, int idHelp, int maxViews, String helpText, ToastView.ArrowPosition pos, View viewToBind ) {
		final Toast toRet;
		if ( isGoingToBeShown(idHelp, maxViews) ) {
			if (pos == ArrowPosition.LEFT) {
				toRet = ToastBuilder.leftPointerToast(ctx, viewToBind, helpText);
			} else if (pos == ArrowPosition.TOP) {
				toRet = ToastBuilder.topPointerToast(ctx, viewToBind, helpText);
			} else if (pos == ArrowPosition.RIGHT) {
				toRet = ToastBuilder.rightPointerToast(ctx, viewToBind, helpText);
			} else {
				// BOTTOM
				toRet = ToastBuilder.bottomPointerToast(ctx, viewToBind, helpText);
			}
			
			toRet.show();
			doIncrement(idHelp);
			
		} else {
			toRet = null;
		}
		return toRet;
	}
	
	public Toast showHelpToast( Context ctx, int idHelp, String helpText, ToastView.ArrowPosition pos, int x, int y ) {
		return showHelpToast(ctx, idHelp, defaultMaxViews, helpText, pos, x, y);
	}
	
	public Toast showHelpToast( Context ctx, int idHelp, int maxViews, String helpText, ToastView.ArrowPosition pos, int x, int y ) {
		final Toast toRet;
		if ( isGoingToBeShown(idHelp, maxViews) ) {
			toRet = ToastBuilder.xYpointerToast(ctx, pos, x, y, helpText);
			
			toRet.show();
			doIncrement(idHelp);
			
		} else {
			toRet = null;
		}
		return toRet;
	}
	

	
}
