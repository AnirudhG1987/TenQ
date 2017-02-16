
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
import com.coachgecko.tenq.Topics.TopicFragment;

public class WorksheetsDisplayActivity extends FragmentActivity
        implements TopicFragment.OnTopicSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topicworksheet);
        setupFragment();

    }

    private Fragment setupFragment() {
        FragmentTransaction transaction;
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new WorksheetFragment();

        transaction.add(R.id.worksheet_fragment, fragment, "Result_Frag");
        transaction.commit();

        return fragment;
    }

    @Override
    public void onTopicSelected(String topicName) {
        fragmentRefresher();
        Fragment fragment = setupFragment();
        Bundle b = new Bundle();
        b.putString("topicKey", topicName);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.worksheet_fragment, fragment).commit();
    }

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

}