package com.jtomaszk.apps.myscale.dao;

import com.jtomaszk.apps.myscale.entity.DataSource;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.util.List;

/**
 * Created by jarema-user on 2015-11-29.
 */
public class WeightEntryDao {

    public List<WeightEntry> getAll() {
        return WeightEntry.listAll(WeightEntry.class);
    }

    public WeightEntry findByHash(int hash) {
        List<WeightEntry> result = WeightEntry.find(WeightEntry.class, "hash = ?", String.valueOf(hash));
        if (result.isEmpty()) {
            return null;
        } else if (result.size() > 1) {
            throw new RuntimeException("not unique hash!");
        } else {
            return result.get(0);
        }
    }

    public void addIfNotMatchedFromGooleFit(long time, int hash, float weight) {
        WeightEntry entry = findByHash(hash);
        if (entry == null) {
            addFromGooleFit(time, hash, weight);
        }
    }

    public void addFromGooleFit(long time, int hash, float weight) {
        WeightEntry entry = new WeightEntry();
        entry.setDataSource(DataSource.GOOGLE_FIT);
        entry.setDateTimeMilliseconds(time);
        entry.setHash(hash);
        entry.setWeight(weight);
        entry.save();
    }
}
