package com.coachgecko.tenq.Results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coachgecko.tenq.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ResultSummaryFragment extends Fragment {

    private String mTopic;
    private DatabaseReference mfiredatabaseRef;
    private List<String> mTopicsList;

    public ResultSummaryFragment() {
        // Required empty public constructor
    }

    private static int starsImageRetriever(int noOfStars) {
        switch (noOfStars) {

            case 2:
                return R.mipmap.twostars;
            case 3:
                return R.mipmap.threestars;
            case 4:
                return R.mipmap.fourstars;
            case 5:
                return R.mipmap.fivestars;
            default:
                return R.mipmap.onestar;
        }

    }

    private static int starsCalculator(int noOfQuestions, int noOfCorrectAns) {
        double percentage = (double) noOfCorrectAns / (double) noOfQuestions;
        if (percentage < 0.3) {
            return 1;
        } else if (percentage < 0.5) {
            return 2;
        } else if (percentage < 0.7) {
            return 3;
        } else if (percentage > 0.9 && percentage < 1) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_result_display, container, false);

        ImageView starImg = (ImageView) rootview.findViewById(R.id.resultImg);

        int noOfQuestions = getArguments().getInt("questions");
        int noOfCorrectAns = getArguments().getInt("answers");

        int noOfStars = starsCalculator(noOfQuestions, noOfCorrectAns);

        starImg.setImageResource(starsImageRetriever(noOfStars));
        TextView scoreTxtView = (TextView) rootview.findViewById(R.id.resultScore);

        scoreTxtView.setText(scoreTxtView.getText().toString() + noOfCorrectAns + "/" + noOfQuestions);

        return rootview;
    }

}
