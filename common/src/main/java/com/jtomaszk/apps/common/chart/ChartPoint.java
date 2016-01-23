package com.jtomaszk.apps.common.chart;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by jtomaszk on 15.12.15.
 */
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode
public class ChartPoint implements Serializable {
    @Getter
    @Setter
    private float x;
    @Getter
    @Setter
    private float y;

}
