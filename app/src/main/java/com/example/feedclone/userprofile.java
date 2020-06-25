package com.example.feedclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userprofile extends AppCompatActivity {

    DatabaseReference databaseReference;
    User user;
    String uniqueIdlogin = null;
    String uniqueIdFeed = null;

    TextView profileName;
    TextView nameofCurrentUser, usernameofCurrentUser, ageofCurrentUser;
    Button signoutButton;

    ImageView edit;

    RelativeLayout addnewfeed, viewmyfeed, viewallfeed;
    String uniqueId = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);



        Intent intent = getIntent();
        uniqueIdlogin = intent.getStringExtra("useridfordatabase");

        Intent intent1 = getIntent();
        uniqueIdFeed = intent.getStringExtra("useridfordatabase");

        if(uniqueIdlogin.equals(null)) {
            uniqueId = uniqueIdFeed;
        } else {
            uniqueId = uniqueIdlogin;
        }


        profileName = findViewById(R.id.firstnameTextViewinProfile);
        signoutButton = findViewById(R.id.signoutButton);
        nameofCurrentUser = findViewById(R.id.nameTextviewinProfile);
        usernameofCurrentUser = findViewById(R.id.usernameTextviewinProfile);
        ageofCurrentUser = findViewById(R.id.ageTextviewinProfile);

        addnewfeed = findViewById(R.id.addnewfeedButton);
        viewmyfeed = findViewById(R.id.viewmyfeedButton);
        viewallfeed = findViewById(R.id.viewalluserfeedButton);

        edit = findViewById(R.id.editbutton);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(userprofile.this, "Funtion Currently Not Avalible", Toast.LENGTH_LONG).show();
            }
        });


        //new feed intent
        addnewfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userprofile.this, addnewfeedactivity.class);
                intent.putExtra("uniqueuserid", uniqueId);
                startActivity(intent);
            }
        });

        //my feed intent
        viewmyfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userprofile.this, viewmyfeedactivity.class);
                intent.putExtra("uniqueuserid", uniqueId);
                intent.putExtra("usernametodisplay", "All Your");
                intent.putExtra("canbecontrolled", true);
                startActivity(intent);
            }
        });

        //allfeedintent
        viewallfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userprofile.this, viewallfeedactivity.class);
                intent.putExtra("uniqueuserid", uniqueId);
                startActivity(intent);
            }
        });

        // Sign out Button
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User newUser = snapshot.getValue(User.class);
                    if(newUser.getId().equals(uniqueId)) {
                        profileName.setText("Hi, " + newUser.getFirstname().substring(0, 1).toUpperCase() + newUser.getFirstname().substring(1));
                        nameofCurrentUser.setText("Name : " + newUser.getFirstname().substring(0, 1).toUpperCase() + newUser.getFirstname().substring(1)
                                + " " + newUser.getLastname().substring(0, 1).toUpperCase() + newUser.getLastname().substring(1));
                        usernameofCurrentUser.setText("Username : " + newUser.getUsername());
                        ageofCurrentUser.setText("Age : " + newUser.getAge());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
