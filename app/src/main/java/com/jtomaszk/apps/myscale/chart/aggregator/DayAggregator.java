package com.jtomaszk.apps.myscale.chart.aggregator;

import com.google.common.base.Function;
import com.jtomaszk.apps.common.chart.ChartPoint;

/**
 * Created by jtomaszk on 21.12.15.
 */
public class DayAggregator implements Function<ChartPoint, Float> {

    @Override
    public Float apply(ChartPoint input) {
        return input.getX();
    }
}
