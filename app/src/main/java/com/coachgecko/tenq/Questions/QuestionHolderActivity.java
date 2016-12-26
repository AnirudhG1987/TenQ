package com.coachgecko.tenq.Questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coachgecko.tenq.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class QuestionHolderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<QuestionClass> mquestionsList;

    private DatabaseReference mfiredatabaseRef;


    private FragmentTransaction transaction;
    private int questionNo;

    private TextView questionNoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mquestionsList = new ArrayList<>();

        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("questions");

        Query latestAttendance = mfiredatabaseRef.orderByChild("studentName");
        //mfiredatabaseRef.addChildEventListener(new ChildEventListener() {
        latestAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());
                mattendanceList.add(attendanceClass);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mattendanceList.size();i++){
                    AttendanceClass a = mattendanceList.get(i);
                    if(a.getKey() != null && a.getKey().contains(attendanceClass.getKey()))
                    {
                        break;
                    }
                }
                mattendanceList.set(i,attendanceClass);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AttendanceClass attendanceClass = dataSnapshot.getValue(AttendanceClass.class);
                attendanceClass.setKey(dataSnapshot.getKey());int i=0;
                for(;i<mattendanceList.size();i++){
                    AttendanceClass a = mattendanceList.get(i);
                    if(a.getKey() != null && a.getKey().contains(attendanceClass.getKey()))
                    {
                        break;
                    }
                }
                mattendanceList.remove(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_holder);

        questionNo = 1;

        questionNoTextView = (TextView) findViewById(R.id.questionNo) ;
        if(questionNoTextView!=null) {
            questionNoTextView.setText("Question No " + questionNo);
        }

        generateQuestion();

        Button nextButton = (Button) findViewById(R.id.btn_next);
        Button prevButton = (Button) findViewById(R.id.btn_prev);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });

    }

    public void prevQuestion(){
        questionNo-=1;
        questionNoTextView.setText("Question No "+questionNo);
    }

    public void nextQuestion() {
        questionNo+=1;
        questionNoTextView.setText("Question No "+questionNo);
    }

    public void generateQuestion() {


        Fragment f = getSupportFragmentManager().findFragmentById(R.id.question);
        if (f!=null) {
            getSupportFragmentManager().beginTransaction().
                    remove(f).commit();
        }

        QuestionFragment qsf = new QuestionFragment();
        Bundle b = new Bundle();
        b.putString("question","something");
        b.putString("option1","jupiter");
        b.putString("option2","mars");
        b.putString("option3","mercury");
        b.putString("option4","saturn");

        qsf.setArguments(b);

        getSupportFragmentManager().beginTransaction().replace(R.id.question, qsf).commit();

    }

}
