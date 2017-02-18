package com.coachgecko.tenq.Results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.coachgecko.tenq.R;

public class ResultsDisplayActivity extends FragmentActivity {

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setupResultSummaryFragment();

        setupResultDisplayFragment();
    }

    private Fragment setupResultSummaryFragment() {

        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new ResultSummaryFragment();


        Bundle extras = getIntent().getExtras();
        fragment.setArguments(extras);

        transaction.add(R.id.result_display, fragment, "Result_Frag");
        transaction.commit();

        return fragment;
    }

    private Fragment setupResultDisplayFragment() {

        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new ResultDisplayFragment();

        Bundle extras = getIntent().getExtras();
        fragment.setArguments(extras);

        transaction.add(R.id.result_answers, fragment, "Display Fragment");
        transaction.commit();

        return fragment;
    }

}
