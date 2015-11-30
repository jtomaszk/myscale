package com.jtomaszk.apps.myscale.dao;

import com.jtomaszk.apps.myscale.entity.DataSource;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.util.List;

/**
 * Created by jarema-user on 2015-11-29.
 */
public class WeightEntryDao {

    public List<WeightEntry> getAll() {
//        return WeightEntry.listAll(WeightEntry.class);
        return WeightEntry.find(WeightEntry.class,
                null, null, null, "date_time_milliseconds", null);
    }

    public List<WeightEntry> findNotSynced() {
        return WeightEntry.find(WeightEntry.class, "synced = 0");
    }

    public void save(List<WeightEntry> list) {
        WeightEntry.saveInTx(list);
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

    public WeightEntry findByDateTimeMillisecons(long time) {
        List<WeightEntry> result = WeightEntry.find(WeightEntry.class, "date_time_milliseconds = ?", String.valueOf(time));
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public void addIfNotMatchedFromGooleFit(long time, int hash, float weight) {
        WeightEntry entry = findByDateTimeMillisecons(time);
        if (entry == null) {
            addFromGooleFit(time, hash, weight);
        }
    }

    public void addFromGooleFit(long time, int hash, float weight) {
        WeightEntry entry = new WeightEntry();
        entry.setDataSource(DataSource.GOOGLE_FIT);
        entry.setDateTimeMilliseconds(time);
        entry.setHash(hash);
        entry.setSynced(true);
        entry.setWeight(weight);
        entry.save();
    }

    public void addFromUser(long time, float weight) {
        WeightEntry entry = new WeightEntry();
        entry.setDataSource(DataSource.USER);
        entry.setDateTimeMilliseconds(time);
        entry.setWeight(weight);
        entry.save();
    }

    public void deleteAll() {
        WeightEntry.deleteAll(WeightEntry.class);
    }
}
