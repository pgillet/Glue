package com.glue.client.android.utils;

import java.util.Arrays;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

public class Utils {

	/**
	 * Set the enabled state of all the views within the given ViewGroup
	 * recursively. The views with the given ids will be ignored.
	 * 
	 * @param enabled
	 * @param vg
	 */
	public static void setEnabled(boolean enabled, ViewGroup vg,
			List<Integer> ids) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);

			if (!ids.contains(child.getId())) {
				child.setEnabled(enabled);
				if (child instanceof ViewGroup) {
					setEnabled(enabled, (ViewGroup) child, ids);
				}
			}
		}
	}

	/**
	 * @see #setEnabled(boolean, ViewGroup, List)
	 */
	public static void setEnabled(boolean enabled, ViewGroup vg, Integer... ids) {
		setEnabled(enabled, vg, Arrays.asList(ids));
	}
}
