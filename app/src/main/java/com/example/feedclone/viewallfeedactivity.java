package com.example.feedclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewallfeedactivity extends AppCompatActivity {

    ArrayList<String> useroffeed, userUniqueId;
    TextView userCount, userFullName;
    ListView userListView;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewallfeedactivity);

        final Intent intent = getIntent();
        final String uniqueId = intent.getStringExtra("uniqueuserid");

        userListView = findViewById(R.id.listviewofalluser);

        useroffeed = new ArrayList<String>();
        userUniqueId = new ArrayList<String>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.orderByChild("firstname");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    User newUser = snap.getValue(User.class);
                    if (!newUser.getId().equals(uniqueId)) {
                        useroffeed.add(newUser.getFirstname().substring(0, 1).toUpperCase() + newUser.getFirstname().substring(1)
                                + " " + newUser.getLastname().substring(0, 1).toUpperCase() + newUser.getLastname().substring(1));
                        userUniqueId.add(newUser.getId());
                    }
                }
                CustomeAdapter customeAdapter = new CustomeAdapter();
                userListView.setAdapter(customeAdapter);

                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent1 = new Intent(viewallfeedactivity.this, viewmyfeedactivity.class);
                        intent1.putExtra("uniqueuserid", userUniqueId.get(position));
                        intent1.putExtra("usernametodisplay", useroffeed.get(position));
                        intent1.putExtra("canbecontrolled", false);
                        startActivity(intent1);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    class CustomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return useroffeed.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.allusercustomlayout, null);

            userCount = convertView.findViewById(R.id.feedcountTextview);
            userFullName = convertView.findViewById(R.id.usernameclview);

            userCount.setText(String.valueOf(position + 1) + ". ");
            userFullName.setText(useroffeed.get(position));

            return convertView;
        }
    }
}
