package com.coachgecko.tenq.Results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coachgecko.tenq.Questions.Question;
import com.coachgecko.tenq.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultDisplayFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Question> mResultsList;

    //private WorksheetResult worksheetResult;

    private FirebaseRecyclerAdapter<Question, WorksheetResultHolder> adapter;

    private DatabaseReference mfiredatabaseRef;

    public ResultDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_recyclerview, container, false);

        String worksheetID = "";
        if (getArguments() != null) {
            worksheetID = getArguments().getString("worksheetID");
        }

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerList);

        // Figure out why divider decoration item cannot be used .. problem with the getApplicationContext()
        // calls facebook...

        // mRecyclerView.addItemDecoration(new DividerItemDecoration(
        //       getApplicationContext()
        // ));

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        System.out.println("worksheet id is " + worksheetID);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("finishedworksheets").child("id1")
                .child(worksheetID).child("questionsList");

        setupWorksheetResult();

        super.onCreate(savedInstanceState);

        if (adapter != null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();

        return rootview;
    }

    private void setupWorksheetResult() {
        mResultsList = new ArrayList<>();
        Query query = mfiredatabaseRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    System.out.println("CHECK THIS " + data.toString());
                    Question q = data.getValue(Question.class);
                    mResultsList.add(q);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<Question, WorksheetResultHolder>(
                Question.class, R.layout.activity_result_item,
                WorksheetResultHolder.class, mfiredatabaseRef) {

            @Override
            public WorksheetResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_result_item, parent, false);

                return new WorksheetResultHolder(itemView);
            }

            @Override
            protected void populateViewHolder(WorksheetResultHolder v, Question model, int position) {
                v.questionTV.setText(model.getQuestion());
                v.answerSelected.setText(model.getAnswerSelected());
                v.correctAns.setText(model.getAnswer());
            }

            @Override
            public void onBindViewHolder(WorksheetResultHolder holder, int position) {
                Question question = mResultsList.get(position);
                holder.bindWorksheetResult(question);
            }

            @Override
            public int getItemCount() {
                return mResultsList.size();
            }

        };
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.cleanup();
        }
    }

    public static class WorksheetResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        public TextView questionTV;
        public TextView answerSelected;
        public TextView correctAns;
        public ImageView resultImg;

        private Question question;

        public WorksheetResultHolder(View v) {
            super(v);

            questionTV = (TextView) v.findViewById(R.id.resultQuestion);
            answerSelected = (TextView) v.findViewById(R.id.answerSelected);
            correctAns = (TextView) v.findViewById(R.id.answerCorrect);
            resultImg = (ImageView) v.findViewById(R.id.resultImg);
            v.setOnClickListener(this);
        }

        public void bindWorksheetResult(Question question) {

            this.question = question;
            questionTV.setText(question.getQuestion());
            answerSelected.setText(question.getAnswerSelected());
            correctAns.setText(question.getAnswer());
            //resultImg.setImageResource(R.mipmap.fourstars);
        }

        @Override
        public void onClick(View v) {
            // later you can show detailed solution
        }
    }

}
