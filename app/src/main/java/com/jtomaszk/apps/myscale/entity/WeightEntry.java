package com.jtomaszk.apps.myscale.entity;

import com.orm.SugarRecord;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jtomaszk on 27.11.15.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@RequiredArgsConstructor(suppressConstructorProperties = true)
@ToString
public class WeightEntry extends SugarRecord<WeightEntry> {
    @NonNull @Setter @Getter
    Float weight;
    @NonNull @Getter @Setter
    Long dateTimeMilliseconds;
    @NonNull @Setter @Getter
    DataSource dataSource;
    @Setter @Getter
    boolean synced;
    @NonNull @Setter @Getter
    String appName;

    public long getDays() {
        return TimeUnit.MILLISECONDS.toDays(dateTimeMilliseconds);
    }

}
