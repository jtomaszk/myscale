package com.jtomaszk.apps.common.chart;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.ValueFormatterHelper;
import lecho.lib.hellocharts.model.AxisValue;

/**
 * Created by jtomaszk on 21.12.15.
 */
public abstract class SimpleAxisValueFormatter implements AxisValueFormatter {

    private ValueFormatterHelper valueFormatterHelper = new ValueFormatterHelper();

    @Override
    public int formatValueForManualAxis(char[] formattedValue, AxisValue axisValue) {
        String convertValue = convertValue(axisValue.getValue());
        return formatValue(formattedValue, convertValue);
    }

    protected abstract String convertValue(float value);

    public int formatValue(char[] formattedValue, String convertValue) {
        char[] text = convertValue.toCharArray();
        valueFormatterHelper.setAppendedText(text);
        valueFormatterHelper.appendText(formattedValue);
        return text.length;
    }

    @Override
    public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int autoDecimalDigits) {
        String convertValue = convertValue(value);
        return formatValue(formattedValue, convertValue);
    }
}
