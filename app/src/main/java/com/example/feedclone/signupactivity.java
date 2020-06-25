package com.example.feedclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class signupactivity extends AppCompatActivity {

    EditText passwordUser, usernameUser, firstnameUser, lastnameUser;
    SeekBar ageUser;
    TextView userAge;
    Button newUserSignup;
    ArrayList<String> usernameList;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);

        usernameList = new ArrayList<String>();


        firstnameUser = findViewById(R.id.firstnameEdittextview);
        lastnameUser = findViewById(R.id.lastnameEdittextview);
        usernameUser = findViewById(R.id.newUsernameEdittextview);
        passwordUser = findViewById(R.id.newPasswordEdittextview);
        ageUser = findViewById(R.id.ageSeekbar);
        userAge = findViewById(R.id.ageOfnewUserTextview);
        newUserSignup = findViewById(R.id.newUserSignupButton);

        String uniqueid = null;

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        ageUser.setMax(99);
        ageUser.setProgress(18);
        ageUser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 15) {
                    userAge.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


            newUserSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //        new user signupus
                    final String username = usernameUser.getText().toString();
                    final String password = passwordUser.getText().toString();
                    if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                        //data to realtime database
                        final String keyId = databaseReference.push().getKey();
                        addNewUserDetail(keyId);
                        //data to authentication
                        mAuth.createUserWithEmailAndPassword(username + "@gmail.com", password)
                                .addOnCompleteListener(signupactivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                        } else {
                                            Toast.makeText(signupactivity.this, "No!" + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Intent intent = new Intent(signupactivity.this, MainActivity.class);
                        startActivity(intent);
                    }  else {
                        Toast.makeText(signupactivity.this, "Empty Field", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void addNewUserDetail(String uniqueId) {
        final String firstName = firstnameUser.getText().toString();
        final String lastName = lastnameUser.getText().toString();
        final String username = usernameUser.getText().toString() + "@gmail.com";
        final String password = passwordUser.getText().toString();
        final String age = userAge.getText().toString();
        final String[] uniqueIdToAdd = {uniqueId};

        Log.i("Username list size", String.valueOf(usernameList.size()));

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(age)) {
            uniqueIdToAdd[0] = databaseReference.push().getKey();
            User user = new User(uniqueIdToAdd[0], firstName, lastName, username, password, age);
            databaseReference.child(uniqueIdToAdd[0]).setValue(user);
        } else {
            Toast.makeText(signupactivity.this, "Empty Field", Toast.LENGTH_SHORT).show();
        }


    }

}

