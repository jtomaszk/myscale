package com.jtomaszk.apps.myscale;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.myscale.repository.GoogleApiClientWrapper;
import com.jtomaszk.apps.myscale.repository.WeightRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    private final static Logger LOG = Logger.getAnonymousLogger();


    /**
     * Track whether an authorization activity is stacking over the current activity, i.e. when
     * a known auth error is being resolved, such as showing the account chooser or presenting a
     * consent dialog. This avoids common duplications as might happen on screen rotations, etc.
     */
    private static final String TAG = "AAAA";

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeightClick();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).show();
            }
        });

        context = this;




        GoogleApiClientWrapper.getInstance(context, savedInstanceState);

    }

    private void addWeightClick() {
        Intent intent = new Intent(this, AddWeightActivity.class);
        startActivity(intent);
    }

    @NonNull
    private String convertToViewType(DataPoint dp, Field field) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(dp.getTimestamp(TimeUnit.MILLISECONDS)) + " " + dp.getValue(field).toString();
    }


    @Override
    protected void onStart() {
        super.onStart();

        final List<PointValue> values = new ArrayList<>();


        WeightRepository.getInstance(context).readAll(
                new ResultCallback<DataReadResult>() {
                    @Override
                    public void onResult(DataReadResult dataReadResult) {

                        ArrayList<String> list = new ArrayList<String>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Log.i(TAG, "bb!!!" + dataReadResult.toString() + " " + dataReadResult.getDataSets().size());
                        for (DataSet ds : dataReadResult.getDataSets()) {
                            Log.i(TAG, "Data set:" + ds.getDataType() + " " + ds.getDataPoints().size());

                            for (DataPoint dp : ds.getDataPoints()) {
                                Log.i(TAG, "Data point:");
                                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                for (Field field : dp.getDataType().getFields()) {
                                    Log.i(TAG, "\tField: " + field.getName() +
                                            " Value: " + dp.getValue(field).asFloat());
                                    list.add(convertToViewType(dp, field));

                                    values.add(new PointValue(dp.getTimestamp(TimeUnit.DAYS), dp.getValue(field).asFloat()));
                                }
                            }
                        }


                        ListView listView = (ListView) findViewById(R.id.listView);
                        listView.setAdapter(new ArrayAdapter<>(context, R.layout.simple_list_item_1, list));

                        //In most cased you can call data model methods in builder-pattern-like manner.
                        Line line = new Line(values).setColor(Color.BLUE).setCubic(true).setHasPoints(true);
                        List<Line> lines = new ArrayList<Line>();
                        lines.add(line);

                        LineChartData data = new LineChartData();
                        data.setLines(lines);
                        Log.i(TAG, Arrays.toString(lines.toArray()));

                        LineChartView chart = (LineChartView) findViewById(R.id.chart);
                        chart.setLineChartData(data);
                        chart.setInteractive(true);
                        chart.setZoomType(ZoomType.HORIZONTAL);
                        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

                        findViewById(R.id.loading_spinner).setVisibility(View.GONE);
                    }
                }
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleApiClientWrapper.getInstance(context).onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult " + requestCode + " " + resultCode + " " + data);
        if (requestCode == GoogleApiClientWrapper.REQUEST_OAUTH) {
            GoogleApiClientWrapper.getInstance(context).onActivityResult(resultCode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GoogleApiClientWrapper.getInstance(context).onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
