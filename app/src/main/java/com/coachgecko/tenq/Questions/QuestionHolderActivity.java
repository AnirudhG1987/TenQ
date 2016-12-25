package com.coachgecko.tenq.Questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.coachgecko.tenq.R;

public class QuestionHolderActivity extends AppCompatActivity {

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_holder);

/*        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        QuestionFragment qsf = new QuestionFragment();
        transaction.add(R.id.question,qsf,"Question_Frag");
        transaction.commit();
  */
        generateQuestion();

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
