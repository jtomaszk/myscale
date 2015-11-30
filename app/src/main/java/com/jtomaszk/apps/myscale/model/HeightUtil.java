package com.jtomaszk.apps.myscale.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jtomaszk on 30.11.15.
 */
public class HeightUtil {

    public static int readHeight(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.valueOf(prefs.getString(MyScalePrefs.HEIGHT.getPrefName(), "180"));
    }
}
