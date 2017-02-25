package com.coachgecko.tenq.Questions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coachgecko.tenq.R;
import com.coachgecko.tenq.Results.ResultsDisplayActivity;
import com.coachgecko.tenq.Results.WorksheetResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static android.R.attr.key;

public class QuestionHolderActivity extends AppCompatActivity implements QuestionFragment.OnRadioButtonSelectedListener {

    int noOfQuestionsCorrect;
    int noOfQuestionsAttempted;
    int noOfQuestions;
    private ArrayList<Question> mquestionsList;
    // private ArrayList<String> manswersSelectedList;
    // private ArrayList<Boolean> manswersResultList;
    private DatabaseReference mfiredatabaseRef;
    private int questionNo;
    private String worksheetID;

    private TextView questionNoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        noOfQuestions = 0;
        questionNo = 1;
        mquestionsList = new ArrayList<>();
        // manswersSelectedList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            worksheetID = extras.getString("worksheetID");
        }


        mfiredatabaseRef = FirebaseDatabase.getInstance().getReference("worksheets").child(worksheetID).child("questions");

        setupQuestions();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_holder);

        questionNoTextView = (TextView) findViewById(R.id.que);
        if (questionNoTextView != null) {
            questionNoTextView.setText("Question No " + questionNo);
        }

        final Button nextButton = (Button) findViewById(R.id.btn_next);
        final Button prevButton = (Button) findViewById(R.id.btn_prev);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionNo < mquestionsList.size()) {
                    questionNo += 1;
                    questionNoTextView.setText("Question No " + questionNo);
                    populateQuestion(mquestionsList.get(questionNo - 1));
                    if (questionNo == mquestionsList.size()) {
                        nextButton.setText("Submit");
                    }
                } else {

                    checkSolution();

                    Context context = v.getContext();
                    Intent intent = new Intent(context, ResultsDisplayActivity.class);
                    intent.putExtra("questions", noOfQuestions);
                    intent.putExtra("worksheetID", worksheetID);
                    intent.putExtra("answers", noOfQuestionsCorrect);
                    context.startActivity(intent);

                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questionNo > 1) {
                    questionNo -= 1;
                    questionNoTextView.setText("Question No " + questionNo);
                    populateQuestion(mquestionsList.get(questionNo - 1));
                    if (questionNo == mquestionsList.size() - 1) {
                        nextButton.setText("Next");
                    }
                }
            }
        });

    }

    public void recheckSubmit() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());

        // set title
        alertDialogBuilder.setTitle("Submit Worksheet");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click Yes to Submit!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkSolution();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void setupQuestions() {

        mquestionsList = new ArrayList<>();

        Query query = mfiredatabaseRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    System.out.println("CHECK THIS " + data.toString());
                    Question q = data.getValue(Question.class);
                    Collections.shuffle(q.getOptions());
                    mquestionsList.add(q);
                    noOfQuestions += 1;

                    //     manswersSelectedList.add("");

                    if (mquestionsList.size() == 1) {
                        populateQuestion(mquestionsList.get(0));
                    }
                    System.out.println("CHECK THIS KEY " + key);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void checkSolution() {
        noOfQuestionsAttempted = noOfQuestions;
        //manswersResultList = new ArrayList<>(noOfQuestions);
        for(int i=0;i<mquestionsList.size(); i++) {
            //   if (!manswersSelectedList.get(i).isEmpty()) {
            if (!mquestionsList.get(i).getAnswerSelected().isEmpty()) {
                if (mquestionsList.get(i).getAnswer().equals(mquestionsList.get(i).getAnswerSelected())) {
                    noOfQuestionsCorrect += 1;
                    mquestionsList.get(i).setResult(true);
                } else {
                    mquestionsList.get(i).setResult(false);
                }
            } else {
                noOfQuestionsAttempted -= 1;
                mquestionsList.get(i).setResult(false);
            }
        }

        populateFirebaseResult();
    }

    public void populateFirebaseResult() {

        /// TODO no of stars to be added

        WorksheetResult result = WorksheetResult.builder().questionsList(mquestionsList)
                .worksheetID(worksheetID).studentID("id1").noofCorrectQuestions(noOfQuestionsCorrect)
                .noofQuestions(mquestionsList.size()).noOfQuestionsAttempted(noOfQuestionsAttempted)
                .noOfStars(4).score(noOfQuestionsCorrect + "/" + mquestionsList.size()).build();

        DatabaseReference resultFirebaseRef = FirebaseDatabase.getInstance()
                .getReference("finishedworksheets").child("id1").child(worksheetID);

        resultFirebaseRef.setValue(result);
    }

    public void populateQuestion(Question question) {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.question);
        if (f!=null) {
            getSupportFragmentManager().beginTransaction().
                    remove(f).commit();
        }

        QuestionFragment qsf = new QuestionFragment();

        Bundle b = new Bundle();
        b.putString("question", question.getQuestion());

        Random rnd = new Random(questionNo);
        String answer = mquestionsList.get(questionNo - 1).getAnswerSelected();

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
        mquestionsList.get(questionNo - 1).setAnswerSelected(answer);
    }
}
