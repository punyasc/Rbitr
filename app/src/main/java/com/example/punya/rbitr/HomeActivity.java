package com.example.punya.rbitr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    public TextView welcomeTextView;
    //private DatabaseReference mDatabase;
    //private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //email = getIntent().getStringExtra("email");
        setContentView(R.layout.activity_home);

        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);

        Profile currentProfile = Profile.getCurrentProfile();
        String welcomeText = "Welcome, " + currentProfile.getFirstName() + ".";
        welcomeTextView.setText(welcomeText);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void prefButtonClick(View view) {
        Intent intent = new Intent(HomeActivity.this, PrefPickerActivity.class);
        startActivity(intent);
    }

    public void logoutButtonClick(View view) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void chooseFriendsButtonClick(View view) {
        Intent intent = new Intent(HomeActivity.this, FriendPickerActivity.class);
        startActivity(intent);
    }
}
