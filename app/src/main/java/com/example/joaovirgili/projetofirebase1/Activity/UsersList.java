package com.example.joaovirgili.projetofirebase1.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.joaovirgili.projetofirebase1.Classes.MyAdapter;
import com.example.joaovirgili.projetofirebase1.Classes.User;
import com.example.joaovirgili.projetofirebase1.R;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersList extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerViewUsersList;
    RecyclerView.Adapter adapter;

    ArrayList<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");

        recyclerViewUsersList = findViewById(R.id.recyclerUsersList);
        recyclerViewUsersList.setHasFixedSize(true);
        recyclerViewUsersList.setLayoutManager(new LinearLayoutManager(this));
        usersList = new ArrayList<>();
        adapter = new MyAdapter(usersList, this);

        recyclerViewUsersList.setAdapter(adapter);

    }


    protected void onStart() {
        super.onStart();

        databaseReference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                usersList.add(getUserFromData(dataSnapshot));
                recyclerViewUsersList.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (User user: usersList) {
                    if (user.getId().equals(dataSnapshot.child("id").getValue().toString())) {
                        usersList.remove(user);
                    }
                }
                recyclerViewUsersList.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private User getUserFromData (DataSnapshot data) {
        return new User(data.child("id").getValue().toString(),
                data.child("email").getValue().toString(),
                data.child("firstName").getValue().toString(),
                data.child("lastName").getValue().toString(),
                data.child("profileImage").getValue().toString()
        );
    }

}
