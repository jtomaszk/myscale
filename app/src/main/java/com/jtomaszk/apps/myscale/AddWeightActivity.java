package com.jtomaszk.apps.myscale;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.jtomaszk.apps.myscale.repository.WeightRepository;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddWeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddWeightActivity";
    public static final String YYYY_MM_DD = "yyyy-mm-dd";
    public static final String HH_MM = "HH:MM";

    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
    private SimpleDateFormat timeFormat = new SimpleDateFormat(HH_MM);
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(YYYY_MM_DD + " " + HH_MM);
    private TextView dateText;
    private TextView timeText;
    private EditText weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_weight);
        dateText = (TextView) findViewById(R.id.editTextDate);
        timeText = (TextView) findViewById(R.id.editTextTime);
        weightText = (EditText) findViewById(R.id.editTextWeight);

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
//        dateText.setText(new Date().toString());
//        weightText.setText(String.valueOf(weight));

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.add_loading_spinner).setVisibility(View.VISIBLE);
                Log.i(TAG, "SaveWeightClick");
                Float value = Float.valueOf(weightText.getText().toString());
                try {
                    Date now = dateTimeFormat.parse(dateText.getText().toString() + " " + timeText.getText().toString());
                    WeightRepository.getInstance(context).insertData(value, now, new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            findViewById(R.id.add_loading_spinner).setVisibility(View.GONE);
                            // Before querying the data, check to see if the insertion succeeded.
                            if (!status.isSuccess()) {
                                Log.i(TAG, "There was a problem inserting the dataset.");
                                return;
                            }

                            // At this point, the data has been inserted and can be read.
                            Log.i(TAG, "Data insert was successful!");
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                Snackbar.make(view, "Saved!", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).show();
            }
        });


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
        cal.set(Calendar.HOUR, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);

        timeText.setText(dateFormat.format(cal.getTime()));
    }
}
