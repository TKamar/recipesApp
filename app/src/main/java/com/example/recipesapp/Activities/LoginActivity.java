package com.example.recipesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText login_EDT_phone_number;
    private Button login_BTN_send;
    private ProgressBar login_PRB_progress_bar;

    // variable for FirebaseAuth class
    private FirebaseAuth myAuth;
    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            // initializing our callbacks for on verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        /**
         *    below method is used when OTP is sent from Firebase
         *    after the code sent Moving to Verify Page
         */
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            login_BTN_send.setVisibility(View.VISIBLE);
            login_PRB_progress_bar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
            // Move to verify activity
            Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
            intent.putExtra(getString(R.string.LBL_phone), login_EDT_phone_number.getText().toString().trim()); //pass the phone number
            intent.putExtra(getString(R.string.LBL_verification_Id), s); //pass the unique id
            startActivity(intent);
            finish();

        }

        // this method is called when user receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            login_BTN_send.setVisibility(View.GONE);
            login_PRB_progress_bar.setVisibility(View.VISIBLE);
        }

        /**
         this method is called when firebase doesn't
         sends our OTP code due to any error or issue.
         */
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            login_PRB_progress_bar.setVisibility(View.GONE);
            login_BTN_send.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        // On Click Send Code -OTP-
        login_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check not Empty
                if (TextUtils.isEmpty(login_EDT_phone_number.getText().toString()))
                    Toast.makeText(LoginActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    // Check valid phone number  - length
                else if (login_EDT_phone_number.getText().toString().length() != 9)
                    Toast.makeText(LoginActivity.this, "Type valid Phone Number", Toast.LENGTH_SHORT).show();
                else {
                    login_BTN_send.setVisibility(View.GONE);
                    login_PRB_progress_bar.setVisibility(View.VISIBLE);
                    String number = login_EDT_phone_number.getText().toString(); // Get the phone number that insert
                    sendVerificationCode(number);
                }
            }
        });
    }

    /*
        this method is used for getting
        OTP (code) on user phone number.
     */
    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(myAuth)
                        .setPhoneNumber(getString(R.string.IL_num) + number)      // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)    // Timeout and unit
                        .setActivity(this)                          // Activity (for callback binding)
                        .setCallbacks(mCallBack)                  // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void findViews() {
        login_EDT_phone_number = findViewById(R.id.login_EDT_phone_number);
        login_BTN_send = findViewById(R.id.login_BTN_send);
        login_PRB_progress_bar = findViewById(R.id.verify_PRB_progress_bar);
        myAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}