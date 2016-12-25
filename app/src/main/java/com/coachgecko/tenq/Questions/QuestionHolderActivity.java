package com.coachgecko.tenq.Questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coachgecko.tenq.R;

public class QuestionHolderActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private int questionNo;

    private TextView questionNoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        RadioGroup radioOptions = (RadioGroup) findViewById(R.id.answersRadio);

        radioOptions.setOnClickListener(new View.OnClickListener(){
            @Override

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
