package com.coachgecko.tenq.Questions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coachgecko.tenq.R;

import java.util.ArrayList;

/**
 * Created by Anirudh on 12/24/2016.
 */

public class QuestionFragment extends Fragment {

    private OnRadioButtonSelectedListener mCallback;

    public QuestionFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnRadioButtonSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRadioButtonSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String question="", answer="", option1="", option2="", option3="", option4="";
        ArrayList<String> optionsList = new ArrayList<>();
        int indexofAns=-1;
        if(getArguments()!=null) {
            question = getArguments().getString("question");
            optionsList = getArguments().getStringArrayList("options");
            indexofAns = getArguments().getInt("index");
        }

        option1 = optionsList.get(0);
        option2 = optionsList.get(1);
        option3 = optionsList.get(2);
        option4 = optionsList.get(3);

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_question, container, false);

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

        switch (indexofAns){
            case 0:
                option1Radio.setChecked(true);
                break;
            case 1:
                option2Radio.setChecked(true);
                break;
            case 2:
                option3Radio.setChecked(true);
                break;
            case 3:
                option4Radio.setChecked(true);
                break;
            default:
                break;
        }

        RadioGroup radioOptions = (RadioGroup) rootview.findViewById(R.id.answersRadio);

        radioOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) rootview.findViewById(checkedId);
                mCallback.onAnswerClicked(rb.getText().toString());
            }
        });

        return rootview;
    }

    public interface OnRadioButtonSelectedListener{
        void onAnswerClicked(String answer);
    }

}
