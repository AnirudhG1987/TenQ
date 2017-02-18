package com.coachgecko.tenq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.coachgecko.tenq.Worksheets.WorksheetsDisplayActivity;


/*

    This Activity handles the welcome screen for registered user.
    The Following are displayed.
    
    (TBD)
    1. Avatar 
    2. Grade
    3. Name
    4. Current Level (design a proficiency level tracker)
    5. Maybe points

*/


public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // this is the current list of subjects that the student registered. This wil be pulled from 
        // the firebase. Currently a text view
        
        // Need to format it. 
        
        //On click takes you to the TopicsActivity.
        TextView math = (TextView)findViewById(R.id.math);
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreenActivity.this, WorksheetsDisplayActivity.class);
                startActivity(intent);
            }
        });

    }

}
