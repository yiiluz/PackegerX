package com.example.packegerv2.model;

import androidx.annotation.NonNull;

import com.example.packegerv2.Entities.Person;
import com.example.packegerv2.controller.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class FireBaseAuth {
    private static FireBaseAuth firebaseAuthObj = null;
    private int exit;

    private FireBaseAuth() {
    }

    public static FireBaseAuth getFirebaseAuthObj() {
        if (firebaseAuthObj == null)
            firebaseAuthObj = new FireBaseAuth();
        return firebaseAuthObj;
    }

    public int createUserInFireBase(final String email, final String pass, final String firstNAme, final String lastName, final String phoneNumber, final String photoUrl) {
        try {
            MainActivity.getMAuth().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        exit = -1;
                    } else {
                        Person p = new Person(firstNAme, lastName, email, phoneNumber, photoUrl, MainActivity.getMAuth().getUid(), MainActivity.getDeviceAddress());
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // Write a message to the database
                        String path = MainActivity.USERS_PATH + File.separator + MainActivity.getMAuth().getUid();
                        final DatabaseReference usersRef = database.getReference(path);
                        usersRef.setValue(p);
                        exit = 0;
                    }
                }
            });
        }
        catch(Exception e){
            exit = -1;
        }
        return exit;
    }
}
