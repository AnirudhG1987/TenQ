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
import com.coachgecko.tenq.R;
import com.coachgecko.tenq.Worksheets.WorksheetsDisplayActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class TopicsActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Topic> mtopicsList;

    private FirebaseRecyclerAdapter<Topic, TopicHolder> adapter;

    private DatabaseReference mfiredatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.inject(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("courses").child("math")
                .child("MA06").child("topics");

        setupTopics();
    }

    private void setupTopics() {
        mtopicsList = new ArrayList<>();
        // mtopicsList.add(Topic.builder().topicName("Arbit").build());
        Query query = mfiredatabaseRef.orderByValue().equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String topicKey = data.getKey();
                    Topic topic = Topic.builder().topicName(topicKey).build();
                    topic.setKey(topicKey);
                    mtopicsList.add(topic);
                }
           }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void attachRecyclerViewAdapter() {

        adapter = new FirebaseRecyclerAdapter<Topic, TopicHolder>(
                Topic.class, R.layout.fragment_topic, TopicHolder.class, mfiredatabaseRef) {

            @Override
            public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_topic, parent, false);

                return new TopicHolder(itemView);
            }

            @Override
            protected void populateViewHolder(TopicHolder v, Topic model, int position) {

                v.topicName.setText(model.getTopicName());
            }

            @Override
            public void onBindViewHolder(TopicHolder holder, int position) {
                if (mtopicsList != null) {
                    Topic itemTopic = mtopicsList.get(position);
                    holder.bindTopic(itemTopic);
                }
            }

            @Override
            public int getItemCount() {
                //if(mtopicsList!=null) {
                return mtopicsList.size();

            }

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
        private Topic mtopic;

        public TopicHolder(View v) {
            super(v);

            topicName = (TextView) v.findViewById(R.id.searchTopic);
            v.setOnClickListener(this);
        }

        public void bindTopic(Topic topic) {

            mtopic = topic;
            topicName.setText(mtopic.getTopicName());
        }


        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, WorksheetsDisplayActivity.class);
            intent.putExtra("topicKey", mtopic.getKey());
            context.startActivity(intent);
        }
    }


}
