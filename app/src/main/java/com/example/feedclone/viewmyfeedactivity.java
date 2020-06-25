package com.example.feedclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;

public class viewmyfeedactivity extends AppCompatActivity {

    TextView feedCount, feedString, feedDate, feedTime, feedviewerdisplay;

    ImageView like;

    ArrayList<String> feeds, dates, times, feedId;

    String id;

    ListView feedListView;
    DatabaseReference referencefeed;

    RelativeLayout relativeLayout;

    boolean isControlled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmyfeedactivity);

        Intent intent = getIntent();
        id = intent.getStringExtra("uniqueuserid");
        String userview = intent.getStringExtra("usernametodisplay") + " Feeds";
        isControlled = intent.getBooleanExtra("canbecontrolled", false);

        feedviewerdisplay = findViewById(R.id.allmyfeedsfield);

        feedviewerdisplay.setText(userview);


        feedListView = findViewById(R.id.listviewoffeed);

        referencefeed = FirebaseDatabase.getInstance().getReference("Feeds").child(id);

        feeds = new ArrayList<String>();
        dates = new ArrayList<String>();
        times = new ArrayList<String>();
        feedId = new ArrayList<String>();

        referencefeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                feedId.clear();
                feeds.clear();
                dates.clear();
                times.clear();
                boolean isAvalibePost = false;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    isAvalibePost = true;
                    FeedClass user = snap.getValue(FeedClass.class);
                    feeds.add(user.getFeed());
                    dates.add(user.getDate());
                    times.add(user.getTime());
                    feedId.add(user.getFeedId());
                }

                if(!isAvalibePost) {
                    Toast.makeText(viewmyfeedactivity.this, "No Post from User", Toast.LENGTH_SHORT).show();
                }

                CustomeAdapter customeAdapter = new CustomeAdapter();
                feedListView.setAdapter(customeAdapter);


                feedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        if (isControlled) {
                            //Alert to delete

                            AlertDialog.Builder builder = new AlertDialog.Builder(viewmyfeedactivity.this);
                            builder.setTitle("Delete Feed");
                            builder.setMessage("Do you want to delete feed?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(getApplicationContext(), "Feed Removed", Toast.LENGTH_LONG).show();
                                            deleteFeed(feedId.get(position));
                                            Toast.makeText(viewmyfeedactivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertdialog = builder.create();
                            alertdialog.show();
                        }

                        return true;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    // class for custome adapter

    class CustomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return feeds.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);

            relativeLayout = convertView.findViewById(R.id.relativelayout1cl);
            like = convertView.findViewById(R.id.likebutton);
            feedCount = convertView.findViewById(R.id.feedcountTextview);
            feedString = convertView.findViewById(R.id.feedtextview);
            feedDate = convertView.findViewById(R.id.datetextview);
            feedTime = convertView.findViewById(R.id.timetextview);

            feedCount.setText(String.valueOf(position + 1) + ".");
            feedString.setText(feeds.get(position));
            feedDate.setText(dates.get(position));
            feedTime.setText(times.get(position));

            if (isControlled) {
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like.setImageResource(R.drawable.liked);
                    }
                });
            }

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewmyfeedactivity.this, "You Liked Post " + (int) (position + 1), Toast.LENGTH_SHORT)
                            .show();
                }
            });


            if (position % 2 == 0) {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.color1));
            } else {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.color2));
            }

            return convertView;
        }

        public View getImageView(int postition, View convertView) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);
            like = convertView.findViewById(R.id.likebutton);
            like.setImageResource(R.drawable.liked);
            return convertView;
        }
    }

    public void deleteFeed(String idToDelete) {
        DatabaseReference feeddeleterreference = FirebaseDatabase.getInstance().getReference("Feeds").child(id).child(idToDelete);
        feeddeleterreference.removeValue();
    }

}


