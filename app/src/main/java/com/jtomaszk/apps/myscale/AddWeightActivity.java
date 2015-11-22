package com.jtomaszk.apps.myscale;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.jtomaszk.apps.myscale.repository.WeightRepository;

import java.util.Date;

public class AddWeightActivity extends AppCompatActivity {

    private static final String TAG = "AddWeightActivity";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_weight);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.add_loading_spinner).setVisibility(View.VISIBLE);
                Log.i(TAG, "SaveWeightClick");
                EditText editText = (EditText) findViewById(R.id.editText3);
                Float value = Float.valueOf(editText.getText().toString());
                Date now = new Date();
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
//                Snackbar.make(view, "Saved!", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).show();
            }
        });


    }
}
