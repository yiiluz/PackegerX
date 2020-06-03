package com.example.packegerv2.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packegerv2.R;
import com.example.packegerv2.model.FireBaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    private ConstraintLayout constraintLayout;
    private static FirebaseAuth mAuth = null;
    private Button in;
    private EditText mail, pass;
    private Geocoder geocoder;
    private static FirebaseDatabase database;
    private TextView user, notRgistred, forgotPass;
    private static GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInButton;
    private static String currUserMail;
    private static String signInMethod;
    public static String USERS_PATH = "users";
    private final String DEFAULT_PASS = "shgrf87w34yuqwbdm";
    private static final String CITIES_URL = "https://data.gov.il/dataset/3fc54b81-25b3-4ac7-87db-248c3e1602de/resource/72bd51be-512b-4430-b2d2-f3295c90e569/download/yeshuvim_20200401.xml";
    private static final String STREETS_URL = "https://data.gov.il/dataset/321/resource/d04feead-6431-427f-81bc-d6a24151c1fb/download/rechovot_20200401.xml";
    private int PERMISSION_ID = 44;
    private static String longAt, latAt, addresses;
    private FusedLocationProviderClient mFusedLocationClient;
    private static double lat, lng;
    private static ArrayList<String> cities = new ArrayList<>();
    private static boolean isCitiesUpdated = false;
    private static boolean isAddressSet = false;
    private static com.example.packegerv2.Entities.Address deviceAddress;
    private ProgressDialog dialog;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            resolveAddressFromLatAndLng();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = MainActivity.getMAuth();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                dialog.hide();
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                System.exit(2);
            }
        });

        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Wait...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        constraintLayout = findViewById(R.id.mainLayout);
        constraintLayout.getBackground().setAlpha(30);
