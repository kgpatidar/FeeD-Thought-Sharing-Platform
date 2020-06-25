package com.example.feedclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addnewfeedactivity extends AppCompatActivity {

    TextView currentDate, currentTime, currentusername;
    Button newfeedUpdateButton;
    EditText feedText;
    String fName, lName;
    DatabaseReference databaseReference, feedReference;
    String uniqueId;
    TextView getUserfullname;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewfeedactivity);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueuserid");

        currentDate = findViewById(R.id.dateEditTextforNewfeed);
        currentTime = findViewById(R.id.timeEditTextforNewfeed);
        newfeedUpdateButton = findViewById(R.id.addingNewFeedButton);
        currentusername = findViewById(R.id.addnewfeedusername);
        feedText = findViewById(R.id.newfeedEdittext);
        getUserfullname = findViewById(R.id.addnewfeedusername);

        //creating new database for feed reference
        feedReference = databaseReference = FirebaseDatabase.getInstance().getReference("Feeds").child(uniqueId);

        //new feed update
        newfeedUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String feedString = feedText.getText().toString();
                String date = currentDate.getText().toString();
                String time = currentTime.getText().toString();
                String currentUsername = getUserfullname.getText().toString();

                if(!TextUtils.isEmpty(feedString) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(currentUsername)) {
                    String key = feedReference.push().getKey();
                    FeedClass feedClass = new FeedClass(key, currentUsername, feedString, date, time);
                    feedReference.child(key).setValue(feedClass);
                } else {
                    Toast.makeText(addnewfeedactivity.this, "Empty Field", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(addnewfeedactivity.this, userprofile.class);
                intent.putExtra("useridfordatabase" , uniqueId);
                startActivity(intent);
                finish();
            }
        });


        // Getting system date and time
        Calendar c = Calendar.getInstance();
//        String currentDateString = DateFormat.getDateTimeInstance().format("DD-MM-YYYY");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        df = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = df.format(c.getTime());
        currentDate.setText("Date : " + formattedDate);
        currentTime.setText("Time : " + formattedTime);
        //---------------------------------------------

        //Accessing cuurent user detail
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User newUser = snapshot.getValue(User.class);
                    if(newUser.getId().equals(uniqueId)) {
                        currentusername.setText(newUser.getFirstname().substring(0, 1).toUpperCase() + newUser.getFirstname().substring(1)
                                + " " + newUser.getLastname().substring(0, 1).toUpperCase() + newUser.getLastname().substring(1));
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
