package com.jtomaszk.apps.myscale.dao;

import com.jtomaszk.apps.myscale.entity.DataSource;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.preferences.AppConst;
import com.jtomaszk.apps.myscale.utils.WeightUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by jarema-user on 2015-11-29.
 */
public class WeightEntryDaoImpl implements WeightEntryDao {

    @Override
    public List<WeightEntry> getAllSorted() {
        List<WeightEntry> ret = WeightEntry.find(WeightEntry.class,
                null, null, null, "date_time_milliseconds", null);

        Collections.sort(ret, WeightUtil.WEIGHT_ENTRY_COMPARATOR);
        return ret;
    }

    @Override
    public List<WeightEntry> findNotSynced() {
        return WeightEntry.find(WeightEntry.class, "synced = 0");
    }

    @Override
    public void save(List<WeightEntry> list) {
        WeightEntry.saveInTx(list);
    }

    @Override
    public WeightEntry findByDateTimeMillisecons(long time) {
        List<WeightEntry> result = WeightEntry.find(WeightEntry.class, "date_time_milliseconds = ?", String.valueOf(time));
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void addIfNotMatchedFromGooleFit(long time, float weight, String appName) {
        WeightEntry entry = findByDateTimeMillisecons(time);
        if (entry == null) {
            addFromGooleFit(time, weight, appName);
        }
    }

    public void addFromGooleFit(long time, float weight, String appName) {
        WeightEntry entry = new WeightEntry(weight, time, DataSource.GOOGLE_FIT, true, appName);
        entry.save();
    }

    @Override
    public void addFromUser(long time, float weight) {
        WeightEntry entry = new WeightEntry(weight, time, DataSource.USER, AppConst.APP_PACKAGE.getValue());
        entry.save();
    }


    @Override
    public void deleteAll() {
        WeightEntry.deleteAll(WeightEntry.class);
    }
}
