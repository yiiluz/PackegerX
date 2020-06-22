package com.packager.user.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.packager.user.R;
import com.packager.user.model.FireBaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private Button signUpButton;
    private EditText newMail, newFName, newLName, newPass, newPass2, newPhone;
    private AutoCompleteTextView cityAuto;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                System.exit(2);
            }
        });


        signUpButton = findViewById(R.id.buttonSignUp);
        newMail = findViewById(R.id.editTextMail);
        newFName = findViewById(R.id.editTextFName);
        newLName = findViewById(R.id.editTextLName);
        newPass = findViewById(R.id.editTextPassword);
        newPass2 = findViewById(R.id.editTextPassword2);
        newPhone = findViewById(R.id.editTextPhone);
        scrollView = findViewById(R.id.mainLayout);
        scrollView.getBackground().setAlpha(30);
        //cityAuto =findViewById(R.id.cityAutoComplete);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, MainActivity.getCities());
//        cityAuto.setAdapter(adapter);
//        Toast.makeText(RegisterActivity.this, "Adapter is set!", Toast.LENGTH_SHORT).show();
    }

    public void onClickSignUp(View v) {
        boolean isOK = true;
        final String email = newMail.getText().toString();
        String firstPass = newPass.getText().toString();
        String secondPass = newPass2.getText().toString();
        if (email.isEmpty()) {
            newMail.setError("Please enter email id");
            newMail.requestFocus();
            isOK = false;
        }
        if (firstPass.isEmpty()) {
            newPass.setError("Please enter your password");
            newPass.requestFocus();
            isOK = false;
        }
        if (!firstPass.contentEquals(secondPass)) {
            Toast.makeText(RegisterActivity.this, "Password are not matched !", Toast.LENGTH_SHORT).show();
            newPass.setError("Password are not matched !");
            newPass2.setError("Password are not matched !");
            newPass.requestFocus();
            newPass2.requestFocus();
            isOK = false;
        }
        if (email.isEmpty() && firstPass.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
            isOK = false;
        } else if (isOK && (!email.isEmpty() && !firstPass.isEmpty())) {
            int exit = FireBaseAuth.getFirebaseAuthObj().createUserInFireBase(email, firstPass, newFName.getText().toString(), newLName.getText().toString(), newPhone.getText().toString(), "");
            if (exit == -1) {
                Toast.makeText(RegisterActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "SignUp Successful, You can Login now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
        }
        return;
    }
}
