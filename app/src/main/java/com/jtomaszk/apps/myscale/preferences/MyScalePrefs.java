package com.jtomaszk.apps.myscale.preferences;

import com.jtomaszk.apps.common.ApplicationPref;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by jtomaszk on 30.11.15.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public enum MyScalePrefs implements ApplicationPref {
    HEIGHT("pref_height"),
    HEIGHT_DATE("pref_height_date"),
    LAST_WEIGHT_SYNC("last_weight_sync");

    @Getter
    private String prefName;

}
