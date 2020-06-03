package com.example.packegerv2.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packegerv2.Entities.Person;
import com.example.packegerv2.R;
import com.example.packegerv2.controller.MainActivity;
import com.example.packegerv2.model.FireBaseData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserManagmentFragment extends Fragment {
    Button signOutButton;
    ProgressDialog dialog;
    TextView userMail, userFname, userLName, userAddress, userPhone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_user_managment, container, false);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Wait...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        userAddress = inf.findViewById(R.id.textViewUserAddrress);
        userFname = inf.findViewById(R.id.textViewUserFName);
        userLName = inf.findViewById(R.id.textViewUserLName);
        userMail = inf.findViewById(R.id.textViewUserEmail4);
        userPhone = inf.findViewById(R.id.textViewUserPhone);

        userMail.setText(MainActivity.getMAuth().getCurrentUser().getEmail() + " by " + MainActivity.getSignInMethod());
        Person user = null;
        final ArrayList<Person> users = new ArrayList<>();
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person person = postSnapshot.getValue(Person.class);
                    if (person.get_email().equalsIgnoreCase(MainActivity.getMAuth().getCurrentUser().getEmail()))
                    {
                        userAddress.setText(person.getAddress().getAddress());
                        userPhone.setText(person.get_phoneNumber());
                        userFname.setText(person.get_firstNAme());
                        userLName.setText(person.get_lastName());
                    }
                }
                dialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.hide();
            }
        });
        signOutButton = inf.findViewById(R.id.buttonSignOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMAuth().signOut();
                Toast.makeText(getActivity(),"LogOut successfully from " + MainActivity.getSignInMethod() + " account",Toast.LENGTH_SHORT).show();
                if (MainActivity.getSignInMethod() == "google")
                    MainActivity.getGoogleClient().signOut();
                startActivity((new Intent(getActivity(), MainActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().finish();
            }
        });
        // Inflate the layout for this fragment
        return inf;
    }
}
