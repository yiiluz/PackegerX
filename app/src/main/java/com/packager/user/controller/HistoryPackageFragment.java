package com.packager.user.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.packager.user.Entities.Configuration;
import com.packager.user.Entities.Package;
import com.packager.user.Entities.Person;
import com.packager.user.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryPackageFragment extends Fragment {
    ProgressDialog dialog;
    private HistoryPackageViewModel mViewModel;

    public static HistoryPackageFragment newInstance() {
        return new HistoryPackageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View inf = inflater.inflate(R.layout.history_package_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(HistoryPackageViewModel.class);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Wait...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        FirebaseDatabase database = MainActivity.getDatabase();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("packages");
        final ListView listView = new ListView(inf.getContext());
        final ArrayList<Package> historyPackages = new ArrayList<>();
        final DatabaseReference finalMyRef = myRef;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Package p = postSnapshot.getValue(Package.class);
                    if (p.getPhoneAddressee().equals(NewHomeActivity.getCurrPerson().get_phoneNumber()) &&
                            !p.getStatus().equals(Configuration.Status.REGISTERED)) {
                        mViewModel.packageRepository.insert(p);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.hide();
            }

        });

//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        final Package p = (Package) listView.getItemAtPosition(position);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setMessage("Are you sure you want to ask this package addressee for picking up his package ?");
//                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                if (!p.getFriendsPhones().contains(NewHomeActivity.getCurrPerson().get_phoneNumber())) {
//                                    p.addFriend(NewHomeActivity.getCurrPerson().get_phoneNumber());
//                                    finalMyRef.child(p.getPackageID()).setValue(p);
//                                }
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                            }
//                        });
//                        AlertDialog dialog = builder.create();
//                        dialog.show();
//                    }
//                });
        mViewModel.packageRepository.getAllPackages().observe(this, new Observer<List<Package>>() {
            @Override
            public void onChanged(List<Package> packages) {
                ArrayAdapter<Package> adapter = new ArrayAdapter<Package>(inf.getContext(),
                        R.layout.adapter_history_packages, packages) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = View.inflate(getContext(), R.layout.adapter_history_packages, null);
                        }
                        Package posPack = mViewModel.getAllPackages().getValue().get(position);
                        TextView picker = (TextView) convertView.findViewById(R.id.textViewPicker);
                        TextView dateOfApproval = (TextView) convertView.findViewById(R.id.textViewAprrovalDate);
                        ImageView image = convertView.findViewById(R.id.imageViewHistory);
                        dateOfApproval.setText(posPack.getDate().toString());
                        Person pPicker = null;
                        for (Person p : NewHomeActivity.getUsers()) {
                            if (p.get_phoneNumber().equals(posPack.getPicker())) {
                                pPicker = p;
                            }
                        }
                        picker.setText(posPack.getPicker() + " " + pPicker.get_firstNAme() + " " + pPicker.get_lastName());
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
                listView.setAdapter(adapter);
            }
        });
        dialog.hide();
        return listView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<List<Package>> list = mViewModel.getAllPackages();


    }

}
