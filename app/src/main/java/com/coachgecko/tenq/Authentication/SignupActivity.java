package com.coachgecko.tenq.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.coachgecko.tenq.R;
import com.coachgecko.tenq.WelcomeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputName;
    private EditText inputDOB;
    private EditText inputCountry;

    private Button btnSignIn;
    private Button btnSignUp;
    private Button btnResetPassword;
    private Spinner spinnerGrade;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    // Default this to zero to see if any grade was selected.
    private int gradeSelected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputName = (EditText) findViewById(R.id.your_full_name);
        inputCountry = (EditText) findViewById(R.id.countryList);
        spinnerGrade = (Spinner) findViewById(R.id.your_grade);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        inputDOB = (EditText) findViewById(R.id.your_DOB);


        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grades_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gradeSelected = Integer.parseInt((String) parent.getItemAtPosition(position));
            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {
                // mTopic = (String) parent.getItemAtPosition(0);
                // mCallback.onTopicSelected(mTopic);
            }

        });




        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                final String name = inputName.getText().toString().trim();

                final String dob, country;

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(inputDOB.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Your Age!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    dob = inputDOB.getText().toString().trim();
                }

                if (TextUtils.isEmpty(inputCountry.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Your Country!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                     country = inputCountry.getText().toString().trim();
                }

                if (gradeSelected==0) {
                    Toast.makeText(getApplicationContext(), "Select Your Grade!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();


                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {


                                    // get the user information of the student created
                                    FirebaseUser userFromRegistration = task.getResult().getUser();

                                    // get the userID of the student
                                    String userId = userFromRegistration.getUid();


                                    // Create a student object with the details above for the profile
                                    Student student = Student.builder().dob(dob).password(password)
                                            .fullName(name).country(country).grade(gradeSelected)
                                            .email(email).pointsEarned(0).starsCollected(0).build();

                                    // Store the student details under the child of the userID
                                    FirebaseDatabase.getInstance().getReference("students").child(userId).setValue(student);

                                    // once done, start the WelcomeScreen
                                    startActivity(new Intent(SignupActivity.this, WelcomeScreenActivity.class));
                                    finish();
                                }
                            }
                        });




            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}