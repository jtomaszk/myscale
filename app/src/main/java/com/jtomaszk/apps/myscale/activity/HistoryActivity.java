package com.jtomaszk.apps.myscale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jtomaszk.apps.common.logger.EidLogger;
import com.jtomaszk.apps.myscale.R;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.jtomaszk.apps.myscale.dao.WeightEntryDaoImpl;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final EidLogger logger = EidLogger.getLogger();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        WeightEntryDao dao = new WeightEntryDaoImpl();
        List<WeightEntry> list = dao.getAllSorted();

        ListView listView = (ListView) findViewById(R.id.listView);
        logger.d("20160123:160003", Arrays.toString(list.toArray()));
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
