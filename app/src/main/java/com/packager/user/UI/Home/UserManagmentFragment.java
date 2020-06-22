package com.packager.user.UI.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.packager.user.UI.Login.MainActivity;
import com.packager.user.Utils.Configuration;
import com.packager.user.Entities.Person;
import com.packager.user.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserManagmentFragment extends Fragment {
    Button signOutButton;
    static TextView userMail, userFname, userLName, userAddress, userPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_user_managment, container, false);
        userAddress = inf.findViewById(R.id.textViewUserAddrress);
        userFname = inf.findViewById(R.id.textViewUserFName);
        userLName = inf.findViewById(R.id.textViewUserLName);
        userMail = inf.findViewById(R.id.textViewUserEmail4);
        userPhone = inf.findViewById(R.id.textViewUserPhone);
        final ArrayList<Person> users = new ArrayList<>();
//        FirebaseDatabase database = MainActivity.getDatabase();
//        DatabaseReference myRef = database.getReference();
//        myRef = myRef.child("users");

        signOutButton = inf.findViewById(R.id.buttonSignOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMAuth().signOut();
                FirebaseAuth.getInstance().signOut();
                AuthUI.getInstance().signOut(getActivity());
                Toast.makeText(getActivity(), "LogOut successfully from " + MainActivity.getSignInMethod() + " account", Toast.LENGTH_SHORT).show();
                if (MainActivity.getSignInMethod() == Configuration.SignInMethod.GOOGLE)
                    MainActivity.getGoogleClient().signOut();
                startActivity((new Intent(getActivity(), MainActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().finish();
            }
        });
        setUserDetails();
        // Inflate the layout for this fragment
        return inf;
    }

    static void setUserDetails() {
        Person curr = NewHomeActivity.getCurrPerson();
        if (curr != null) {
            userAddress.setText(curr.getAddress().getAddress());
            userFname.setText(curr.get_firstNAme());
            userLName.setText(curr.get_lastName());
            userMail.setText(curr.get_email());
            userPhone.setText(curr.get_phoneNumber());
        }
    }
}
