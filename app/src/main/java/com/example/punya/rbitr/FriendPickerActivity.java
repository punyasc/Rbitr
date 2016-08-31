package com.example.punya.rbitr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendPickerActivity extends AppCompatActivity {
    List<String> allNames = new ArrayList<>();
    List<String> allIds = new ArrayList<>();
    List<String> allImages = new ArrayList<>();
    List<Friend> chosenFriends = new ArrayList<>();
    ListView lv;
    HashMap<Integer, Integer[]> selectedMap = new HashMap<>();
    HashSet<Integer> selectedIndexes = new HashSet<>();
    CustomAdapter ad;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_picker);
        FacebookSdk.sdkInitialize(getApplicationContext());
        /* Make graph request */
        GraphRequestAsyncTask request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        graphResponseToLists(response.getJSONObject());
                    }
                }
        ).executeAsync();
    }

    public void graphResponseToLists(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject oneFriend = jsonArray.getJSONObject(i);
                Friend newFriend = new Friend(oneFriend.getString("name"), oneFriend.getString("id"));
                allNames.add(oneFriend.getString("name"));
                allIds.add(oneFriend.getString("id"));
                addImageFromId(oneFriend.getString("id"), i);
                //allFriends.add(newFriend);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void addImageFromId(String id, int i) {
        //final int index = i;
        GraphRequestAsyncTask request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "?fields=picture",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                if (data.has("picture")) {
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    allImages.add(profilePicUrl);
                                    if (allNames.size() == allImages.size()) {
                                        inflateListView();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).executeAsync();
    }

    public void inflateListView() {
        final TextView mTextView = (TextView) findViewById(R.id.textView2);
        lv = (ListView) findViewById(R.id.listview);
        final CustomAdapter mAdapter = new CustomAdapter(FriendPickerActivity.this, arrayListToArray(allNames), arrayListToArray(allImages));
        ad = mAdapter;
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arrayListToArray(allNames));
        lv.setAdapter(adapt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                if (selectedIndexes.contains(position)) {
                    selectedIndexes.remove(position);
                    //Button remButton = (Button) findViewById(777 - position);
                    //ViewGroup layout = (ViewGroup) remButton.getParent();
                    //if(null!=layout) //for safety only  as you are doing onClick
                    //    layout.removeView(remButton);
                } else {
                    selectedIndexes.add(position);
                    //Button newButton = new Button(FriendPickerActivity.this);
                    //newButton.setVisibility(View.VISIBLE);
                    //newButton.setText(allNames.get(position));
                    //newButton.setId(777 + position);
                }
            }
        });
    }

    public String[] arrayListToArray(List<String> list) {
        Object[] objList = list.toArray();
        return Arrays.copyOf(objList, objList.length, String[].class);
    }

    public void selectButtonClicked(View view) {
        int len = lv.getCount();
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        for (int i = 0; i < len; i++)
            if (checked.get(i)) {
                Log.i("tester", allNames.get(i));
            }
        final int[] summedPrefs = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        final int onChangeCounter = 0;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        for (Integer i : selectedIndexes) {
            //String thisId = allFriends.get(i).getId();
            mDatabase.child("users").child(allIds.get(i)).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                Log.d("abc", "VALUE NULL");
                            } else {
                                RUser thisUser = dataSnapshot.getValue(RUser.class);
                                Log.i("foodpref", "" + thisUser.chinese);
                                summedPrefs[0] += thisUser.american;
                                summedPrefs[1] += thisUser.chinese;
                                summedPrefs[2] += thisUser.french;
                                summedPrefs[3] += thisUser.indian;
                                summedPrefs[4] += thisUser.italian;
                                summedPrefs[5] += thisUser.japanese;
                                summedPrefs[6] += thisUser.mediterranean;
                                summedPrefs[7] += thisUser.mexican;
                                summedPrefs[8] += thisUser.vegan;
                                for (int i = 0; i < summedPrefs.length; i++) {
                                    Log.i("daaa", "index " + i + " rating " + summedPrefs[i]);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("c", "getUser:onCancelled", databaseError.toException());
                        }
                    });
        }
        Log.i("INDEXCHECK", "sihhze = " + allIds.size());
        int maxRating = 0;
        int chosenIndex = 0;
        for (int i = 0; i < summedPrefs.length; i++) {
            Log.i("daaa", "index " + i + " rating " + summedPrefs[i]);
            if (summedPrefs[i] > maxRating) {
                maxRating = summedPrefs[i];
                chosenIndex = i;
            }
        }
        Log.i("INDEXCHECK", "sihhze = " + chosenIndex);
        indexToCategory(chosenIndex);
    }

    public void indexToCategory(int chosen) {
        //Toast.makeText(FriendPickerActivity.this, "chosen: " + chosen, Toast.LENGTH_SHORT).show();
        if (chosen == 0)
            Toast.makeText(FriendPickerActivity.this, "AMERICAN chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 1)
            Toast.makeText(FriendPickerActivity.this, "CHINESE chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 2)
            Toast.makeText(FriendPickerActivity.this, "FRENCH chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 3)
            Toast.makeText(FriendPickerActivity.this, "INDIAN chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 4)
            Toast.makeText(FriendPickerActivity.this, "ITALIAN chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 5)
            Toast.makeText(FriendPickerActivity.this, "JAPANESE chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 6)
            Toast.makeText(FriendPickerActivity.this, "MEDITERRANEAN chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 7)
            Toast.makeText(FriendPickerActivity.this, "MEXICAN chosen", Toast.LENGTH_SHORT).show();
        if (chosen == 8)
            Toast.makeText(FriendPickerActivity.this, "VEGAN chosen", Toast.LENGTH_SHORT).show();
    }
}
