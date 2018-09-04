package com.justinlee.jntfirbase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_CHILD_USERS;
import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_CHILD_USERS_EMAIL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "firebasex";

    public static final String EMAIL_JUSTIN = "justinlee.archer@gmail.com";
    public static final String NAME_JUSTIN = "Justin Lee";

    FirebaseDatabase mDatabase;
    User mLoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance();

        setupUserInterface();
        setupFirebase();
        getLoggedInUser();
    }

    private void setupFirebase() {
//        setListenerForFriendRequests();
    }



    private void setupUserInterface() {
        Button buttonCreateUser = findViewById(R.id.button_create_user);
        buttonCreateUser.setOnClickListener(this);

        Button buttonSendFriendRequest = findViewById(R.id.button_send_friend_request);
        buttonSendFriendRequest.setOnClickListener(this);

        Button buttonAcceptFriendRequest = findViewById(R.id.button_accept_friend_request);
        buttonAcceptFriendRequest.setOnClickListener(this);
        setButtonClicableState(buttonAcceptFriendRequest, false);

        Button buttonRejectFriendRequest = findViewById(R.id.button_reject_friend_request);
        buttonRejectFriendRequest.setOnClickListener(this);
        setButtonClicableState(buttonRejectFriendRequest, false);

        Button buttonPostArticle = findViewById(R.id.button_post_article);
        buttonPostArticle.setOnClickListener(this);

        Button buttonGetArticlesByTag = findViewById(R.id.button_get_articles_by_tag);
        buttonGetArticlesByTag.setOnClickListener(this);

        Button buttonGetArticlesByEmail = findViewById(R.id.button_get_articles_by_email);
        buttonGetArticlesByEmail.setOnClickListener(this);

        Button buttonGetArticlesByTagAndEmail = findViewById(R.id.button_get_articles_by_both_tag_and_email);
        buttonGetArticlesByTagAndEmail.setOnClickListener(this);
    }

    private void setButtonClicableState(Button button, boolean clickable) {
        if(clickable) {
            button.setClickable(clickable);
            button.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            button.setClickable(clickable);
            button.setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    public void getLoggedInUser() {
        DatabaseReference userReference = mDatabase.getReference(FIREBASE_CHILD_USERS);
        Query query = userReference.orderByChild(FIREBASE_CHILD_USERS_EMAIL).equalTo(EMAIL_JUSTIN);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                String name = (String) dataSnapshot.child("name").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                mLoggedInUser = new User(key, email, name);
                Log.d(TAG, "onChildAdded: logged in user is: " + mLoggedInUser.getName() + " with " + mLoggedInUser.getEmail());

                // other follow up setups

                setListenerForFriendRequests();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.button_create_user:
                createUser();
                break;

            case R.id.button_send_friend_request:
                if(mLoggedInUser != null) {
                    String inputEmail = getInputEmail();
                    FriendsManager.getUserKeyAndSendRequest(mLoggedInUser, inputEmail);
                }
                break;

            case R.id.button_accept_friend_request:
                if(mLoggedInUser != null) {
                    FriendsManager.acceptAllFriendRequests(mLoggedInUser);
                }
                break;

            case R.id.button_reject_friend_request:
                Log.d(TAG, "onClick: reject0");
                if(mLoggedInUser != null) {
                    FriendsManager.rejectAllFriendRequests(mLoggedInUser);
                }
                break;

            case R.id.button_post_article:
                if(mLoggedInUser != null) {
                    ArticlesManager.sendArticle(this, mLoggedInUser, mDatabase);
                }
                break;

            case R.id.button_get_articles_by_tag:
                if(mLoggedInUser != null) {
                    ArticlesManager.getArticlesByTag(this, mLoggedInUser, mDatabase);
                }
                break;

            case R.id.button_get_articles_by_email:
                if(mLoggedInUser != null) {
                    ArticlesManager.getArticlesByFriendEmail(this, mLoggedInUser, mDatabase);
                }
                break;

            case R.id.button_get_articles_by_both_tag_and_email:
                if(mLoggedInUser != null) {
                    ArticlesManager.getArticlesByTagAndFriendEmail(this, mLoggedInUser, mDatabase);
                }
                break;

            default:
                break;
        }
    }




    /***************************************************
     * methods for users
     ***************************************************/
    private void createUser() {
//        final User user = new User("test@gmail.com", "test");
        if(mLoggedInUser == null) {
            User user = new User(EMAIL_JUSTIN, NAME_JUSTIN);

            DatabaseReference userReference = mDatabase.getReference(FIREBASE_CHILD_USERS);
            userReference.push().setValue(user);

            Log.d(TAG, "createUser: user created");
        }
    }


    /****************************************************
     * methods for friends and friends requests
     ***************************************************/
    public String getInputEmail() {
        EditText emailEditText = findViewById(R.id.text_email_friend_request);
        String emailString = emailEditText.getText().toString();

        if((emailString != null) && !emailString.isEmpty()) {
            return emailString;
        } else {
            return null;
        }
    }


    private void setListenerForFriendRequests() {
        final Button buttonAcceptFriendRequest = findViewById(R.id.button_accept_friend_request);
        final Button buttonRejectFriendRequest = findViewById(R.id.button_reject_friend_request);

        DatabaseReference friendsReference = mDatabase.getReference(FIREBASE_CHILD_USERS).child(mLoggedInUser.getKey()).child(FirebaseConstants.FIREBASE_CHILD_FRIENDS);
        friendsReference.addChildEventListener(new ChildEventListener() {
            int receivedCount = 0;

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: some friends actions were added");
                if(dataSnapshot.getValue() != null) {
                    String friendState = dataSnapshot.getValue().toString();
                    if (friendState.equals(FirebaseConstants.FIREBASE_REQUEST_RECEIVED)) {
                        Button buttonAcceptFriendRequest = findViewById(R.id.button_accept_friend_request);
                        setButtonClicableState(buttonAcceptFriendRequest, true);

                        Button buttonRejectFriendRequest = findViewById(R.id.button_reject_friend_request);
                        setButtonClicableState(buttonRejectFriendRequest, true);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue() != null) {
                    String friendState = dataSnapshot.getValue().toString();
                    if (friendState.equals(FirebaseConstants.FIREBASE_REQUEST_RECEIVED)) {
                        receivedCount++;
//                        Button buttonAcceptFriendRequest = findViewById(R.id.button_accept_friend_request);
//                        setButtonClicableState(buttonAcceptFriendRequest, true);
                    }

                    if(receivedCount > 0) {
                        setButtonClicableState(buttonAcceptFriendRequest, true);
                        setButtonClicableState(buttonRejectFriendRequest, true);
                        receivedCount = 0;
                    } else {
                        setButtonClicableState(buttonAcceptFriendRequest, false);
                        setButtonClicableState(buttonRejectFriendRequest, false);
                        receivedCount = 0;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildAdded: some friends actions were removed");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: some friends actions were moved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onChildAdded: some friends actions were canceled");
            }
        });
    }
}
