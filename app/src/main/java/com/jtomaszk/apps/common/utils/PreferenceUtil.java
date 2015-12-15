package com.jtomaszk.apps.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jtomaszk.apps.common.ApplicationPref;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by jtomaszk on 07.12.15.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreferenceUtil {

    public static int readInt(ApplicationPref pref, Integer defaultValue, Context context) {
        return Integer.valueOf(readString(pref, defaultValue.toString(), context));
    }

    public static String readString(ApplicationPref pref, String defaultValue, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(pref.getPrefName(), defaultValue);
    }

    public static long readTimeInMillis(ApplicationPref pref, Long defaultValue, Context context) {
        return Long.valueOf(readString(pref, defaultValue.toString(), context));
    }

    public static void writeTimeInMillis(ApplicationPref pref, Long value, Context context) {
        writeString(pref, value.toString(), context);
    }

    public static void writeInt(ApplicationPref pref, Integer value, Context context) {
        writeString(pref, value.toString(), context);
    }

    public static void writeString(ApplicationPref pref, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(pref.getPrefName(), value).apply();
    }
}
