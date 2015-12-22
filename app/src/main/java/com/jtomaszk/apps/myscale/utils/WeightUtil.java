package com.jtomaszk.apps.myscale.utils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.jtomaszk.apps.common.chart.ChartPoint;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.util.Comparator;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by jtomaszk on 15.12.15.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WeightUtil {

    public static final Function<WeightEntry, ChartPoint> CHART_POINT_CONVERTER = new Function<WeightEntry, ChartPoint>() {
        @Override
        public ChartPoint apply(WeightEntry input) {
            return new ChartPoint(input.getDateTimeMilliseconds(), input.getWeight());
        }
    };
    public static final Comparator<WeightEntry> WEIGHT_ENTRY_COMPARATOR = new Comparator<WeightEntry>() {
        @Override
        public int compare(WeightEntry lhs, WeightEntry rhs) {
            return lhs.getDateTimeMilliseconds().compareTo(rhs.getDateTimeMilliseconds());
        }
    };

    public static List<ChartPoint> simpleTransform(List<WeightEntry> list) {
        return Lists.newArrayList(Collections2.transform(list, CHART_POINT_CONVERTER));
    }

    public static List<ChartPoint> simpleTransformAggregateByDay(List<WeightEntry> list) {
        return null;
    }
}
