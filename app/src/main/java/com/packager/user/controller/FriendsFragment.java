package com.packager.user.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.packager.user.Entities.Address;
import com.packager.user.Entities.Configuration;
import com.packager.user.Entities.Package;
import com.packager.user.Entities.Person;
import com.packager.user.R;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendsFragment extends Fragment {
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inf = inflater.inflate(R.layout.fragment_friends, container, false);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Wait...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("packages");
        final ListView listView = new ListView(inf.getContext());
        final ArrayList<Package> friendsPackages = new ArrayList<>();
        final DatabaseReference finalMyRef = myRef;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Package p = postSnapshot.getValue(Package.class);
                    if (distance(p.getCurrentAddress(), NewHomeActivity.getCurrPerson().getAddress()) < Configuration.MAX_DISTANCE
                            && (p.getStatus() == Configuration.Status.REGISTERED ||
                            (p.getPicker() != null &&
                                    p.getPicker().equals(NewHomeActivity.getCurrPerson().get_phoneNumber())))) {
                        friendsPackages.add(p);
                    }
                }
                ArrayAdapter<Package> adapter = new ArrayAdapter<Package>(inf.getContext(),
                        R.layout.adapter_friends_packages, friendsPackages) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = View.inflate(getContext(), R.layout.adapter_friends_packages, null);
                        }
                        Package posPack = friendsPackages.get(position);
                        TextView to = (TextView) convertView.findViewById(R.id.textViewToFriendAdapter);
                        TextView currLocation = (TextView) convertView.findViewById(R.id.textViewCurrentLocationFriendAdapter);
                        TextView dstLocation = (TextView) convertView.findViewById(R.id.textViewDestinationFriendsAdapter);
                        ImageView image = convertView.findViewById(R.id.imageViewFriends);
                        to.setText(posPack.getPhoneAddressee());
                        currLocation.setText(posPack.getCurrentAddress().getAddress());
                        Person addressee = null;
                        for (Person p : NewHomeActivity.getUsers()) {
                            if (p.get_phoneNumber().equals(posPack.getPhoneAddressee())) {
                                addressee = p;
                            }
                        }
                        dstLocation.setText(addressee.getAddress().getAddress());
                        switch (posPack.getPackageType()) {
                            case ENVELOPE:
                                if (NewHomeActivity.getCurrPerson().get_phoneNumber().equals(posPack.getPicker()))
                                    image.setImageResource(R.drawable.ic_confirmed_envelope);
                                else
                                    image.setImageResource(R.drawable.ic_envelpoe);
                                break;
                            default:
                                if (NewHomeActivity.getCurrPerson().get_phoneNumber().equals(posPack.getPicker()))
                                    image.setImageResource(R.drawable.ic_confirmed_package);
                                else
                                    image.setImageResource(R.drawable.ic_package);
                                break;
                        }
                        return convertView;
                    }
                };
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Package p = (Package) listView.getItemAtPosition(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure you want to ask this package addressee for picking up his package ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!p.getFriendsPhones().contains(NewHomeActivity.getCurrPerson().get_phoneNumber())) {
                                    p.addFriend(NewHomeActivity.getCurrPerson().get_phoneNumber());
                                    finalMyRef.child(p.getPackageID()).setValue(p);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.hide();
            }

        });
        dialog.hide();
        return listView;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double distance(Address a, Address b) {
        double lat1 = a.getLat();
        double lon1 = a.getLongt();
        double lat2 = b.getLat();
        double lon2 = b.getLongt();
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }
}

