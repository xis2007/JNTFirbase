package com.justinlee.jntfirbase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_CHILD_FRIENDS;
import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_CHILD_USERS;
import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_CHILD_USERS_EMAIL;
import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_REQUEST_RECEIVED;
import static com.justinlee.jntfirbase.FirebaseConstants.FIREBASE_REQUEST_SENT;

public class FriendsManager {
    private static final String TAG = "firebasex";

    public static void getUserKeyAndSendRequest(final User loggedInUser, String targetEmail) {

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_USERS);
        Query userQuery = userReference.orderByChild(FIREBASE_CHILD_USERS_EMAIL).equalTo(targetEmail);
        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                setSentStateTo(loggedInUser.getKey(), dataSnapshot.getKey());
                setReceivedStateTo(dataSnapshot.getKey(), loggedInUser.getKey());
                Log.d(TAG, "onChildAdded: friend request sent from: " + loggedInUser.getName() + " to " + dataSnapshot.child("name").getValue().toString());
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

    public static void setSentStateTo(String senderKey, String receiverKey) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_USERS).child(senderKey).child(FIREBASE_CHILD_FRIENDS).child(receiverKey);
        userReference.setValue(FIREBASE_REQUEST_SENT);
    }


    public static void setReceivedStateTo(String receiverKey, String senderKey) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_USERS).child(receiverKey).child(FIREBASE_CHILD_FRIENDS).child(senderKey);
        userReference.setValue(FIREBASE_REQUEST_RECEIVED);
    }


    /**
     * methods for receiving friend requests
     */
    public static void acceptAllFriendRequests(final User loggedInUser) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_USERS).child(loggedInUser.getKey()).child(FirebaseConstants.FIREBASE_CHILD_FRIENDS);
        userReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue().toString().equals(FirebaseConstants.FIREBASE_REQUEST_RECEIVED)) {
                    setFriendAcceptedStateTo(loggedInUser.getKey(), dataSnapshot.getKey());
                    setFriendAcceptedStateTo(dataSnapshot.getKey(), loggedInUser.getKey());
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

    private static void setFriendAcceptedStateTo(String targetKey, String keyOfFriendUnderTarget) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_USERS).child(targetKey).child(FIREBASE_CHILD_FRIENDS).child(keyOfFriendUnderTarget);
        userReference.setValue("true");
    }
}
