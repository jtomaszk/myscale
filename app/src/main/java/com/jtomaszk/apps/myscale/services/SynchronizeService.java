package com.jtomaszk.apps.myscale.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.common.logger.EidLogger;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.dao.WeightEntryDaoImpl;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.gapi.HeightGFitnessRepository;
import com.jtomaszk.apps.myscale.gapi.WeightGFitnessRepository;
import com.jtomaszk.apps.myscale.utils.HeightUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Setter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class SynchronizeService extends IntentService {

    private static final EidLogger logger = EidLogger.getLogger();

    private static final String ACTION_SYNC_WEIGHT = ".action.ACTION_SYNC_WEIGHT";
    private static final String ACTION_SYNC_HEIGHT = ".action.ACTION_SYNC_HEIGHT";

    private static final String EXTRA_SYNC_ALL = ".extra.EXTRA_SYNC_ALL";

    @Setter
    private WeightEntryDao dao = new WeightEntryDaoImpl();

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
        logger.i("20160123:160601", "handleActionSyncWeight");
        WeightGFitnessRepository weightRepository = new WeightGFitnessRepository(getBaseContext());
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
                    logger.i("20160123:160606", dp.hashCode() + " " + weight);
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
        long heightTime = HeightUtil.readHeightTime(getBaseContext());

        logger.i("20160123:160611", "handleActionSyncHeight " + height);

        HeightGFitnessRepository heightRepository = new HeightGFitnessRepository(getBaseContext());
        DataReadResult dataReadResult = heightRepository.readAll();

        float lastHeightValue = 0;
        long lastHeightTime = 0;
        boolean foundEntry = false;

        for (DataSet ds : dataReadResult.getDataSets()) {
            for (DataPoint dp : ds.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    logger.i("20160123:160616", dp.getTimestamp(TimeUnit.MILLISECONDS) + field.getName() + " " + dp.getValue(field).asFloat());

                    long time = dp.getTimestamp(TimeUnit.MILLISECONDS);
                    if (time > lastHeightTime) {
                        lastHeightTime = time;
                        lastHeightValue = dp.getValue(field).asFloat();
                        foundEntry = true;
                    }
                }
            }
        }

        if (foundEntry && ((height / 100.0) - lastHeightValue) < 0.01) {
            HeightUtil.writeHeight(Math.round(lastHeightValue * 100), lastHeightTime, getBaseContext());
        } else {
            heightRepository.insert((float) (height / 100.0));
        }
    }
}
