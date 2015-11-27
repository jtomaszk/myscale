package com.jtomaszk.apps.myscale.repository;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

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
public class WeightRepository {

    private static WeightRepository ourInstance = null;
    private static final String TAG = "WeightRepository";
    private Context ctx;

    public static WeightRepository getInstance(Context ctx) {
//        if (ourInstance == null) {
            ourInstance = new WeightRepository(ctx);
//        }
        ourInstance.ctx = ctx;
        return ourInstance;
    }

    private WeightRepository(Context ctx) {
        this.ctx = ctx;
    }

    public void insertData(float value, Date date, ResultCallback<? super Status> callback) {
        Log.i(TAG, "insertData " + value + " " + date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long endTime = cal.getTimeInMillis();
        long startTime = cal.getTimeInMillis();

        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName("com.jtomaszk.apps.myscale")
                .setDataType(DataType.TYPE_WEIGHT)
                .setName(TAG + " - weight")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_WEIGHT).setFloat(value);
        dataSet.add(dataPoint);

        GoogleApiClientWrapper.getInstance(ctx).insertData(dataSet, callback);
    }

    public void readAll(ResultCallback<DataReadResult> callback) {
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

        DataReadRequest readRequest = GoogleApiClientWrapper.getInstance(ctx).getSimpleDataReadRequest(endTime, startTime, DataType.TYPE_WEIGHT);
        GoogleApiClientWrapper.getInstance(ctx).read(readRequest, callback);
    }
}
