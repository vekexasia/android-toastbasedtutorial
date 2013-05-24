package com.andreabaccega.toastutorials;

import android.content.Context;

public interface IToastIdentifier {
	/**
	 * Should return the unique identifier of this.
	 * @return
	 */
	public int getId();
	/**
	 * Should return the maximum number of views that this instance should have
	 * @return
	 */
	public int getMaxViews();
	
	/**
	 * Should return the text. of the baloon
	 * @return
	 */
	public String getText(Context ctx);
}
