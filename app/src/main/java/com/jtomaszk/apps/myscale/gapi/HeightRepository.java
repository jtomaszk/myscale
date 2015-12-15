package com.jtomaszk.apps.myscale.gapi;

import android.content.Context;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.common.gapi.AbstractFitnessApiClient;
import com.jtomaszk.apps.myscale.preferences.AppConst;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by jarema-user on 2015-11-19.
 */
public class HeightRepository extends AbstractFitnessApiClient {
    private static final String TAG = "HeightRepository";

    public HeightRepository(Context ctx) {
        super(ctx, null);
    }

    public DataReadResult readAll() {
        long endTime = getCurrentTimeInMillis();
        long startTime = getTimeInMillisAddInterval(Calendar.YEAR, -5);

        DataReadRequest readRequest = getSimpleDataReadRequest(endTime, startTime, DataType.TYPE_HEIGHT);
        return read(readRequest);
    }

    public void insert(float height) {
        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(AppConst.APP_PACKAGE.getValue())
                .setDataType(DataType.TYPE_HEIGHT)
                .setName("MyScale - height")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet dataSet = DataSet.create(dataSource);

        long startTime = getCurrentTimeInMillis();

        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, startTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_HEIGHT).setFloat(height);
        dataSet.add(dataPoint);

        insertData(dataSet);
    }
}
