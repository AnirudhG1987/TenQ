package com.coachgecko.tenq.Questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coachgecko.tenq.R;

/**
 * Created by Anirudh on 12/24/2016.
 */

public class QuestionFragment extends Fragment {

    public QuestionFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String question="", option1="", option2="", option3="", option4="";
        if(getArguments()!=null) {
            question = getArguments().getString("question");
            option1 = getArguments().getString("option1");
            option2 = getArguments().getString("option2");
            option3 = getArguments().getString("option3");
            option4 = getArguments().getString("option4");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_question, container, false);

        TextView questionTextView = (TextView) rootview.findViewById(R.id.questionText);
        RadioButton option1Radio = (RadioButton) rootview.findViewById(R.id.radio_1);
        RadioButton option2Radio = (RadioButton) rootview.findViewById(R.id.radio_2);
        RadioButton option3Radio = (RadioButton) rootview.findViewById(R.id.radio_3);
        RadioButton option4Radio = (RadioButton) rootview.findViewById(R.id.radio_4);

        questionTextView.setText(question);
        option1Radio.setText(option1);
        option2Radio.setText(option2);
        option3Radio.setText(option3);
        option4Radio.setText(option4);

        RadioGroup radioOptions = (RadioGroup) rootview.findViewById(R.id.answersRadio);

        radioOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                Toast.makeText(getActivity(), "" + checkedId, Toast.LENGTH_SHORT).show();
            }
        });

        return rootview;
    }


}
