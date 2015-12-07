package com.jtomaszk.apps.myscale.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jtomaszk on 07.12.15.
 */
public class PreferenceUtil {

    public static int readInt(MyScalePrefs pref, Integer defaultValue, Context context) {
        return Integer.valueOf(readString(pref, defaultValue.toString(), context));
    }

    public static String readString(MyScalePrefs pref, String defaultValue, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(pref.getPrefName(), defaultValue);
    }

    public static long readDateInMillis(MyScalePrefs pref, Long defaultValue, Context context) {
        return Long.valueOf(readString(pref, defaultValue.toString(), context));
    }

    public static void writeDateInMillis(MyScalePrefs pref, Long value, Context context) {
        writeString(pref, value.toString(), context);
    }

    public static void writeInt(MyScalePrefs pref, Integer value, Context context) {
        writeString(pref, value.toString(), context);
    }

    public static void writeString(MyScalePrefs pref, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(pref.getPrefName(), value).apply();
    }
}
