package com.packager.user.Receivers;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.packager.user.Entities.Package;
import com.packager.user.Entities.Person;
import com.packager.user.UI.Login.MainActivity;
import com.packager.user.UI.Home.NewHomeActivity;

public class NewPackageService extends IntentService {
    private FirebaseAuth mAuth;
    private Person currentUser;
    private String currentUserMail;
    private Date _date;
    private DateFormat _dateFormat;
    private String _now;

    @SuppressLint("SimpleDateFormat")
    public NewPackageService() {
        super("NewPackageService");
        mAuth = MainActivity.getMAuth();
        currentUser = NewHomeActivity.getCurrPerson();
        currentUserMail = currentUser.get_email();
        mAuth = FirebaseAuth.getInstance();
        _date = Calendar.getInstance().getTime();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("packages");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Package p = dataSnapshot.getValue(Package.class);
                if (p != null) {
                    if ((p.getPhoneAddressee().trim().equals(currentUser.get_phoneNumber())) &&
                            (p.getDate().compareTo(_date) > 0)) {
                        {
                            sendBroadcast(new Intent("new_package_service"));
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String
                    s) {

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
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(true);
    }
}

