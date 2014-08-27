package de.canberkdemirkan.mediaboxmngr.util;

import android.content.Context;

public class AppContextUtil {

	private static Context sContext;

	public static Context getContext() {
		return sContext;
	}

	public static void setContext(Context appContext) {
		if (sContext == null)
			sContext = appContext;
	}

}