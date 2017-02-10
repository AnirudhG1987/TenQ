package com.coachgecko.tenq.Worksheets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coachgecko.tenq.DividerItemDecoration;
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

import butterknife.ButterKnife;

import static com.coachgecko.tenq.R.id.itemdes;
import static com.coachgecko.tenq.R.id.itemname;

public class WorksheetsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Worksheet> mworkSheetList;

    private FirebaseRecyclerAdapter<Worksheet, WorksheetsActivity.WorksheetHolder> adapter;

    private DatabaseReference mfiredatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.inject(this);

        String topicKey = "";

        // Get the Topic Name from TopicsActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            topicKey = extras.getString("topicKey");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("topics").child(topicKey).
                child("worksheets");
        System.out.println("CHECK THIS " + mfiredatabaseRef.toString());
        setupWorksheets();
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
                Worksheet.class, R.layout.activity_worksheetitem, WorksheetHolder.class, mfiredatabaseRef) {

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
    protected void onStart() {
        if (adapter != null) {
            adapter.cleanup();
        }
        attachRecyclerViewAdapter();
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
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

