package com.jtomaszk.apps.myscale.repository;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.myscale.MainActivity;
import com.jtomaszk.apps.myscale.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by jarema-user on 2015-11-19.
 */
public class GoogleApiClientWrapper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleApiClientWrapper";
    public static final int REQUEST_OAUTH = 1000;
    private static final String AUTH_PENDING = "auth_state_pending";

    private boolean authInProgress = false;

    private GoogleApiClient mClient;

    private static GoogleApiClientWrapper ourInstance = null;

    public static GoogleApiClientWrapper getInstance(Context ctx) {
//        if (ourInstance == null) {
            ourInstance = new GoogleApiClientWrapper(ctx);
//        }
        return ourInstance;
    }

    public static GoogleApiClientWrapper getInstance(Context ctx, Bundle savedInstanceState) {
        if (ourInstance == null) {
            ourInstance = new GoogleApiClientWrapper(ctx, savedInstanceState);
        }
        return ourInstance;
    }

    private GoogleApiClientWrapper(Context ctx) {
        this(ctx, null);
    }

    private GoogleApiClientWrapper(Context ctx, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        buildFitnessClient(ctx);

        // Connect to the Fitness API
        Log.i(TAG, "Connecting...");
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
                                Log.i(TAG, "Connected!!!");
                                // Now you can make calls to the Fitness APIs.
                                // Put application specific code here.
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "Connection failed. Cause: " + result.toString());
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
                                        Log.i(TAG, "Attempting to resolve failed connection");
                                        authInProgress = true;
                                        result.startResolutionForResult(getActivity(),
                                                REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG, "Exception while starting resolution activity", e);
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
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (NoSuchFieldException e) {
            Log.e(TAG, e.getMessage(), e);
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

    public void insertData(DataSet dataSet, ResultCallback<? super Status> callback) {

        Fitness.HistoryApi.insertData(mClient, dataSet)
                .setResultCallback(callback);
    }

    public DataReadRequest getSimpleDataReadRequest(long endTime, long startTime, DataType dataType) {

        return new DataReadRequest.Builder()
                .read(dataType)
                .enableServerQueries()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        //                        .aggregate(DataType.TYPE_WEIGHT, DataType.AGGREGATE_WEIGHT_SUMMARY)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        //                        .bucketByTime(1, TimeUnit.DAYS)
                        //                        .bu
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    public void read(DataReadRequest readRequest, ResultCallback<DataReadResult> callback) {
        Fitness.HistoryApi.readData(mClient, readRequest)
                .setResultCallback(callback);
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

}
