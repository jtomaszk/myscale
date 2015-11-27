package com.jtomaszk.apps.myscale.repository;

import android.content.Context;

import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by jarema-user on 2015-11-19.
 */
public class HeightRepository {
    private static HeightRepository ourInstance = null;
    private static final String TAG = "HeightRepository";

    public static HeightRepository getInstance(Context ctx) {
        if (ourInstance == null) {
            ourInstance = new HeightRepository(ctx);
        }
        return ourInstance;
    }

    private HeightRepository(Context ctx) {
    }

    public void readAll() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -10);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = getDataReadRequest(endTime, startTime);

//        Fitness.HistoryApi.readData(mClient, readRequest).setResultCallback(
//                new ResultCallback<DataReadResult>() {
//                    @Override
//                    public void onResult(DataReadResult dataReadResult) {
//
//                        ArrayList<String> list = new ArrayList<String>();
//
//                        Log.i(TAG, "bb!!!" + dataReadResult.toString() + " " + dataReadResult.getDataSets().size());
//                        for (DataSet ds : dataReadResult.getDataSets()) {
//                            Log.i(TAG, "Data set:" + ds.getDataType() + " " + ds.getDataPoints().size());
//
//                            for (DataPoint dp : ds.getDataPoints()) {
//                                Log.i(TAG, "Data point:");
//                                Log.i(TAG, "\tType: " + dp.getDataType().getName());
//                                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
//                                for (Field field : dp.getDataType().getFields()) {
//                                    Log.i(TAG, "\tField: " + field.getName() +
//                                            " Value: " + dp.getValue(field).asFloat());
//                                    list.add(convertToViewType(dp, field));
//
//                                    values.add(new PointValue(dp.getTimestamp(TimeUnit.DAYS), dp.getValue(field).asFloat()));
//                                }
//                            }
//                        }
//
//
//                        ListView listView = (ListView) findViewById(R.id.listView);
//                        listView.setAdapter(new ArrayAdapter<>(context, R.layout.simple_list_item_1, list));
//
//                        //In most cased you can call data model methods in builder-pattern-like manner.
//                        Line line = new Line(values).setColor(Color.BLUE).setCubic(true).setHasPoints(true);
//                        List<Line> lines = new ArrayList<Line>();
//                        lines.add(line);
//
//                        LineChartData data = new LineChartData();
//                        data.setLines(lines);
//                        Log.i(TAG, Arrays.toString(lines.toArray()));
//
//                        LineChartView chart = (LineChartView) findViewById(R.id.chart);
//                        chart.setLineChartData(data);
//                        chart.setInteractive(true);
//                        chart.setZoomType(ZoomType.HORIZONTAL);
//                        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//                    }
//                }
//        );
    }

    private DataReadRequest getDataReadRequest(long endTime, long startTime) {
        return new DataReadRequest.Builder()
                .read(DataType.TYPE_HEIGHT)
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
}
