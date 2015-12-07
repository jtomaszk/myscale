package com.jtomaszk.apps.myscale.model;

import lombok.Getter;

/**
 * Created by jtomaszk on 30.11.15.
 */
public enum MyScalePrefs {
    HEIGHT("pref_height"),
    HEIGHT_DATE("pref_height_date"),
    LAST_WEIGHT_SYNC("last_weight_sync");

    @Getter
    private String prefName;

    MyScalePrefs(String prefName) {
        this.prefName = prefName;
    }
}
