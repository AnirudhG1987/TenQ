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

import com.coachgecko.tenq.Questions.QuestionHolderActivity;
import com.coachgecko.tenq.R;
import com.coachgecko.tenq.WelcomeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputAge, inputGrade;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private Spinner spinnerCountry;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String countrySelected;

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
        inputAge = (EditText) findViewById(R.id.your_age);
        inputGrade = (EditText) findViewById(R.id.your_grade);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        spinnerCountry = (Spinner) findViewById(R.id.countryList);


        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                countrySelected = (String) parent.getItemAtPosition(position);
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

                final int age,grade;

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(inputAge.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Your Age!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    age = Integer.parseInt(inputAge.getText().toString().trim());
                }

                if (TextUtils.isEmpty(inputGrade.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Your Grade!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    grade = Integer.parseInt(inputGrade.getText().toString().trim());
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

                                    FirebaseUser userFromRegistration = task.getResult().getUser();
                                    String userId = userFromRegistration.getUid();

                                    Student student = Student.builder().age(age).password(password)
                                            .fullName(name).country(countrySelected).grade(grade)
                                            .email(email).pointsEarned(0).starsCollected(0).build();

                                    FirebaseDatabase.getInstance().getReference("students").child(userId).setValue(student);


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