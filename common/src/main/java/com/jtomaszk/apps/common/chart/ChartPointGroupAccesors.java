package com.jtomaszk.apps.common.chart;

import com.google.common.base.Function;

/**
 * Created by jtomaszk on 16.12.15.
 */
public class ChartPointGroupAccesors {

    public static Function<ChartPointGroup, Float> avg() {
        return new Function<ChartPointGroup, Float>() {
            @Override
            public Float apply(ChartPointGroup input) {
                return input.getAvg();
            }
        };
    }

    public static Function<ChartPointGroup, Float> min() {
        return new Function<ChartPointGroup, Float>() {
            @Override
            public Float apply(ChartPointGroup input) {
                return input.getMin();
            }
        };
    }

    public static Function<ChartPointGroup, Float> max() {
        return new Function<ChartPointGroup, Float>() {
            @Override
            public Float apply(ChartPointGroup input) {
                return input.getMax();
            }
        };
    }
}
