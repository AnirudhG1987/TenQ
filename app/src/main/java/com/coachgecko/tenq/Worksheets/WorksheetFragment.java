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

import com.coachgecko.tenq.Questions.QuestionHolderActivity;
import com.coachgecko.tenq.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorksheetFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Worksheet> mworkSheetList;

    private FirebaseRecyclerAdapter<Worksheet, WorksheetHolder> adapter;

    private DatabaseReference mfiredatabaseRef;

    public WorksheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_recyclerview, container, false);

        String topicKey = "";
        if (getArguments() != null) {
            topicKey = getArguments().getString("topicKey");
        }

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerList);

        // Figure out why divider decoration item cannot be used .. problem with the getApplicationContext()
        // calls facebook... 

        // mRecyclerView.addItemDecoration(new DividerItemDecoration(
        //       getApplicationContext()
        // ));

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("courses").child("math")
                .child("MA06").child("topics").child(topicKey).child("worksheets");

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
        Query query = mfiredatabaseRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("SNAPSHOT " + dataSnapshot.toString() + " ");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String key = data.getKey();
                    System.out.println("this is the key " + data.getKey() + " ");

                    /// THIS IS to get worksheet name and description , think of something better
                    Query query = FirebaseDatabase.getInstance().getReference("worksheets").child(key).child("details");
                    if (query != null) {


                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("SNAPSHOT 2" + dataSnapshot.toString() + " key " + key + " value " + dataSnapshot.getValue());

                            final String name = dataSnapshot.child("name").getValue().toString();
                            final String description = dataSnapshot.child("description").getValue().toString();
                            // this is to get the score and no of stars
                            Query query = FirebaseDatabase.getInstance()
                                    .getReference("finishedworksheets").child("id1")
                                    .child(key).child("score");

                            if (query != null) {
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getValue() != null) {

                                            mworkSheetList.add(Worksheet.builder().key(key)
                                                    .name(name)
                                                    .description(description)
                                                    .score(dataSnapshot.getValue().toString()).build());

                                        } else {

                                            mworkSheetList.add(Worksheet.builder().key(key)
                                                    .name(name)
                                                    .description(description).build());

                                        }
                                        adapter.notifyDataSetChanged();

                                        System.out.println("worksheet added " + mworkSheetList.size());


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
                WorksheetHolder.class, mfiredatabaseRef) {

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
            context.startActivity(intent);
        }
    }
}
