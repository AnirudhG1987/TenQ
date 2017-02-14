package com.coachgecko.tenq.Topics;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.coachgecko.tenq.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends Fragment {

    ArrayAdapter<String> topicsSpinnerAdapter;
    OnTopicSelectedListener mCallback;
    private Spinner mTopicsSpinner;
    private String mTopic;
    private View rootview;
    private DatabaseReference mfiredatabaseRef;
    private List<String> mTopicsList;

    public TopicFragment() {
        // Required empty public constructor
    }

    @Override

    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {

            mCallback = (OnTopicSelectedListener) activity;

        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()

                    + " must implement OnTopicSelectedListener");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_topic, container, false);

        mTopicsSpinner = (Spinner) rootview.findViewById(R.id.spinnerTopicList);
        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("courses").child("math")
                .child("MA06").child("topics");

        setupTopicsSpinner();

        return rootview;
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    private void setupTopicsSpinner() {

        mTopicsList = new ArrayList<>();

        Query query = mfiredatabaseRef.orderByValue().equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    final String topicKey = data.getKey();
                    mTopicsList.add(topicKey);
                }

                topicsSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mTopicsList);

                topicsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                mTopicsSpinner.setAdapter(topicsSpinnerAdapter);

            }

            @Override

            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mTopicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    mTopic = selection;
                    mCallback.onTopicSelected(mTopic);
                }


            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {
                mTopic = (String) parent.getItemAtPosition(0);
                mCallback.onTopicSelected(mTopic);
            }

        });

    }

    public interface OnTopicSelectedListener {

        void onTopicSelected(String s);

    }


}
