package com.jtomaszk.apps.myscale.chart;

import com.jtomaszk.apps.common.DateUtil;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.ValueFormatterHelper;
import lecho.lib.hellocharts.model.AxisValue;

/**
 * Created by jtomaszk on 15.12.15.
 */
public class DateAxisFormatter implements AxisValueFormatter {

    private ValueFormatterHelper valueFormatterHelper = new ValueFormatterHelper();

    @Override
    public int formatValueForManualAxis(char[] formattedValue, AxisValue axisValue) {
        long days = convertValueToDays(axisValue.getValue());
        return formatValue(formattedValue, days);
    }

    public int formatValue(char[] formattedValue, long days) {
        char[] text = DateUtil.daysToShortStringWithoutYears(days).toCharArray();
        valueFormatterHelper.setAppendedText(text);
        valueFormatterHelper.appendText(formattedValue);
        return text.length;
    }

    @Override
    public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int autoDecimalDigits) {
        long days = convertValueToDays(value);
        return formatValue(formattedValue, days);
    }

    public long convertValueToDays(float value) {
        return Float.valueOf(value).longValue();
    }

}