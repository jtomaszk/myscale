package com.jtomaszk.apps.myscale;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddWeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddWeightActivity";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM = "HH:mm";

    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
    private SimpleDateFormat timeFormat = new SimpleDateFormat(HH_MM);
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(YYYY_MM_DD + " " + HH_MM);
    private WeightEntryDao dao = new WeightEntryDao();
    private TextView dateText;
    private TextView timeText;
    private NumberPicker weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_weight);
        dateText = (TextView) findViewById(R.id.editTextDate);
        timeText = (TextView) findViewById(R.id.editTextTime);
        weightText = (NumberPicker) findViewById(R.id.editTextWeight);

        dateText.setText(dateFormat.format(new Date()));
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        timeText.setText(timeFormat.format(new Date()));
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        weightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker();
            }
        });

        findViewById(R.id.buttonAddWeight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.add_loading_spinner).setVisibility(View.VISIBLE);
                Log.i(TAG, "SaveWeightClick");
                Float weight = null; //Float.valueOf(weightText.getText().toString());

                try {
                    Date date = dateTimeFormat.parse(dateText.getText().toString() + " " + timeText.getText().toString());
                    dao.addFromUser(date.getTime(), weight);
                    Log.i(TAG, "Data insert was successful!");
                    AddWeightActivity.this.finish();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Problem with parse date", Toast.LENGTH_SHORT).show();
                }

                findViewById(R.id.add_loading_spinner).setVisibility(View.GONE);
            }
        });
    }

    private void showNumberPicker() {
//        NumberDi np = NumberPicker.
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddWeightActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Choose Date:");
    }

    private void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                AddWeightActivity.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );
        dpd.show(getFragmentManager(), "Choose Time:");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);

        dateText.setText(dateFormat.format(cal.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);

        timeText.setText(timeFormat.format(cal.getTime()));
    }
}
