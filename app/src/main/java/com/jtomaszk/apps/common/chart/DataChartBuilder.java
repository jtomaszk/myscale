package com.jtomaszk.apps.common.chart;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by jtomaszk on 15.12.15.
 */
public class DataChartBuilder {
    private List<Line> lines = Lists.newArrayList();
    private Axis axisX;
    private Axis axisY;

    public DataChartBuilder() {
    }

    public DataChartBuilder addLineGroupBy(List<ChartPoint> list,
                                           Function<ChartPoint, Float> groupFunction,
                                           Function<ChartPointGroup, Float> valueFunction) {

        Map<Float, ChartPointGroup> groupMap = groupListBy(list, groupFunction);

        List<PointValue> values = Lists.newArrayListWithCapacity(groupMap.values().size());

        for (ChartPointGroup i : groupMap.values()) {
            Float x = i.getGroupValue();
            Float y = valueFunction.apply(i);
            values.add(createPoint(x, y));
        }

        lines.add(createLine(values));
        return this;
    }

    @NonNull
    private Map<Float, ChartPointGroup> groupListBy(List<ChartPoint> list, Function<ChartPoint, Float> groupFunction) {
        Map<Float, ChartPointGroup> groupMap = Maps.newLinkedHashMap();

        for (ChartPoint cp : list) {
            Float groupValue = groupFunction.apply(cp);
            Preconditions.checkNotNull(groupValue);
            if (!groupMap.containsKey(groupValue)) {
                groupMap.put(groupValue, new ChartPointGroup(groupValue));
            }
            groupMap.get(groupValue).addPoint(cp);
        }
        return groupMap;
    }

    @NonNull
    private PointValue createPoint(Float x, Float y) {
        Preconditions.checkNotNull(x);
        Preconditions.checkNotNull(y);
        return new PointValue(x, y);
    }

    private Line createLine(List<PointValue> values) {
        return new Line(values).setColor(Color.BLUE).setCubic(true).setHasPoints(true);
    }

    public DataChartBuilder addLine(List<ChartPoint> list) {
        List<PointValue> values = Lists.newArrayList();

        for (ChartPoint entry : list) {
            float x = entry.getX();
            float y = entry.getY();
            values.add(createPoint(x, y));
        }

        lines.add(createLine(values));
        return this;
    }

    public DataChartBuilder addAxisX() {
        return addAxisX(null);
    }

    public DataChartBuilder addAxisX(AxisValueFormatter formatter) {
        axisX = new Axis()
                .setAutoGenerated(true)
                .setFormatter(formatter);
        return this;
    }

    public DataChartBuilder addAxisY() {
        axisY = new Axis()
                .setAutoGenerated(true)
                .setHasSeparationLine(true);
        return this;
    }

    public LineChartData getData() {
        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

}
