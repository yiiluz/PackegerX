package com.example.packegerv2.model;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.packegerv2.Entities.Person;
import com.example.packegerv2.R;
import com.example.packegerv2.controller.MainActivity;
import com.example.packegerv2.controller.NewPackageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseData {
    public static ArrayList<Person> getUsers() {
        return users;
    }

    final static ArrayList<Person> users = new ArrayList<>();

    public static void getUsersList() {
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person person = postSnapshot.getValue(Person.class);
                    users.add(person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
