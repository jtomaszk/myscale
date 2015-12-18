package com.jtomaszk.apps.common.chart;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collection;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by jtomaszk on 16.12.15.
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class ChartPointGroup {

    @Getter
    @NonNull
    private Float groupValue;
    @Getter
    private float min = Float.MAX_VALUE;
    @Getter
    private float max = Float.MIN_VALUE;
    @Getter
    private float sum = 0;
    @Getter
    private int count = 0;

    private Collection<ChartPoint> points = Lists.newLinkedList();

    public void addPoint(ChartPoint point) {
        points.add(point);
        Float value = point.getY();
        addValue(value);
    }

    public float getAvg() {
        return sum / (float) count;
    }

    private void addValue(Float value) {
        Preconditions.checkNotNull(value);

        count++;
        sum += value;

        if (value > max) {
            max = value;
        }
        if (value < min) {
            min = value;
        }
    }
}
