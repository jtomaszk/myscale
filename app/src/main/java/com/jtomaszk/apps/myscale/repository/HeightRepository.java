package com.jtomaszk.apps.myscale.repository;

import android.content.Context;

import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jarema-user on 2015-11-19.
 */
public class HeightRepository extends AbstractFitnessApiClient {
    private static final String TAG = "HeightRepository";

    public HeightRepository(Context ctx) {
        super(ctx, null);
    }

    public DataReadResult readAll() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -10);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = getSimpleDataReadRequest(endTime, startTime, DataType.TYPE_HEIGHT);
        return read(readRequest);
    }
}
