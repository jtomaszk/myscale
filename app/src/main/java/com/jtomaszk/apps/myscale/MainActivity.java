package com.jtomaszk.apps.myscale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResult;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.model.BMI;
import com.jtomaszk.apps.myscale.model.BMIModel;
import com.jtomaszk.apps.myscale.repository.GoogleApiClientWrapper;
import com.jtomaszk.apps.myscale.repository.WeightRepository;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String TAG = "MainActivity";

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


    float last = 0;

    @Override
    protected void onStart() {
        super.onStart();

        final List<PointValue> values = new ArrayList<>();

        WeightEntryDao dao = new WeightEntryDao();
        List<WeightEntry> list2 = dao.getAll();
        Log.e(TAG, Arrays.toString(list2.toArray()));

        for (WeightEntry entry : list2) {
            values.add(new PointValue(entry.getDays(), entry.getWeight()));
        }

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


        WeightRepository.getInstance(context).readAll(
                new ResultCallback<DataReadResult>() {
                    @Override
                    public void onResult(DataReadResult dataReadResult) {
                        WeightEntryDao dao = new WeightEntryDao();

//                        List<WeightEntry> list3 = WeightEntry.findWithQuery(WeightEntry.class, "select id, avg(weight) as weight, days, date_time_milliseconds, synced, hash, data_source from weight_entry group by days");
//                        Select.from(WeightEntry.class)
//                                .groupBy("days")
//                                .list();
//                        Log.e(TAG, Arrays.toString(list3.toArray()));

                        ArrayList<String> list = new ArrayList<String>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//                        Log.i(TAG, "bb!!!" + dataReadResult.toString() + " " + dataReadResult.getDataSets().size());
                        for (DataSet ds : dataReadResult.getDataSets()) {
//                            Log.i(TAG, "Data set:" + ds.getDataType() + " " + ds.getDataPoints().size());

                            for (DataPoint dp : ds.getDataPoints()) {
//                                Log.i(TAG, "Data point:");
//                                Log.i(TAG, "\tType: " + dp.getDataType().getName());
//                                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                for (Field field : dp.getDataType().getFields()) {
                                    float weight = dp.getValue(field).asFloat();
                                    last = weight;
//                                    Log.i(TAG, "\tField: " + field.getName() + " Value: " + weight);

                                    dao.addIfNotMatchedFromGooleFit(dp.getTimestamp(TimeUnit.MILLISECONDS), dp.hashCode(), weight);
//                                    values.add(new PointValue(dp.getTimestamp(TimeUnit.DAYS), weight));
                                }
                            }
                        }

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        int height = Integer.valueOf(prefs.getString("pref_height", "180"));
                        BMIModel bmiModel = new BMIModel(height, last);

                        TextView bmiText = (TextView) findViewById(R.id.bmiView);
                        BMI bmi = bmiModel.bmi();
                        bmiText.setText("height=" + height + " last=" + last + " bmi=" + bmi.getBmi() + " cat=" + bmi.getCategory());
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
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.history_activity:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
