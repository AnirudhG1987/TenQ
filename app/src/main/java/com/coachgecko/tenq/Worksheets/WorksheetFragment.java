package com.coachgecko.tenq.Worksheets;

import android.content.Context;
import android.content.Intent;
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
import com.coachgecko.tenq.Questions.QuestionHolderActivity;
import com.coachgecko.tenq.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class WorksheetFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Worksheet> mworkSheetList;

    private FirebaseRecyclerAdapter<Worksheet, WorksheetHolder> adapter;

    private DatabaseReference mFiredatabaseRef;

    private String currUserID;

    public WorksheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_recyclerview, container, false);

        String topicKey = "";

        String grade = "";
        if (getArguments() != null) {
            topicKey = getArguments().getString("topicKey");
            grade = getArguments().getString("grade");
        }

        // get current user ID.
        currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerList);

        // Figure out why divider decoration item cannot be used .. problem with the getApplicationContext()
        // calls facebook... 

        // mRecyclerView.addItemDecoration(new DividerItemDecoration(
        //       getApplicationContext()
        // ));

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        // TODO make the subject an input
        mFiredatabaseRef = FirebaseDatabase.getInstance().getReference("courses").child("math")
                .child(grade).child("topics").child(topicKey).child("worksheets");

        setupWorksheets();

        super.onCreate(savedInstanceState);

        if (adapter != null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();

        return rootview;
    }

    private void setupWorksheets() {


        mworkSheetList = new ArrayList<>();

        Query query = mFiredatabaseRef;


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 for (DataSnapshot data : dataSnapshot.getChildren()) {

                     // This gets the list of worksheet ids under the Topic Name
                    final String key = data.getValue(String.class);
                    /// THIS IS to get worksheet name and description , think of something better
                    Query query = FirebaseDatabase.getInstance().getReference("worksheets").child(key).child("details");
                    if (query != null) {


                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final String name = dataSnapshot.child("name").getValue().toString();
                            final String description = dataSnapshot.child("description").getValue().toString();
                            // this is to get the score and no of stars
                            Query query = FirebaseDatabase.getInstance()
                                    .getReference("finishedworksheets").child(currUserID)
                                    .child(key);

                            // This now downloads the entire finished worksheet, along with questions and answers
                            // is there a better way TODO

                            if (query != null) {
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getValue() != null) {

                                            long noOfCorrectQuestions = (long)dataSnapshot.child("noOfCorrectQuestions").getValue();
                                            long noOfQuestions = (long)dataSnapshot.child("noOfQuestions").getValue();


                                            mworkSheetList.add(Worksheet.builder().key(key)
                                                    .name(name)
                                                    .description(description)
                                                    .score(noOfCorrectQuestions+"/"+noOfQuestions)
                                                    .noOfStars((long)dataSnapshot.child("noOfStars").getValue())
                                                    .build());

                                        } else {

                                            mworkSheetList.add(Worksheet.builder().key(key)
                                                    .name(name)
                                                    .description(description).build());

                                        }
                                        adapter.notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<Worksheet, WorksheetHolder>(
                Worksheet.class, R.layout.activity_worksheet_item,
                WorksheetHolder.class, mFiredatabaseRef) {

            @Override
            public WorksheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_worksheet_item, parent, false);

                return new WorksheetHolder(itemView);
            }

            @Override
            protected void populateViewHolder(WorksheetHolder v, Worksheet model, int position) {
                v.worksheetName.setText(model.getName());
                v.worksheetDescription.setText(model.getDescription());
            }

            @Override
            public void onBindViewHolder(WorksheetHolder holder, int position) {
                Worksheet worksheet = mworkSheetList.get(position);
                holder.bindWorksheet(worksheet);
            }

            @Override
            public int getItemCount() {
                return mworkSheetList.size();
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

    public static class WorksheetHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        public TextView worksheetName;
        public TextView worksheetDescription;
        public TextView worksheetScore;
        public ImageView worksheetImage;

        private Worksheet mworksheet;

        public WorksheetHolder(View v) {
            super(v);

            worksheetName = (TextView) v.findViewById(R.id.worksheetName);
            worksheetDescription = (TextView) v.findViewById(R.id.worksheetDescription);
            worksheetScore = (TextView) v.findViewById(R.id.worksheetScore);
            worksheetImage = (ImageView) v.findViewById(R.id.worksheetImg);
            v.setOnClickListener(this);
        }

        public void bindWorksheet(Worksheet worksheet) {

            mworksheet = worksheet;
            worksheetName.setText(mworksheet.getName());
            worksheetDescription.setText(mworksheet.getDescription());
            worksheetScore.setText(mworksheet.getScore());
            worksheetImage.setImageResource(R.mipmap.fourstars);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, QuestionHolderActivity.class);
            intent.putExtra("worksheetID", mworksheet.getKey());
            intent.putExtra("pointsEarned",mworksheet.getScore());
            intent.putExtra("starCollected",mworksheet.getNoOfStars());
            context.startActivity(intent);
        }
    }
}
