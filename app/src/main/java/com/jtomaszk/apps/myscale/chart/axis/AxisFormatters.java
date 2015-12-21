package com.jtomaszk.apps.myscale.chart.axis;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;

/**
 * Created by jtomaszk on 16.12.15.
 */
public class AxisFormatters {

    public static AxisValueFormatter dayFormatter() {
        return new DayAxisFormatter();
    }

    public static AxisValueFormatter monthFormatter() {
        return new MonthAxisFormatter();
    }

    public static AxisValueFormatter weekFormatter() {
        return new WeekAxisFormatter();
    }
}
