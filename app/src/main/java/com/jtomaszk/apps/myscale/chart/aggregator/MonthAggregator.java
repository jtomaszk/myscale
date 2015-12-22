package com.jtomaszk.apps.myscale.chart.aggregator;

import com.google.common.base.Function;
import com.jtomaszk.apps.common.chart.ChartPoint;
import com.jtomaszk.apps.common.utils.DateUtil;

/**
 * Created by jtomaszk on 21.12.15.
 */
public class MonthAggregator implements Function<ChartPoint, Float> {

    @Override
    public Float apply(ChartPoint input) {
        long val = Float.valueOf(input.getX()).longValue();
        return DateUtil.truncateMillisecondsToMonth(val).floatValue();
    }
}
