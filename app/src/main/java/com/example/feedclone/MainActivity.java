package com.example.feedclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText usernameText, passwordText;
    Button loginButton;
    TextView signupButton;
    CheckBox rememberme;

    ImageView cloneIcon;

    CoordinatorLayout coordinatorLayout;
    ConstraintLayout constraintLayout;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference, updateRefrence;
    SharedPreferences sharedPreferences;
    boolean isUserlogin = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        usernameText = findViewById(R.id.usernameEdittext);
        passwordText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupbutton);
        rememberme = findViewById(R.id.rememberCheckbox);
        cloneIcon = findViewById(R.id.ImageviewcloneIcon);

        sharedPreferences = getApplicationContext().getSharedPreferences("logindetail", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean currentuserStatus = sharedPreferences.getBoolean("islogin", false);
        String currentUserusername = sharedPreferences.getString("currentUserUsername", "");
        String currentUserpassword = sharedPreferences.getString("currentUserPassword", "");
        Log.i("Current user status", String.valueOf(currentuserStatus));
        if(currentuserStatus) {
            usernameText.setText(currentUserusername);
            passwordText.setText(currentUserpassword);
            rememberme.setChecked(true);
        }

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Original method to accessing data from Firebase datanbase*************
        /*Query query = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("username")
                .equalTo(usernameString);*/

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);




            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // checking internet connection///////
                    if(isNetworkAvailable(MainActivity.this)) {
                    //Login
                        final String emailwithuoutgmail = usernameText.getText().toString().trim();
                        final String email = emailwithuoutgmail + "@gmail.com";
                        final String password = passwordText.getText().toString();
                        final boolean[] isMatched = {false};
                        loginButton.setEnabled(false);

                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Fetching Your Data!", Snackbar.LENGTH_LONG).setDuration(10000);
                        snackbar.show();

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    User user = snap.getValue(User.class);

                                    if (email.equals(user.getUsername()) && password.equals(user.getPassword())) {
                                        //search matching
                                        isMatched[0] = true;

                                        //shared is login
                                        if (rememberme.isChecked()) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("currentUserUsername", emailwithuoutgmail);
                                            editor.putString("currentUserPassword", password);
                                            editor.putBoolean("islogin", true);
                                            editor.commit();
                                        } else {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("currentUserUsername", "");
                                            editor.putString("currentUserPassword", "");
                                            editor.putBoolean("islogin", false);
                                            editor.commit();
                                        }

                                        Intent intent = new Intent(MainActivity.this, userprofile.class);
                                        intent.putExtra("useridfordatabase", user.getId());
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                if (!isMatched[0]) {
                                    Toast.makeText(MainActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                                    loginButton.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Oops! No Internet Connection.", Snackbar.LENGTH_LONG).setDuration(10000);
                        snackbar.show();
                    }

                }


            });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sign
                Intent intent = new Intent(MainActivity.this, signupactivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
