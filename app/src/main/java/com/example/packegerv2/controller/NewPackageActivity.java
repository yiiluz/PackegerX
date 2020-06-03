package com.example.packegerv2.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packegerv2.Entities.Configuration;
import com.example.packegerv2.Entities.Package;
import com.example.packegerv2.Entities.Person;
import com.example.packegerv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewPackageActivity extends AppCompatActivity {
    private AutoCompleteTextView senderPhone, addresseePhone;
    private CheckBox fragile;
    private Spinner packageType, packageWeight;
    private Button addPackage;
    private ArrayList<String> usersPhones = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView addresseeAdrress, senderAddress;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_package);

        senderPhone = findViewById(R.id.senderPhoneNumber);
        addresseePhone = findViewById(R.id.addresseePhoneNumber);
        packageType = findViewById(R.id.packageType);
        packageWeight = findViewById(R.id.packageWeight);
        fragile = findViewById(R.id.checkBoxIsFragile);
        addPackage = findViewById(R.id.button);
        addresseeAdrress = findViewById(R.id.addresseeAddress);
        senderAddress = findViewById(R.id.senderAddress);
        scrollView = findViewById(R.id.mainLayout);
        scrollView.getBackground().setAlpha(30);
        final ArrayList<Person> users = new ArrayList<>();
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
                usersPhones.clear();
                for (Person p : users) {
                    usersPhones.add(p.get_phoneNumber());
                }
                adapter = new ArrayAdapter<String>(NewPackageActivity.this, R.layout.support_simple_spinner_dropdown_item, usersPhones);
                senderPhone.setAdapter(adapter);
                addresseePhone.setAdapter(adapter);
                Toast.makeText(NewPackageActivity.this, "Adapter is set!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewPackageActivity.this, "Error in reading data " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        packageType.setAdapter(new ArrayAdapter<Configuration.PackageType>(NewPackageActivity.this, android.R.layout.simple_list_item_1, Configuration.PackageType.values()));
        packageWeight.setAdapter(new ArrayAdapter<Configuration.PackageWeight>(NewPackageActivity.this, android.R.layout.simple_list_item_1, Configuration.PackageWeight.values()));
        addresseePhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Person p : users) {
                    if (p.get_phoneNumber().contentEquals(addresseePhone.getText().toString())) {
                        addresseeAdrress.setText(p.getAddress().getAddress());
                    }
                }
            }
        });
        senderPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Person p : users) {
                    if (p.get_phoneNumber().contentEquals(senderPhone.getText().toString())) {
                        senderAddress.setText(p.getAddress().getAddress());
                    }
                }
            }
        });

        addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (senderPhone.length() != 10 || addresseePhone.length() != 10) {
                    Toast.makeText(NewPackageActivity.this, "Fill All details before clicking add", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!MainActivity.isIsAddressSet()) {
                    Toast.makeText(NewPackageActivity.this, "We haven't found your location. Reopen app and make sure the permissions OK", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase database = MainActivity.getDatabase();
                DatabaseReference myRef = database.getReference();
                myRef = myRef.child("packages");
                String packageID = myRef.push().getKey();
                Package p = new Package((Configuration.PackageType) packageType.getSelectedItem(), fragile.isChecked(),
                        (Configuration.PackageWeight) packageWeight.getSelectedItem(), packageID, addresseePhone.getText().toString(),
                        senderPhone.getText().toString(), MainActivity.getDeviceAddress());
                myRef.child(packageID).setValue(p);
                Toast.makeText(NewPackageActivity.this, "Package Added with ID: " + packageID, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
