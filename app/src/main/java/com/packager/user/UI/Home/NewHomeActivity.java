package com.packager.user.UI.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.packager.user.UI.Home.FriendsPackages.FriendsFragment;
import com.packager.user.UI.Login.MainActivity;
import com.packager.user.Utils.Configuration;
import com.packager.user.Entities.Person;
import com.packager.user.Receivers.NewPackageService;
import com.packager.user.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewHomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView userName, userMail;
    private static Person currPerson = null;
    private ProgressDialog dialog;
    private NavigationView navigationView;
    private static ArrayList<Person> users = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
       // registerReceiver(new NewPackageBroadcastReceiver(), new IntentFilter("new_package_service"));

        dialog = new ProgressDialog(this);
        dialog.setMessage("Wait...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.getBackground().setAlpha(30);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_user_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                                new UserManagmentFragment()).commit();
                        break;
                    case R.id.nav_registered_packages:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                                new RegisteredPackagesFragment()).commit();
                        break;
                    case R.id.nav_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                                new HistoryPackageFragment()).commit();
                        break;
                    case R.id.nav_friends:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                                new FriendsFragment()).commit();
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

        users = new ArrayList<>();
        FirebaseDatabase database = MainActivity.getDatabase();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_container,
                    new UserManagmentFragment()).commit();
        }

        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person person = postSnapshot.getValue(Person.class);
                    users.add(person);
                    if (MainActivity.getSignInMethod() == Configuration.SignInMethod.EMAIL ||
                            MainActivity.getSignInMethod() == Configuration.SignInMethod.GOOGLE) {
                        if (person.get_email().equalsIgnoreCase(MainActivity.getMAuth().getCurrentUser().getEmail())) {
                            setCurrPerson(person);
                        }
                    } else if (person.get_phoneNumber().equalsIgnoreCase("0" + MainActivity.getMAuth().getCurrentUser().getPhoneNumber().substring(4))) {
                        setCurrPerson(person);
                    }
                }
                if (getCurrPerson() == null) {
                    MainActivity.getMAuth().signOut();
                    FirebaseAuth.getInstance().signOut();
                    AuthUI.getInstance().signOut(NewHomeActivity.this);
                    Toast.makeText(NewHomeActivity.this, "Error ! Sign Up first", Toast.LENGTH_SHORT).show();
                    if (MainActivity.getSignInMethod() == Configuration.SignInMethod.GOOGLE)
                        MainActivity.getGoogleClient().signOut();
                    startActivity((new Intent(NewHomeActivity.this, MainActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
                startService(new Intent(NewHomeActivity.this, NewPackageService.class));
                View headerView = navigationView.getHeaderView(0);
                userMail = headerView.findViewById(R.id.user_header_mail);
                userName = headerView.findViewById(R.id.user_header_name);
                userName.setText(currPerson.get_firstNAme() + " " + currPerson.get_lastName());
                userMail.setText(currPerson.get_email());
                UserManagmentFragment.setUserDetails();
                navigationView.setCheckedItem(R.id.nav_user_profile);
                dialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    public static Person getCurrPerson() {
        return currPerson;
    }

    public static void setCurrPerson(Person currPerson) {
        NewHomeActivity.currPerson = currPerson;
    }

    public static ArrayList<Person> getUsers() {
        return users;
    }
}
