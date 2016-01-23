package com.jtomaszk.apps.myscale.importer;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.jtomaszk.apps.myscale.entity.DataSource;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.preferences.AppConst;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.wavesoftware.eid.exceptions.EidRuntimeException;

/**
 * Created by jarema-user on 2016-01-20.
 */
public class Importer {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'HH:'HH:mm");
    private NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

    @NonNull
    public List<WeightEntry> parseCsvData(File file) {
        List<WeightEntry> list = Lists.newArrayList();

        CSVParser parser = createParser(file);
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
            return numberFormat.parse(weightStr).floatValue();
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
    private CSVParser createParser(File file) {
        try {
            return CSVParser.parse(file, Charset.forName("UTF-8"),
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
