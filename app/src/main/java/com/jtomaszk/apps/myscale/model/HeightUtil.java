package com.jtomaszk.apps.myscale.model;

import android.content.Context;

/**
 * Created by jtomaszk on 30.11.15.
 */
public class HeightUtil {

    public static int readHeight(Context context) {
        return PreferenceUtil.readInt(MyScalePrefs.HEIGHT, 180, context);
    }

    public static void writeHeight(Integer height, Long timeInMillis, Context context) {
        writeHeightDate(timeInMillis, context);
        PreferenceUtil.writeInt(MyScalePrefs.HEIGHT, height, context);
    }

    private static void readHeightDate(Long defaultValue, Context context) {
        PreferenceUtil.readDateInMillis(MyScalePrefs.HEIGHT_DATE, defaultValue, context);
    }

    private static void writeHeightDate(Long timeInMillis, Context context) {
        PreferenceUtil.writeDateInMillis(MyScalePrefs.HEIGHT_DATE, timeInMillis, context);
    }
}
