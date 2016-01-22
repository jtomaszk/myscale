package com.jtomaszk.apps.myscale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jtomaszk.apps.common.utils.DateUtil;
import com.jtomaszk.apps.myscale.R;
import com.jtomaszk.apps.myscale.dao.WeightEntryDaoImpl;
import com.jtomaszk.apps.myscale.dao.WeightEntryDao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import pl.wavesoftware.eid.exceptions.Eid;

public class AddWeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AddWeightActivity";

    private Context context;
    private DateFormat dateFormat = DateUtil.getDateInstance();

    private DateFormat timeFormat = DateUtil.getTimeInstance();

    private DateFormat dateTimeFormat = DateUtil.getDateTimeInstance();

    private WeightEntryDao dao = new WeightEntryDaoImpl();
    private TextView dateText;
    private TextView timeText;
    private NumberPicker weightPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_weight);

        setupWeightPicker();
        setupDateTimePicker();

        findViewById(R.id.buttonAddWeight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.add_loading_spinner).setVisibility(View.VISIBLE);
                Log.i(TAG, "SaveWeightClick");
                Float weight = (float) weightPicker.getValue() / 10.0F;

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

    private void setupDateTimePicker() {
        Date actualDate = new Date();
        String initialDate = dateFormat.format(actualDate);
        dateText = setupDatePicker(R.id.editTextDate, initialDate, new DatePickerOnClickListener());
        String initialTime = timeFormat.format(actualDate);
        timeText = setupDatePicker(R.id.editTextTime, initialTime, new TimePickerOnClickListener());
    }

    private TextView setupDatePicker(int viewId, String text, View.OnClickListener onClickListener) {
        TextView textView = (TextView) findViewById(viewId);
        textView.setText(text);
        textView.setOnClickListener(onClickListener);
        return textView;
    }

    private void setupWeightPicker() {
        weightPicker = (NumberPicker) findViewById(R.id.editTextWeight);

        weightPicker.setFormatter(new WeightFormatter());
        weightPicker.setMinValue(100);
        weightPicker.setMaxValue(2000);

        Bundle bundle = getIntent().getExtras();
        float last = bundle.getFloat("last");
        weightPicker.setValue((int) (last * 10));

        firstRenderingFix();
    }

    private void firstRenderingFix() {
        try {
            Field f = NumberPicker.class.getDeclaredField("mInputText");
            f.setAccessible(true);
            EditText inputText = (EditText) f.get(weightPicker);
            inputText.setFilters(new InputFilter[0]);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, new Eid("20160120:233036").toString(), e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, new Eid("20160120:233052").toString(), e);
        }
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

    private static class WeightFormatter implements NumberPicker.Formatter {
        private final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

        @Override
        public String format(int value) {
            return String.valueOf(value / 10)
                    + decimalFormatSymbols.getDecimalSeparator()
                    + String.valueOf(value % 10);
        }
    }

    private class DatePickerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDatePicker();
        }
    }

    private class TimePickerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showTimePicker();
        }
    }
}
