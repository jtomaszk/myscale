package com.jtomaszk.apps.myscale.preferences;

import lombok.Getter;

/**
 * Created by jtomaszk on 02.12.15.
 */
public enum AppConst {
    APP_PACKAGE("com.jtomaszk.apps.myscale"),
    ;

    @Getter
    private final String value;

    AppConst(String value) {
        this.value = value;
    }
}
