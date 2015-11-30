package com.jtomaszk.apps.myscale.model;

import lombok.Getter;

/**
 * Created by jtomaszk on 27.11.15.
 */
public class BMI {
    @Getter
    private float bmi;

    public BMI(float bmi) {
        this.bmi = bmi;
    }

    public BMICategory getCategory() {
        return BMICategory.findByBMIValue(bmi);
    }
}
