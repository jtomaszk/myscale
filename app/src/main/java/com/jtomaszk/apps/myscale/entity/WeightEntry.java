package com.jtomaszk.apps.myscale.entity;

import com.orm.SugarRecord;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jtomaszk on 27.11.15.
 */
public class WeightEntry extends SugarRecord<WeightEntry> {
    @Setter @Getter
    float weight;
    @Getter
    long days;
    @Getter
    long dateTimeMilliseconds;
    @Setter @Getter
    boolean synced;
    @Setter @Getter
    int hash;
    @Setter @Getter
    DataSource dataSource;

    public void setDateTimeMilliseconds(long dateTimeMilliseconds) {
        this.dateTimeMilliseconds = dateTimeMilliseconds;
        this.days = TimeUnit.MILLISECONDS.toDays(dateTimeMilliseconds);
    }

    @Override
    public String toString() {
        return "WeightEntry{" +
                "weight=" + weight +
                ", days=" + days +
                ", dateTimeMilliseconds=" + dateTimeMilliseconds +
                ", synced=" + synced +
                ", hash=" + hash +
                ", dataSource=" + dataSource +
                '}';
    }
}