//        try {
//            if (!isCitiesUpdated) {
//                new DownloadXmlTask().execute(CITIES_URL); //get the cities list
//            }
//        } catch (Exception e) {
//        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton = findViewById(R.id.sign_in_button);
        in = findViewById(R.id.buttonIn);
        mail = findViewById(R.id.editTextMail);
        pass = findViewById(R.id.editTextPass);
        user = findViewById(R.id.textViewUser);
        notRgistred = findViewById(R.id.textViewNotRegistred);
        forgotPass = findViewById(R.id.textViewForgotPass);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mail.getText().toString().isEmpty()) {
                    mAuth.sendPasswordResetEmail(mail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Email sent successfully!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } else {
                    mail.setError("Check the input mail");
                    mail.requestFocus();
                }
            }
        });

        notRgistred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mail.getText().toString();
                String pwd = pass.getText().toString();
                if (email.isEmpty()) {
                    mail.setError("Please enter email id");
                    mail.requestFocus();
                } else if (pwd.isEmpty()) {
                    pass.setError("Please enter your password");
                    pass.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(MainActivity.this, "Login successfully as " + email, Toast.LENGTH_SHORT).show();
                                        currUserMail = mAuth.getCurrentUser().getEmail();
                                        signInMethod = "firebase";
                                        dialog.hide();
                                        startActivity(new Intent(MainActivity.this, NewHomeActivity.class));
                                        //finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.hide();
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    dialog.hide();
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
        getLastLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        MainActivity.lat = lat;
    }

    public static double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        MainActivity.lng = lng;
    }

    public static boolean isIsAddressSet() {
        return isAddressSet;
    }

    public static void setIsAddressSet(boolean isAddressSet) {
        MainActivity.isAddressSet = isAddressSet;
    }

    public static com.example.packegerv2.Entities.Address getDeviceAddress() {
        return deviceAddress;
    }

    public static String getSignInMethod() {
        return signInMethod;
    }

    public static String getCurrUser() {
        return currUserMail;
    }

    public static FirebaseAuth getMAuth() {
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        ;
        return mAuth;
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(FirebaseDatabase database) {
        MainActivity.database = database;
    }

    public static GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }

    public static ArrayList<String> getCities() {
        return cities;
    }


    /**
     * This is the functions that handle sign in by google
     */
    private void signIn() {
        dialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(final Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                final String personName = acct.getDisplayName();
                final String personGivenName = acct.getGivenName();
                final String personFamilyName = acct.getFamilyName();
                final String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                mAuth.fetchSignInMethodsForEmail(personEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().size() == 0) {
                            dialog.hide();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Enter Your Phone number:");

                            // Set up the input
                            final EditText phoneOfGoogleUser = new EditText(MainActivity.this);
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            phoneOfGoogleUser.setInputType(InputType.TYPE_CLASS_PHONE);
                            builder.setView(phoneOfGoogleUser);

                            // Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FireBaseAuth.getFirebaseAuthObj().createUserInFireBase(personEmail, DEFAULT_PASS, personGivenName, personFamilyName, phoneOfGoogleUser.getText().toString(), "");
                                    handleSignInResult(completedTask);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        }
                        mAuth.signInWithEmailAndPassword(personEmail, DEFAULT_PASS)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(MainActivity.this, "Login successfully as " + personEmail, Toast.LENGTH_SHORT).show();
                                            currUserMail = mAuth.getCurrentUser().getEmail();
                                            signInMethod = "google";
                                            dialog.hide();
                                            startActivity(new Intent(MainActivity.this, NewHomeActivity.class));
                                        } else {
                                            dialog.hide();
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(MainActivity.this, "Google Login Failed with " + personEmail, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });


            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            dialog.hide();
            Toast.makeText(MainActivity.this, "Error signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
            //updateUI(null);
        }
    }


    /**
     * This is the functions that handle location
     */
    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat = location.getLatitude();
                                    lng = location.getLongitude();
                                    resolveAddressFromLatAndLng();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    public static void setDeviceAddress(com.example.packegerv2.Entities.Address deviceAddress) {
        MainActivity.deviceAddress = deviceAddress;
    }

    private void resolveAddressFromLatAndLng() {
        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(MainActivity.getLat(), MainActivity.getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Can't resolve address. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        deviceAddress = new com.example.packegerv2.Entities.Address(lat, lng, address);
        setIsAddressSet(true);
    }

    //
//
//    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            XmlPullParserFactory pullParserFactory;
//            try {
//                pullParserFactory = XmlPullParserFactory.newInstance();
//                XmlPullParser parser = pullParserFactory.newPullParser();
//
//                InputStream in_s = downloadUrl(CITIES_URL);
//                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//                parser.setInput(in_s, null);
//
//                cities = parseXMLForCities(parser);
//
//            } catch (XmlPullParserException e) {
//
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return "";
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            //setContentView(R.layout.activity_register);
//            if (!isCitiesUpdated) {
//                isCitiesUpdated = true;
//            }
//        }
//    }
//
//    private InputStream downloadUrl(String urlString) throws IOException {
//        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(10000 /* milliseconds */);
//        conn.setConnectTimeout(15000 /* milliseconds */);
//        conn.setRequestMethod("GET");
//        conn.setDoInput(true);
//        conn.connect();
//        InputStream stream = conn.getInputStream();
//        return stream;
//    }
//
//    private ArrayList<String> parseXMLForCities(XmlPullParser parser) throws XmlPullParserException, IOException {
//        //ArrayList<product> products = null;
//        ArrayList<String> cities = new ArrayList<>();
//        int eventType = parser.getEventType();
//        //Product currentProduct = null;
//
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            String name = null;
//            switch (eventType) {
//                case XmlPullParser.START_DOCUMENT:
//                    //products = new ArrayList();
//                    cities = new ArrayList<>();
//                    break;
//                case XmlPullParser.START_TAG:
//                    name = parser.getName();
//                    if (name.equalsIgnoreCase("שם_ישוב")) {
//                        cities.add(parser.nextText().replaceAll(" ", ""));
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//            }
//            eventType = parser.next();
//        }
//        return cities;
//    }

}
