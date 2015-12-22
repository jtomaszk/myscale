package com.jtomaszk.apps.myscale.chart.axis;

import com.jtomaszk.apps.common.chart.SimpleAxisValueFormatter;
import com.jtomaszk.apps.common.utils.DateUtil;

import static com.jtomaszk.apps.myscale.chart.axis.AxisFormatterHelper.convertValueToMilliseconds;

/**
 * Created by jtomaszk on 15.12.15.
 */
public class WeekAxisFormatter extends SimpleAxisValueFormatter {

    @Override
    protected String convertValue(float value) {
        long days = convertValueToMilliseconds(value);
        return String.valueOf(DateUtil.millisecondsToWeek(days));
    }
}
