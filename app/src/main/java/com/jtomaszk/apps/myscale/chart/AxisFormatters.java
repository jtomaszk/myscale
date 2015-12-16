package com.jtomaszk.apps.myscale.chart;

/**
 * Created by jtomaszk on 16.12.15.
 */
public class AxisFormatters {
    public static final DateAxisFormatter DATE_AXIS_FORMATTER = new DateAxisFormatter();

    public static DateAxisFormatter dateFormatter() {
        return DATE_AXIS_FORMATTER;
    }
}
