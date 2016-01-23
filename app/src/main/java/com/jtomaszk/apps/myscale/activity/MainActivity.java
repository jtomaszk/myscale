package com.jtomaszk.apps.myscale.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jtomaszk.apps.common.chart.ChartPoint;
import com.jtomaszk.apps.common.chart.DataChartBuilder;
import com.jtomaszk.apps.common.logger.EidLogger;
import com.jtomaszk.apps.myscale.R;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.dao.WeightEntryDaoImpl;
import com.jtomaszk.apps.myscale.entity.WeightEntry;
import com.jtomaszk.apps.myscale.model.BMI;
import com.jtomaszk.apps.myscale.model.BMIModel;
import com.jtomaszk.apps.myscale.services.SynchronizeService;
import com.jtomaszk.apps.myscale.utils.HeightUtil;

import java.util.List;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.view.LineChartView;

import static com.jtomaszk.apps.common.chart.ChartPointGroupAccesors.avg;
import static com.jtomaszk.apps.myscale.chart.aggregator.ValueAggregator.dayAggregator;
import static com.jtomaszk.apps.myscale.chart.aggregator.ValueAggregator.monthAggregator;
import static com.jtomaszk.apps.myscale.chart.aggregator.ValueAggregator.weekAggregator;
import static com.jtomaszk.apps.myscale.chart.axis.AxisFormatters.dayFormatter;
import static com.jtomaszk.apps.myscale.chart.axis.AxisFormatters.monthFormatter;
import static com.jtomaszk.apps.myscale.chart.axis.AxisFormatters.weekFormatter;
import static com.jtomaszk.apps.myscale.utils.WeightUtil.simpleTransform;

public class MainActivity extends AppCompatActivity {

    private static final EidLogger logger = EidLogger.getLogger();

    private Context context;
    private List<WeightEntry> list;
    private LineChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(addWeightClick());

        RadioButton radioButtonMonth = (RadioButton) findViewById(R.id.radioButtonMonth);
        radioButtonMonth.setOnClickListener(addRadioButtonMonthClick());
        RadioButton radioButtonWeek = (RadioButton) findViewById(R.id.radioButtonWeek);
        radioButtonWeek.setOnClickListener(addRadioButtonWeekClick());
        RadioButton radioButtonDay = (RadioButton) findViewById(R.id.radioButtonDay);
        radioButtonDay.setOnClickListener(addRadioButtonDayClick());

        context = this;
//        AbstractGFitnessApiClient.getInstance(context, savedInstanceState);
    }

    private View.OnClickListener addRadioButtonDayClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadChartData(dayAggregator(), dayFormatter());
            }
        };
    }

    private View.OnClickListener addRadioButtonWeekClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadChartData(weekAggregator(), weekFormatter());
            }
        };
    }

    @NonNull
    private View.OnClickListener addRadioButtonMonthClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadChartData(monthAggregator(), monthFormatter());
            }
        };
    }

    private View.OnClickListener addWeightClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddWeightActivity.class);
                intent.putExtra("last", last);
                startActivity(intent);
            }
        };
    }


    float last = 0;

    @Override
    protected void onStart() {
        super.onStart();
//        WeightEntry.deleteAll(WeightEntry.class);

        WeightEntryDao dao = new WeightEntryDaoImpl();
        list = dao.getAllSorted();
        last = Iterables.getLast(list).getWeight();

        chart = (LineChartView) findViewById(R.id.chart2);
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        reloadChartData(dayAggregator(), dayFormatter());

        findViewById(R.id.loading_spinner).setVisibility(View.GONE);

        int height = HeightUtil.readHeight(getBaseContext());
        BMIModel bmiModel = new BMIModel(height, last);

        TextView bmiText = (TextView) findViewById(R.id.bmiView);
        BMI bmi = bmiModel.bmi();
        if (last > 10) {
            bmiText.setText("height=" + height + " last=" + last + " bmi=" + bmi.getBmi() + " cat=" + bmi.getCategory());
        }
    }

    private void reloadChartData(Function<ChartPoint, Float> groupFunction,
                                 AxisValueFormatter formatter) {
        DataChartBuilder dataChart = getDataChartBuilderForGroup(groupFunction, formatter);
        chart.setLineChartData(dataChart.getData());
    }

    private DataChartBuilder getDataChartBuilderForGroup(Function<ChartPoint, Float> groupFunction,
                                                         AxisValueFormatter formatter) {
        return new DataChartBuilder()
                .addLineGroupBy(simpleTransform(list), groupFunction, avg())
                .addAxisX(formatter)
                .addAxisY();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        AbstractGFitnessApiClient.getInstance(context).onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        logger.i("20160123:160414", "onActivityResult %d %d %s", requestCode, resultCode, data);
//        if (requestCode == AbstractGFitnessApiClient.REQUEST_OAUTH) {
//            AbstractGFitnessApiClient.getInstance(context).onActivityResult(resultCode);
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        AbstractGFitnessApiClient.getInstance(context).onSaveInstanceState(outState);
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
            case R.id.action_import:
                intent = new Intent(this, ImportActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_force_sync:
                SynchronizeService.startActionSyncWeight(context, true);
                SynchronizeService.startActionSyncHeight(context);
                return true;
            case R.id.history_activity:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
