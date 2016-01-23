package com.jtomaszk.apps.common.gapi;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.common.logger.EidLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by jarema-user on 2015-11-19.
 */
public abstract class AbstractGFitnessApiClient implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final EidLogger logger = EidLogger.getLogger();

    public static final int REQUEST_OAUTH = 1000;
    private static final String AUTH_PENDING = "auth_state_pending";

    private boolean authInProgress = false;

    protected GoogleApiClient mClient;
    protected Context ctx;

    protected AbstractGFitnessApiClient(Context ctx, Bundle savedInstanceState) {
        this.ctx = ctx;
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        buildFitnessClient(ctx);

        // Connect to the Fitness API
        logger.i("20160123:160641", "Connecting...");
        mClient.connect();
    }

    /**
     * Build a {@link GoogleApiClient} that will authenticate the user and allow the application
     * to connect to Fitness APIs. The scopes included should match the scopes your app needs
     * (see documentation for details). Authentication will occasionally fail intentionally,
     * and in those cases, there will be a known resolution, which the OnConnectionFailedListener()
     * can address. Examples of this include the user never having signed in before, or having
     * multiple accounts on the device and needing to specify which account to use, etc.
     *
     * @param ctx
     */
    private void buildFitnessClient(Context ctx) {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(ctx)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                logger.i("20160123:160652", "Connected!!!");
                                // Now you can make calls to the Fitness APIs.
                                // Put application specific code here.
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    logger.i("20160123:160655", "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    logger.i("20160123:160659", "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                logger.i("20160123:160708", "Connection failed. Cause: " + result.toString());
                                if (!result.hasResolution()) {
                                    // Show the localized error dialog
//                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
//                                            MainActivity.this, 0).show();
                                    return;
                                }
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        logger.i("20160123:160712", "Attempting to resolve failed connection");
                                        authInProgress = true;
                                        result.startResolutionForResult(getActivity(),
                                                REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        logger.e("20160123:160715", "Exception while starting resolution activity", e);
                                    }
                                }
                            }
                        }
                )
                .build();
    }

    public static Activity getActivity() {
        try {
            return getActivityThrows();
        } catch (NoSuchMethodException e) {
            logger.e("20160123:160724", e.getMessage(), e);
            return null;
        } catch (ClassNotFoundException e) {
            logger.e("20160123:160729", e.getMessage(), e);
            return null;
        } catch (IllegalAccessException e) {
            logger.e("20160123:160733", e.getMessage(), e);
            return null;
        } catch (InvocationTargetException e) {
            logger.e("20160123:160738", e.getMessage(), e);
            return null;
        } catch (NoSuchFieldException e) {
            logger.e("20160123:160741", e.getMessage(), e);
            return null;
        }
    }

    public static Activity getActivityThrows() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        java.lang.reflect.Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        HashMap activities = (HashMap) activitiesField.get(activityThread);
        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            java.lang.reflect.Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityRecord)) {
                java.lang.reflect.Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected Status insertData(DataSet dataSet) {
        return Fitness.HistoryApi.insertData(mClient, dataSet).await();
    }

    protected DataReadRequest getSimpleDataReadRequest(long endTime, long startTime, DataType dataType) {

        return new DataReadRequest.Builder()
                .read(dataType)
                .enableServerQueries()
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    protected DataReadResult read(DataReadRequest readRequest) {
        return Fitness.HistoryApi.readData(mClient, readRequest).await();
    }

    public void onActivityResult(int resultCode) {
        authInProgress = false;
        if (resultCode == Activity.RESULT_OK) {
            // Make sure the app is not already connected or attempting to connect
            if (!mClient.isConnecting() && !mClient.isConnected()) {
                mClient.connect();
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    public void onStop() {
        if (mClient.isConnected()) {
            mClient.disconnect();
        }
    }

    protected long getTimeInMillisAddInterval(int field, int value) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.add(field, value);
        return cal.getTimeInMillis();
    }

    protected long getCurrentTimeInMillis() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        return cal.getTimeInMillis();
    }

}
