package com.jtomaszk.apps.myscale.model;

import lombok.Getter;

/**
 * Created by jtomaszk on 30.11.15.
 */
public enum MyScalePrefs {
    HEIGHT("pref_height"),
    LAST_WEIGHT_SYNC("last_weight_sync");

    @Getter
    private String prefName;

    MyScalePrefs(String prefName) {
        this.prefName = prefName;
    }
}
