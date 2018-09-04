package com.justinlee.jntfirbase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticlesManager {
    private static final String TAG = "firebasexx";

    public static void sendArticle(Activity activity, User loggedInUser, FirebaseDatabase database) {
        String articleTitle;
        String articleContent;
        String articleTag;
        String author;
        String createdTime;
        String userEmail;

        EditText titleEditText = activity.findViewById(R.id.text_article_title);
        articleTitle = titleEditText.getText().toString();

        EditText contentEditText = activity.findViewById(R.id.text_article_content);
        articleContent = contentEditText.getText().toString();

        RadioGroup radioGroup = activity.findViewById(R.id.radioGroup);
        RadioButton checkedButon = activity.findViewById(radioGroup.getCheckedRadioButtonId());
        articleTag = checkedButon.getText().toString();

        author = loggedInUser.getName();

//        Date datetime = Calendar.getInstance().getTime();
//        createdTime = new SimpleDateFormat("yyyy/mm/dd").format((int) (datetime.getTime()));
        createdTime = new SimpleDateFormat("yyyy/M/d").format(new Date());

        userEmail = loggedInUser.getEmail();

        Article article = new Article(articleTitle, articleContent, articleTag, author, createdTime, userEmail);

        DatabaseReference articleReference = database.getReference(FirebaseConstants.FIREBASE_CHILD_ARTICLES);
        articleReference.push().setValue(article);
    }

    public static void getArticlesByTag(MainActivity mainActivity, User loggedInUser, final FirebaseDatabase database) {
        RadioGroup radioGroup = mainActivity.findViewById(R.id.radioGroup);
        final RadioButton radioButton = mainActivity.findViewById(radioGroup.getCheckedRadioButtonId());

        DatabaseReference articleReference = database.getReference(FirebaseConstants.FIREBASE_CHILD_ARTICLES);
        articleReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child(FirebaseConstants.FIREBASE_CHILD_ARTICL_TAG).getValue().equals(radioButton.getText())) {
                    Log.d(TAG, "onChildAdded: filtered item is: " + dataSnapshot.toString());
                }
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


    public static void getArticlesByFriendEmail(MainActivity mainActivity, User loggedInUser, final FirebaseDatabase database) {
        EditText textFriendEmail = mainActivity.findViewById(R.id.text_friend_email);
        final String friendEmail = textFriendEmail.getText().toString();

        DatabaseReference articleReference = database.getReference(FirebaseConstants.FIREBASE_CHILD_ARTICLES);
        articleReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child(FirebaseConstants.FIREBASE_CHILD_EMAIL_TAG).getValue().equals(friendEmail)) {
                    Log.d(TAG, "onChildAdded: filtered item is: " + dataSnapshot.toString());
                }
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

    public static void getArticlesByTagAndFriendEmail(MainActivity mainActivity, User loggedInUser, final FirebaseDatabase database) {
        RadioGroup radioGroup = mainActivity.findViewById(R.id.radioGroup);
        final RadioButton radioButton = mainActivity.findViewById(radioGroup.getCheckedRadioButtonId());

        EditText textFriendEmail = mainActivity.findViewById(R.id.text_friend_email);
        final String friendEmail = textFriendEmail.getText().toString();

        DatabaseReference articleReference = database.getReference(FirebaseConstants.FIREBASE_CHILD_ARTICLES);
        articleReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child(FirebaseConstants.FIREBASE_CHILD_EMAIL_TAG).getValue().equals(friendEmail)) {
                    if(dataSnapshot.child(FirebaseConstants.FIREBASE_CHILD_ARTICL_TAG).getValue().equals(radioButton.getText())) {
                        Log.d(TAG, "onChildAdded: filtered item is: " + dataSnapshot.toString());
                    }
                }
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
}
