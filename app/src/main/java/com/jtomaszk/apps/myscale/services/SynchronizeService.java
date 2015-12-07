package com.jtomaszk.apps.myscale.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.model.HeightUtil;
import com.jtomaszk.apps.myscale.repository.HeightRepository;
import com.jtomaszk.apps.myscale.repository.WeightRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class SynchronizeService extends IntentService {
    private static final String TAG = "HistoryActivity";

    private static final String ACTION_SYNC_WEIGHT = ".action.ACTION_SYNC_WEIGHT";
    private static final String ACTION_SYNC_HEIGHT = ".action.ACTION_SYNC_HEIGHT";

    private static final String EXTRA_SYNC_ALL = ".extra.EXTRA_SYNC_ALL";

    private WeightEntryDao dao = new WeightEntryDao();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSyncWeight(Context context, Boolean syncAll) {
        Intent intent = new Intent(context, com.jtomaszk.apps.myscale.services.SynchronizeService.class);
        intent.setAction(ACTION_SYNC_WEIGHT);
        intent.putExtra(EXTRA_SYNC_ALL, syncAll);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSyncHeight(Context context) {
        Intent intent = new Intent(context, com.jtomaszk.apps.myscale.services.SynchronizeService.class);
        intent.setAction(ACTION_SYNC_HEIGHT);
        context.startService(intent);
    }

    public SynchronizeService() {
        super("com.jtomaszk.apps.myscale.services.SynchronizeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC_WEIGHT.equals(action)) {
                final Boolean syncAll = intent.getBooleanExtra(EXTRA_SYNC_ALL, false);
                handleActionSyncWeight(syncAll);
            } else if (ACTION_SYNC_HEIGHT.equals(action)) {
                handleActionSyncHeight();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSyncWeight(boolean syncAll) {
        Log.i(TAG, "handleActionSyncWeight");
        WeightRepository weightRepository = new WeightRepository(getBaseContext());
//        weightRepository.deleteAll();
//        dao.deleteAll();

        final List<WeightEntry> notSyncedList = dao.findNotSynced();

        List<WeightEntry> result = weightRepository.insertData(notSyncedList);
        dao.save(result);

        DataReadResult dataReadResult = weightRepository.readAll();

        for (DataSet ds : dataReadResult.getDataSets()) {
            for (DataPoint dp : ds.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    float weight = dp.getValue(field).asFloat();
                    Log.i(TAG,  dp.hashCode() + " " + weight);
                    dao.addIfNotMatchedFromGooleFit(dp.getTimestamp(TimeUnit.MILLISECONDS),
                            weight, dp.getDataSource().getAppPackageName());
                }
            }
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSyncHeight() {
        int height = HeightUtil.readHeight(getBaseContext());
        Log.i(TAG, "handleActionSyncHeight " + height);

        HeightRepository heightRepository = new HeightRepository(getBaseContext());
        DataReadResult dataReadResult = heightRepository.readAll();

        float heightVal = 0;
        long maxTime = 0;
        boolean foundEntry = false;

        for (DataSet ds : dataReadResult.getDataSets()) {
            for (DataPoint dp : ds.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    Log.i(TAG, dp.getTimestamp(TimeUnit.MILLISECONDS) + field.getName() + " " + dp.getValue(field).asFloat());

                    long time = dp.getTimestamp(TimeUnit.MILLISECONDS);
                    if (time > maxTime) {
                        maxTime = time;
                        heightVal = dp.getValue(field).asFloat();
                        foundEntry = true;
                    }
                }
            }
        }

        if (foundEntry && ((height / 100.0) - heightVal) < 0.01) {
            HeightUtil.writeHeight(Math.round(heightVal * 100), maxTime, getBaseContext());
        } else {
            heightRepository.insert((float) (height / 100.0));
        }
    }
}
