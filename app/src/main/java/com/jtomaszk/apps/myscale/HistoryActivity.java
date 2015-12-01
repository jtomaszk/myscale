package com.jtomaszk.apps.myscale;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.PointValue;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        WeightEntryDao dao = new WeightEntryDao();
        List<WeightEntry> list = dao.getAll();

        ListView listView = (ListView) findViewById(R.id.listView);
        Log.d(TAG, Arrays.toString(list.toArray()));
        List<String> values = new ArrayList<>(list.size());
        for (WeightEntry entry : list) {
            values.add(convertToViewType(entry));
        }
        listView.setAdapter(new ArrayAdapter<>(context, R.layout.simple_list_item_1, values));
    }

    @NonNull
    private String convertToViewType(WeightEntry entry) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(entry.getDateTimeMilliseconds())) + " " + entry.getWeight()
                + " " + entry.getDataSource();
    }
}
