package com.jtomaszk.apps.myscale.entity;

import com.orm.SugarRecord;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jtomaszk on 27.11.15.
 */
public class WeightEntry extends SugarRecord<WeightEntry> {
    @Setter @Getter
    float weight;
    @Setter @Getter
    long dateMiliseconds;
    @Setter @Getter
    boolean sync;
    @Setter @Getter
    int hash;


}
