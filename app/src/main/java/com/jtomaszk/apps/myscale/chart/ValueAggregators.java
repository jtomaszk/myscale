package com.jtomaszk.apps.myscale.chart;

import android.support.annotation.NonNull;

import com.google.common.base.Function;
import com.jtomaszk.apps.common.chart.ChartPoint;

/**
 * Created by jtomaszk on 16.12.15.
 */
public class ValueAggregators {

    @NonNull
    public static Function<ChartPoint, Float> dayAggregator() {
        return new Function<ChartPoint, Float>() {
            @Override
            public Float apply(ChartPoint input) {
                return input.getX();
            }
        };
    }

    @NonNull
    public static Function<ChartPoint, Float> weekAggregator() {
        return new Function<ChartPoint, Float>() {
            @Override
            public Float apply(ChartPoint input) {
                return (float) Math.floor(input.getX() / 7) * 7f;
            }
        };
    }

    @NonNull
    public static Function<ChartPoint, Float> monthAggregator() {
        return new Function<ChartPoint, Float>() {
            @Override
            public Float apply(ChartPoint input) {
                return (float) Math.floor(input.getX() / 30) * 30f; //TODO żle
            }
        };
    }
}
