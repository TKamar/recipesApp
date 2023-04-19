package com.example.recipesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Objects.User;
import com.example.recipesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {

    private final DataManager dataManager = DataManager.getInstance();

    private EditText verify_EDT_code1, verify_EDT_code2, verify_EDT_code3, verify_EDT_code4, verify_EDT_code5, verify_EDT_code6;
    private TextView verify_TXT_text_mobile;
    private TextView verify_TXT_resend;
    private Button verify_BTN_verify;
    private ProgressBar verify_PRB_progress_bar;

    // string for storing our verification ID
    private String verificationId;
    private String phoneNumber;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        findViews();
        setupInputs();
        phoneNumber = String.format("+972%s", getIntent().getStringExtra(getString(R.string.LBL_phone)));
        verify_TXT_text_mobile.setText(phoneNumber);

        // Get the Unique Id from the login Activity
        verificationId = getIntent().getStringExtra(getString(R.string.LBL_verification_Id));

        //Resend OTP
        verify_TXT_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumber);
            }
        });

        //Verify the OTP inserted
        verify_BTN_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify_EDT_code1.getText().toString().trim().isEmpty() ||
                        verify_EDT_code2.getText().toString().trim().isEmpty() ||
                        verify_EDT_code3.getText().toString().trim().isEmpty() ||
                        verify_EDT_code4.getText().toString().trim().isEmpty() ||
                        verify_EDT_code5.getText().toString().trim().isEmpty() ||
                        verify_EDT_code6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(VerifyActivity.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        verify_BTN_verify.setVisibility(View.GONE);
                        verify_PRB_progress_bar.setVisibility(View.VISIBLE);
                        String code = getCodeOTP();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        signInWithCredential(credential);
                    }
                    else
                        Toast.makeText(VerifyActivity.this, "please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getCodeOTP() {
        return verify_EDT_code1.getText().toString().trim() +
                verify_EDT_code2.getText().toString().trim() +
                verify_EDT_code3.getText().toString().trim() +
                verify_EDT_code4.getText().toString().trim() +
                verify_EDT_code5.getText().toString().trim() +
                verify_EDT_code6.getText().toString().trim();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if the code entered is correct or not.
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        verify_BTN_verify.setVisibility(View.VISIBLE);
                        verify_PRB_progress_bar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(VerifyActivity.this, "Authentication Success", Toast.LENGTH_LONG).show();
                            /** Now need to check if the user is already in the database or need to sign up */
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew){
                                // Move to verify activity
                                Intent intent = new Intent(VerifyActivity.this, SignUpActivity.class);
                                intent.putExtra(getString(R.string.LBL_phone), phoneNumber); //pass the phone number
                                startActivity(intent);
                                finish();
                            }else {
                                FirebaseUser user = task.getResult().getUser();
                                loadUserData(user.getUid());
                                // if the code is correct and the user exist
                                Intent i = new Intent(VerifyActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            // if the code is not correct then we are displaying an error message to the user.
                            Toast.makeText(VerifyActivity.this, "Authentication Failed - Enter the Correct OTP", Toast.LENGTH_LONG).show();
                        }
                    }


                });
    }

    private void loadUserData(String uid) {
        Log.d("verify", "uid: "+uid);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = dataManager.getRealTimeDB().getReference(Keys.KEY_USERS).child(uid);
        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User u = dataSnapshot.getValue(User.class);
                    Log.d("verify","U user: "+ u.toString());
                    dataManager.setCurrentUser(u);
                    Log.d("verify", "data manager: "+dataManager.getCurrentUser().toString());
                }
            }
        });
    }



    private void findViews() {
        verify_EDT_code1 = findViewById(R.id.verify_EDT_code1);
        verify_EDT_code2 = findViewById(R.id.verify_EDT_code2);
        verify_EDT_code3 = findViewById(R.id.verify_EDT_code3);
        verify_EDT_code4 = findViewById(R.id.verify_EDT_code4);
        verify_EDT_code5 = findViewById(R.id.verify_EDT_code5);
        verify_EDT_code6 = findViewById(R.id.verify_EDT_code6);
        verify_TXT_resend = findViewById(R.id.verify_TXT_resend);
        verify_TXT_text_mobile = findViewById(R.id.verify_TXT_text_mobile);
        verify_BTN_verify = findViewById(R.id.verify_BTN_verify);
        verify_PRB_progress_bar = findViewById(R.id.verify_PRB_progress_bar);
        myAuth = FirebaseAuth.getInstance();
    }

    private void setupInputs() {
        verify_EDT_code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    verify_EDT_code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        verify_EDT_code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    verify_EDT_code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        verify_EDT_code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    verify_EDT_code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        verify_EDT_code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    verify_EDT_code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        verify_EDT_code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    verify_EDT_code6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            // initializing our callbacks for on verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        /**
         *    below method is used when OTP is sent from Firebase
         *    after the code sent Moving to Verify Page
         */
        @Override
        public void onCodeSent(@NonNull String newS, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            verificationId = newS;
            Toast.makeText(VerifyActivity.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
        }

        // this method is called when user receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        }

        /**
         this method is called when firebase doesn't
         sends our OTP code due to any error or issue.
         */
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };

    /**
       this method is used for getting
       OTP (code) on user phone number.
    */
    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(myAuth)
                        .setPhoneNumber(number)      // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)    // Timeout and unit
                        .setActivity(this)                          // Activity (for callback binding)
                        .setCallbacks(mCallBack)                  // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}