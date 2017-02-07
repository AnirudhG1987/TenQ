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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuestionHolderActivity extends AppCompatActivity implements QuestionFragment.OnRadioButtonSelectedListener{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<QuestionClass> mquestionsList;
    private ArrayList<String> manswersSelectedList;

    private DatabaseReference mfiredatabaseRef;


    private FragmentTransaction transaction;
    private int questionNo;

    private TextView questionNoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mquestionsList = new ArrayList<>();
        manswersSelectedList = new ArrayList<>();
        /*
        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("questions");

        Query latestAttendance = mfiredatabaseRef.limitToFirst(10);
        //mfiredatabaseRef.addChildEventListener(new ChildEventListener() {
        latestAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                QuestionClass questionClass = dataSnapshot.getValue(QuestionClass.class);
                questionClass.setKey(dataSnapshot.getKey());
                mquestionsList.add(questionClass);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                QuestionClass questionClass = dataSnapshot.getValue(QuestionClass.class);
                questionClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mquestionsList.size();i++){
                    QuestionClass a = mquestionsList.get(i);
                    if(a.getKey() != null && a.getKey().contains(questionClass.getKey()))
                    {
                        break;
                    }
                }
                mquestionsList.set(i,questionClass);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                QuestionClass questionClass = dataSnapshot.getValue(QuestionClass.class);
                questionClass.setKey(dataSnapshot.getKey());
                int i=0;
                for(;i<mquestionsList.size();i++){
                    QuestionClass a = mquestionsList.get(i);
                    if(a.getKey() != null && a.getKey().contains(questionClass.getKey()))
                    {
                        break;
                    }
                }
                mquestionsList.remove(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/

        for(int i=0;i<10;i++) {
            QuestionClass q = new QuestionClass("Math","Fraction",2,"This is question No "+(i+1),
                    "answer");

            ArrayList<String> options = new ArrayList<>();
            options.add("answer");
            options.add("Option 1");
            options.add("Option 2");
            options.add("Option 3");
            q.setOptions(options);
            Collections.shuffle(q.getOptions());

            mquestionsList.add(q);
            manswersSelectedList.add("");
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_holder);

        questionNo = 1;

        generateQuestion(mquestionsList.get(0));

        questionNoTextView = (TextView) findViewById(R.id.questionNo) ;
        if(questionNoTextView!=null) {
            questionNoTextView.setText("Question No " + questionNo);
        }

        final Button nextButton = (Button) findViewById(R.id.btn_next);
        final Button prevButton = (Button) findViewById(R.id.btn_prev);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionNo < mquestionsList.size()) {
                    questionNo += 1;
                    questionNoTextView.setText("Question No "+questionNo);
                    generateQuestion(mquestionsList.get(questionNo-1));
                    if(questionNo == mquestionsList.size()) {
                        nextButton.setText("Submit");
                    }
                }
                else{
                    checkSolution();
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(questionNo > 1) {
                    questionNo -= 1;
                    questionNoTextView.setText("Question No "+questionNo);
                    generateQuestion(mquestionsList.get(questionNo-1));
                    if(questionNo == mquestionsList.size()-1) {
                        nextButton.setText("Next");
                    }
                }
            }
        });

    }

    public void checkSolution() {
        int noofQuestionsCorrect = 0, noofQuestionsAttempted = 0;
        for(int i=0;i<mquestionsList.size(); i++) {
            if (!mquestionsList.get(i).getAnswerText().isEmpty()) {
                if (mquestionsList.get(i).getAnswerText().equals(manswersSelectedList.get(i))) {
                    noofQuestionsCorrect += 1;
                }
            }
        }
    }

    public void generateQuestion(QuestionClass question) {


        Fragment f = getSupportFragmentManager().findFragmentById(R.id.question);
        if (f!=null) {
            getSupportFragmentManager().beginTransaction().
                    remove(f).commit();
        }

        QuestionFragment qsf = new QuestionFragment();

        Bundle b = new Bundle();
        b.putString("question",question.getQuestionText());
        // shuffle the questions and use the question no as the seed.
        Random rnd = new Random(questionNo);
        String answer = manswersSelectedList.get(questionNo-1);
        int indexofAns;
        if(!answer.isEmpty()) {
            indexofAns = question.getOptions().indexOf(answer);
        }
        else{
            indexofAns = -1;
        }
        b.putStringArrayList("options", question.getOptions());
        b.putInt("index", indexofAns);
        qsf.setArguments(b);

        getSupportFragmentManager().beginTransaction().replace(R.id.question, qsf).commit();

    }

    @Override
    public void onAnswerClicked(String answer) {
        manswersSelectedList.set(questionNo -1, answer);
    }
}
