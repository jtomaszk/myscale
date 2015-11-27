package com.jtomaszk.apps.myscale.model;

import java.math.BigDecimal;

/**
 * Created by jtomaszk on 27.11.15.
 */
public class BMIModel {
    private int height;
    private float weight;

    public BMIModel(int height, float weight) {
        this.height = height;
        this.weight = weight;
    }

    public BMI bmi() {
        return new BMI(round(weight / (height / 100.0 * height / 100.0), 2));
    }

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(double d, int decimalPlace) {
        BigDecimal bd = BigDecimal.valueOf(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
