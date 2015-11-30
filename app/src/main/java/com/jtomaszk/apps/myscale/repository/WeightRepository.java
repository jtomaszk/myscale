package com.jtomaszk.apps.myscale.repository;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by jarema-user on 2015-11-19.
 */
public class WeightRepository extends AbstractFitnessApiClient {

    private static final String TAG = "WeightRepository";

    public WeightRepository(Context ctx) {
        super(ctx, null);
    }

    public DataReadResult readAll() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.MONTH, -1);
        long startTime = cal.getTimeInMillis();

        final List<PointValue> values = new ArrayList<>();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = getSimpleDataReadRequest(endTime, startTime, DataType.TYPE_WEIGHT);
        return read(readRequest);
    }

    public List<WeightEntry> insertData(List<WeightEntry> list) {
        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName("com.jtomaszk.apps.myscale")
                .setDataType(DataType.TYPE_WEIGHT)
                .setName("MyScale - weight")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        for (WeightEntry weightEntry : list) {
            long endTime = weightEntry.getDateTimeMilliseconds();
            long startTime = weightEntry.getDateTimeMilliseconds();

            DataPoint dataPoint = dataSet.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            dataPoint.getValue(Field.FIELD_WEIGHT).setFloat(weightEntry.getWeight());
            weightEntry.setHash(dataPoint.hashCode());
            dataSet.add(dataPoint);
        }

        Status status = null;
        if (!dataSet.isEmpty()) {
            status = insertData(dataSet);
        }

        if (status != null && status.isSuccess()) {
            for (WeightEntry weightEntry : list) {
                weightEntry.setSynced(true);
            }
        } else {
            for (WeightEntry weightEntry : list) {
                weightEntry.setHash(null);
            }
        }
        return list;
    }
}
