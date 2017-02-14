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

import static com.coachgecko.tenq.R.id.itemdes;
import static com.coachgecko.tenq.R.id.itemname;

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

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("topics").child(topicKey).
                child("worksheets");
        System.out.println("CHECK THIS " + mfiredatabaseRef.toString());
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

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //figure out how to get this info from worksheets
                    String key = data.getKey();
                    System.out.println("CHECK THIS KEY " + key);
                    mworkSheetList.add(Worksheet.builder().key(key).name(key).description("This is a Worksheet").build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<Worksheet, WorksheetHolder>(
                Worksheet.class, R.layout.activity_worksheetitem,
                WorksheetHolder.class, mfiredatabaseRef) {

            @Override
            public WorksheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_worksheetitem, parent, false);

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
        private Worksheet mworksheet;

        public WorksheetHolder(View v) {
            super(v);

            worksheetName = (TextView) v.findViewById(itemname);
            worksheetDescription = (TextView) v.findViewById(itemdes);
            v.setOnClickListener(this);
        }

        public void bindWorksheet(Worksheet worksheet) {

            mworksheet = worksheet;
            worksheetName.setText(mworksheet.getName());
            worksheetDescription.setText(mworksheet.getDescription());
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
