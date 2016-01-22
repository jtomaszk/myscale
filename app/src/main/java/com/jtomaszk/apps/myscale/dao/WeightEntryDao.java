package com.jtomaszk.apps.myscale.dao;

import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.util.List;

/**
 * Created by jtomaszk on 22.01.16.
 */
public interface WeightEntryDao {
    List<WeightEntry> getAllSorted();

    List<WeightEntry> findNotSynced();

    void save(List<WeightEntry> list);

    WeightEntry findByDateTimeMillisecons(long time);

    void addIfNotMatchedFromGooleFit(long time, float weight, String appName);

    void addFromUser(long time, float weight);

    void deleteAll();
}
