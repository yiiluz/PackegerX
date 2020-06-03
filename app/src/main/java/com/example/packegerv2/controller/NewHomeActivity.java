package com.example.packegerv2.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packegerv2.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewHomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView userName, userMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.getBackground().setAlpha(30);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_user_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                                new UserManagmentFragment()).commit();
                        break;
                    case R.id.nav_new_package:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                                new NewPackageFragment()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);

        // Change user name and mail at the navigation header
        userMail = headerView.findViewById(R.id.user_header_mail);
        userName = headerView.findViewById(R.id.user_header_name);
        userMail.setText(MainActivity.getMAuth().getCurrentUser().getEmail());
        FirebaseDatabase database = MainActivity.getDatabase();
        String pathToFname = "users/" +
                MainActivity.getMAuth().getCurrentUser().getUid() + "/_firstNAme";
        String pathToLname = "users/" +
                MainActivity.getMAuth().getCurrentUser().getUid() + "/_lastName";
        final DatabaseReference refToUserFname = database.getReference(pathToFname);
        final DatabaseReference refToUserUserLname = database.getReference(pathToLname);
        refToUserFname.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userName.setText(dataSnapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(NewHomeActivity.this, "Failed to read user name from server", Toast.LENGTH_SHORT).show();
                    }
                });
        refToUserUserLname.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userName.setText(userName.getText().toString() + " " + dataSnapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(NewHomeActivity.this, "Failed to read user name from server", Toast.LENGTH_SHORT).show();
                    }
                });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                    new UserManagmentFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_user_profile);
        }
    }
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }


}
