package com.jtomaszk.apps.myscale.model;

import lombok.Getter;

/**
 * Created by jtomaszk on 27.11.15.
 */
public enum BMICategory {
    VERY_SEVERELY_UNDERWEIGHT(0, 15),
    SEVERELY_UNDERWEIGHT(15, 16),
    UNDERWEIGHT(16, 18.5f),
    NORMAL(18.5f, 25),
    OVERWEIGHT(25, 30),
    MODERATELY_OBESE(30, 35),
    SEVERELY_OBESE(35, 40),
    VERY_SEVERELY_OBESE(40, 1000),
    ;

    @Getter
    private final float from;
    @Getter
    private final float to;

    BMICategory(float from, float to) {
        this.from = from;
        this.to = to;
    }

    public static BMICategory findByBMIValue(float bmi) {
        for (BMICategory cat : BMICategory.values()) {
            if (cat.getFrom() < bmi && cat.getTo() > bmi) {
                return cat;
            }
        }
        throw new RuntimeException("BMI out of range! " + bmi);
    }
}
