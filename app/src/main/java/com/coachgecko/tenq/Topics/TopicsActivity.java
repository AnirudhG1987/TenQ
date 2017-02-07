package com.coachgecko.tenq.Topics;

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

public class TopicsActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<TopicClass> mtopicsList;

    private FirebaseRecyclerAdapter<TopicClass, TopicHolder> adapter;

    private DatabaseReference mfiredatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("Grades");



        setupTopics();
    }

    private void setupTopics() {

        Query query = mfiredatabaseRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mtopicsList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    TopicClass topicClass = data.getValue(TopicClass.class);
                    topicClass.setKey(data.getKey());
                    mtopicsList.add(topicClass);
                }
           }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<TopicClass, TopicHolder>(
                TopicClass.class, R.layout.activity_topics, TopicHolder.class, mfiredatabaseRef) {

            @Override
            public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_topics, parent, false);

                return new TopicHolder(itemView);
            }

            @Override
            protected void populateViewHolder(TopicHolder v, TopicClass model, int position) {
                v.topicName.setText(model.getTopicName());
            }

            @Override
            public void onBindViewHolder(TopicHolder holder, int position) {
                TopicClass itemTopic = mtopicsList.get(position);
                holder.bindTopic(itemTopic);
            }

            /*@Override
            public int getItemCount() {
                return mtopicsList.size();
            }*/

        };
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart(){
        if(adapter!=null) {
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

    public static class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2

        public TextView topicName;
        private TopicClass mtopic;

        public TopicHolder(View v) {
            super(v);

            topicName = (TextView) v.findViewById(R.id.topic);
            v.setOnClickListener(this);
        }

        public void bindTopic(TopicClass topic) {

            mtopic = topic;
            topicName.setText(mtopic.getTopicName());
        }


        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, QuestionHolderActivity.class);
            context.startActivity(intent);
        }
    }


}
