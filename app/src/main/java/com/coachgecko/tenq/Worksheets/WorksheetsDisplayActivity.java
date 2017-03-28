
package com.coachgecko.tenq.Worksheets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.coachgecko.tenq.R;

import java.text.DecimalFormat;



public class WorksheetsDisplayActivity extends FragmentActivity
        implements TopicFragment.OnTopicSelectedListener {


    private String grade;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topicworksheet);

        // This is received from Welcome Screen
        // Grade
        extras = getIntent().getExtras();


        if (extras != null) {
            int gradeNo = extras.getInt("grade");
            DecimalFormat formatter = new DecimalFormat("00");
            //TODO make this subject as an input option
            grade = "MA"+formatter.format(gradeNo);
        }
        else{
            // Default it to Grade 4
            // TODO make this give an error
            grade = "MA04";
        }

        // The top Topic Selection section
        setupTopicFragment();

        // Upon Topic Selection, the worksheets available
        setupWorksheetListFragment();

    }


    private Fragment setupTopicFragment() {
        FragmentTransaction transaction;
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new TopicFragment();

        // send Grade info to load the appropriate topics list
        Bundle b = new Bundle();
        b.putString("grade", grade);
        fragment.setArguments(b);
        transaction.add(R.id.search_topic_fragment, fragment, "Search_Frag");
        transaction.commit();

        return fragment;
    }

    private Fragment setupWorksheetListFragment() {
        FragmentTransaction transaction;
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new WorksheetFragment();


        transaction.add(R.id.worksheet_fragment, fragment, "Result_Frag");
        transaction.commit();

        return fragment;
    }




    // Removes Existing Fragment
    private void fragmentRefresher() {

        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.worksheet_fragment);

        if (f != null) {
            getSupportFragmentManager().beginTransaction().
                    remove(f).commit();
        }

    }


    // This is triggered from Topics Fragment
    @Override
    public void onTopicSelected(String topicName) {
        fragmentRefresher();
        Fragment fragment = setupWorksheetListFragment();

        // send the topicID selected and grade
        Bundle b = new Bundle();
        b.putString("topicKey", topicName);
        b.putString("grade", grade);

        // these arguments are sent to Worksheet Fragment.
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.worksheet_fragment, fragment).commit();
    }

}