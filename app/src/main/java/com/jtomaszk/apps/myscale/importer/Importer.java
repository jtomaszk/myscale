package com.jtomaszk.apps.myscale.importer;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.jtomaszk.apps.myscale.entity.DataSource;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.preferences.AppConst;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.wavesoftware.eid.exceptions.EidRuntimeException;

/**
 * Created by jarema-user on 2016-01-20.
 */
public class Importer {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'HH:'HH:mm");
    private DecimalFormat df = new DecimalFormat();

    @NonNull
    public List<WeightEntry> parseCsvData(String csvData) {
        List<WeightEntry> list = Lists.newArrayList();

        CSVParser parser = createParser(csvData);
        for (CSVRecord csvRecord : parser) {
            if (csvRecord.size() < 2) {
                continue;
            }

            String dateStr = csvRecord.get("date");
            Date date = parseDate(dateStr);

            String weightStr = csvRecord.get("weight");
            Float weight = parseFloat(weightStr);

            WeightEntry weightEntry = new WeightEntry(weight, date.getTime(), DataSource.IMPORT, AppConst.APP_PACKAGE.getValue());
            list.add(weightEntry);
        }
        return list;
    }

    private float parseFloat(String weightStr) {
        try {
            return df.parse(weightStr).floatValue();
        } catch (ParseException e) {
            throw new EidRuntimeException("20160121:115705", weightStr, e);
        }
    }

    private Date parseDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new EidRuntimeException("20160121:115317", date, e);
        }
    }

    @NonNull
    private CSVParser createParser(String csvData) {
        try {
            return CSVParser.parse(csvData,
                    CSVFormat
                    .newFormat(';')
                    .withCommentMarker('#')
                    .withHeader("date", "weight")
            );
        } catch (IOException e) {
            throw new EidRuntimeException("20160121:115237", e);
        }
    }

}
