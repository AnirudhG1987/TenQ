package com.coachgecko.tenq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.coachgecko.tenq.Authentication.Student;
import com.coachgecko.tenq.Questions.Question;
import com.coachgecko.tenq.Worksheets.WorksheetsDisplayActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.R.attr.data;
import static android.R.attr.key;


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

    private String userId;

    @InjectView(R.id.nameWelcome)
    TextView nameTV;

    @InjectView(R.id.pointsEarned)
    TextView pointTV;

    @InjectView(R.id.starsCollected)
    TextView starsTV;

    private Student currStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        ButterKnife.inject(this);

        // this is the current list of subjects that the student registered. This wil be pulled from 
        // the firebase. Currently a text view
        
        // Need to format it. 
        


        // Get the current user ID.
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Access the firebase node under the userID.
        Query query = FirebaseDatabase.getInstance().getReference("students").child(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currStudent = dataSnapshot.getValue(Student.class);

                String text = "Welcome " + currStudent.getFullName().toString();
                nameTV.setText(text);

                String starsTxt = "Stars Collected " + currStudent.getStarsCollected();
                starsTV.setText(starsTxt);

                String pointsTxt = "Points Earned " + currStudent.getPointsEarned();
                pointTV.setText(pointsTxt);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


        // On clicking the subject, open the Worksheet Display Activity and send info on current Grade of the student.
        TextView math = (TextView)findViewById(R.id.math);
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currStudent!=null) {
                    Intent intent = new Intent(WelcomeScreenActivity.this, WorksheetsDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("grade", currStudent.getGrade());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

    }

}
