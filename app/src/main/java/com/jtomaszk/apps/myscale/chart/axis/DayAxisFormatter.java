package com.jtomaszk.apps.myscale.chart.axis;

import com.jtomaszk.apps.common.chart.SimpleAxisValueFormatter;
import com.jtomaszk.apps.common.utils.DateUtil;

/**
 * Created by jtomaszk on 15.12.15.
 */
public class DayAxisFormatter extends SimpleAxisValueFormatter {

    private long convertValueToDays(float value) {
        return Float.valueOf(value).longValue();
    }

    @Override
    protected String convertValue(float value) {
        long days = convertValueToDays(value);
        return DateUtil.daysToShortStringWithoutYears(days);
    }
}
