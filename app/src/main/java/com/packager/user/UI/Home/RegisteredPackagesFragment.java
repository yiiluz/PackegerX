package com.packager.user.UI.Home;


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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.packager.user.UI.Login.MainActivity;
import com.packager.user.Utils.Configuration;
import com.packager.user.Entities.Package;
import com.packager.user.R;

import java.util.ArrayList;

public class RegisteredPackagesFragment extends Fragment {
    ProgressDialog dialog;

    public RegisteredPackagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View inf = inflater.inflate(R.layout.fragment_registered_packages, container, false);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Wait...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("packages");
        final ListView listView = new ListView(inf.getContext());
        final ArrayList<Package> registeredPackages = new ArrayList<>();
        final DatabaseReference finalMyRef = myRef;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Package p = postSnapshot.getValue(Package.class);
                    if (p.getPhoneAddressee().equalsIgnoreCase(NewHomeActivity.getCurrPerson().get_phoneNumber())
                            && p.getStatus() == Configuration.Status.REGISTERED) {
                        registeredPackages.add(p);
                    }
                }
                ArrayAdapter<Package> adapter = new ArrayAdapter<Package>(inf.getContext(),
                        R.layout.adapter_registered_package, registeredPackages) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = View.inflate(getContext(), R.layout.adapter_registered_package, null);
                        }
                        Package posPack = registeredPackages.get(position);
                        ImageView image = convertView.findViewById(R.id.imageViewRegistred);
                        TextView sender = (TextView) convertView.findViewById(R.id.textViewSender);
                        TextView status = (TextView) convertView.findViewById(R.id.textViewStatus);
                        TextView location = (TextView) convertView.findViewById(R.id.textViewLocation);
                        sender.setText(posPack.getPhoneSender());
                        status.setText(posPack.getStatus().toString());
                        location.setText(posPack.getCurrentAddress().getAddress());
                        switch (posPack.getPackageType()) {
                            case ENVELOPE:
                                image.setImageResource(R.drawable.ic_envelpoe);
                                break;
                            default:
                                image.setImageResource(R.drawable.ic_package);
                        }
                        return convertView;
                    }
                };
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Package p = (Package) listView.getItemAtPosition(position);
                        if (p.getStatus().equals(Configuration.Status.REGISTERED)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            View mView = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
                            builder.setMessage("Choose friend to pick up your delivery:");
                            final Spinner spinner = mView.findViewById(R.id.spinner);
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, p.getFriendsPhones());
                            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerAdapter);
                            builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (spinner.getSelectedItem() != null) {
                                        p.setPicker(spinner.getSelectedItem().toString());
                                        p.setStatus(Configuration.Status.ON_THE_WAY);
                                        finalMyRef.child(p.getPackageID()).setValue(p);
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                            builder.setView(mView);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("The friend that you choose for picking up this delivery is: " + p.getPicker());
                        }
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
}
