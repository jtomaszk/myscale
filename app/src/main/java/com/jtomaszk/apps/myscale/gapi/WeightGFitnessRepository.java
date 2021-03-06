package com.jtomaszk.apps.myscale.gapi;

import android.content.Context;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.common.gapi.AbstractGFitnessApiClient;
import com.jtomaszk.apps.common.logger.EidLogger;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.preferences.AppConst;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jarema-user on 2015-11-19.
 */
public class WeightGFitnessRepository extends AbstractGFitnessApiClient {

    private static final EidLogger logger = EidLogger.getLogger();

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public WeightGFitnessRepository(Context ctx) {
        super(ctx, null);
    }

    public void deleteAll() {
        // Set a start and end time for our data, using a start time of 1 day before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -20);
        long startTime = cal.getTimeInMillis();

        //  Create a delete request object, providing a data type and a time interval
        DataDeleteRequest request = new DataDeleteRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .addDataType(DataType.TYPE_WEIGHT)
                .build();

        Status status = Fitness.HistoryApi.deleteData(mClient, request).await();

        if (status.isSuccess()) {
            logger.i("20160123:160528", "Successfully deleted today's step count data");
        } else {
            logger.i("20160123:160532", "Failed to delete today's step count data");
        }
    }

    public DataReadResult readAll() {
        long endTime = getCurrentTimeInMillis();
        long startTime = getTimeInMillisAddInterval(Calendar.YEAR, -5);

        DataReadRequest readRequest = getSimpleDataReadRequest(endTime, startTime, DataType.TYPE_WEIGHT);
        return read(readRequest);
    }

    public List<WeightEntry> insertData(List<WeightEntry> list) {
        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(AppConst.APP_PACKAGE.getValue())
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
        }
        return list;
    }
}
