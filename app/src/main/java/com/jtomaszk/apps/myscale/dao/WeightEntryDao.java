package com.jtomaszk.apps.myscale.dao;

import com.jtomaszk.apps.myscale.entity.DataSource;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.util.List;

/**
 * Created by jarema-user on 2015-11-29.
 */
public class WeightEntryDao {

    public List<WeightEntry> getAll() {
        return WeightEntry.find(WeightEntry.class,
                null, null, null, "date_time_milliseconds", null);
    }

    public List<WeightEntry> findNotSynced() {
        return WeightEntry.find(WeightEntry.class, "synced = 0");
    }

    public void save(List<WeightEntry> list) {
        WeightEntry.saveInTx(list);
    }

    public WeightEntry findByDateTimeMillisecons(long time) {
        List<WeightEntry> result = WeightEntry.find(WeightEntry.class, "date_time_milliseconds = ?", String.valueOf(time));
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public void addIfNotMatchedFromGooleFit(long time, float weight) {
        WeightEntry entry = findByDateTimeMillisecons(time);
        if (entry == null) {
            addFromGooleFit(time, weight);
        }
    }

    public void addFromGooleFit(long time, float weight) {
        WeightEntry entry = new WeightEntry(weight, time, DataSource.GOOGLE_FIT, true, "");
        entry.save();
    }

    public void addFromUser(long time, float weight) {
        WeightEntry entry = new WeightEntry(weight, time, DataSource.USER);
        entry.save();
    }

    public void addFromImport(long time, float weight) {
        WeightEntry entry = new WeightEntry(weight, time, DataSource.IMPORT);
        entry.save();
    }

    public void deleteAll() {
        WeightEntry.deleteAll(WeightEntry.class);
    }
}
