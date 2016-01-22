package com.jtomaszk.apps.myscale.importer;

import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.util.List;

import lombok.Setter;

/**
 * Created by jtomaszk on 22.01.16.
 */
public class ImporterService {

    @Setter
    private WeightEntryDao dao;

    public void mergeFromImport(List<WeightEntry> entries) {

        for (WeightEntry entry : entries) {
            WeightEntry count = dao.findByDateTimeMillisecons(entry.getDateTimeMilliseconds());
            if (count == null) {
                entry.save();
            }
        }
    }
}
