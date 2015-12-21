package com.jtomaszk.apps.myscale.chart.aggregator;

import android.support.annotation.NonNull;

import com.google.common.base.Function;
import com.jtomaszk.apps.common.chart.ChartPoint;

/**
 * Created by jtomaszk on 16.12.15.
 */
public class ValueAggregator {

    @NonNull
    public static Function<ChartPoint, Float> dayAggregator() {
        return new DayAggregator();
    }

    @NonNull
    public static Function<ChartPoint, Float> weekAggregator() {
        return new WeekAggregator();
    }

    @NonNull
    public static Function<ChartPoint, Float> monthAggregator() {
        return new MonthAggregator();
    }
}
