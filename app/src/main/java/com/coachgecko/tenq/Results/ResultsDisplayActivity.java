package com.coachgecko.tenq.Results;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.coachgecko.tenq.R;

public class ResultsDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultTV = (TextView) findViewById(R.id.result);
        Bundle extras = getIntent().getExtras();
        String result = "";
        if (extras != null) {
            result = extras.getString("resultDetails");
        }
        resultTV.setText(result);
    }
}
