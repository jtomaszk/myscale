package com.jtomaszk.apps.myscale.utils;

import android.content.Context;

import com.jtomaszk.apps.common.PreferenceUtil;
import com.jtomaszk.apps.myscale.preferences.MyScalePrefs;

/**
 * Created by jtomaszk on 30.11.15.
 */
public class HeightUtil {

    public static int readHeight(Context context) {
        return PreferenceUtil.readInt(MyScalePrefs.HEIGHT, 180, context);
    }

    public static void writeHeight(Integer height, Long timeInMillis, Context context) {
        writeHeightTime(timeInMillis, context);
        PreferenceUtil.writeInt(MyScalePrefs.HEIGHT, height, context);
    }

    public static long readHeightTime(Context context) {
        return PreferenceUtil.readTimeInMillis(MyScalePrefs.HEIGHT_DATE, 0L, context);
    }

    private static void writeHeightTime(Long timeInMillis, Context context) {
        PreferenceUtil.writeTimeInMillis(MyScalePrefs.HEIGHT_DATE, timeInMillis, context);
    }
}
