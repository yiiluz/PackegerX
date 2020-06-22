package com.packager.user.Data;

import androidx.annotation.NonNull;

import com.packager.user.Entities.Package;
import com.packager.user.Entities.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.packager.user.UI.Login.MainActivity;

import java.util.ArrayList;

public class FireBaseData {
    public static ArrayList<Person> getUsers() {
        return users;
    }

    final static ArrayList<Person> users = new ArrayList<>();
    final static ArrayList<Package> packages = new ArrayList<>();

    public static void getUsersList() {
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        final DatabaseReference usersRef = myRef.child("users"), packagesRef = myRef.child("packages");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        packagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                packages.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Package p = postSnapshot.getValue(Package.class);
                    packages.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
