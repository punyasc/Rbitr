package com.example.punya.rbitr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrefPickerActivity extends AppCompatActivity {

    private String id;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_picker);
    }

    public void prefSaveButton(View view) {
        id = Profile.getCurrentProfile().getId();
        int american = (int) ((RatingBar) findViewById(R.id.ratingBarAmerican)).getRating();
        int chinese = (int) ((RatingBar) findViewById(R.id.ratingBarChinese)).getRating();
        int french = (int) ((RatingBar) findViewById(R.id.ratingBarFrench)).getRating();
        int indian = (int) ((RatingBar) findViewById(R.id.ratingBarIndian)).getRating();
        int italian = (int) ((RatingBar) findViewById(R.id.ratingBarItalian)).getRating();
        int japanese = (int) ((RatingBar) findViewById(R.id.ratingBarJapanese)).getRating();
        int mediterranean = (int) ((RatingBar) findViewById(R.id.ratingBarMediterranean)).getRating();
        int mexican = (int) ((RatingBar) findViewById(R.id.ratingBarMexican)).getRating();
        int vegan = (int) ((RatingBar) findViewById(R.id.ratingBarVegan)).getRating();

        RUser user = new RUser(id, american, chinese, french, indian, italian, japanese, mediterranean, mexican, vegan);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).setValue(user);

        Intent intent = new Intent(PrefPickerActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
