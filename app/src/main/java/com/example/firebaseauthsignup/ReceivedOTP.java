package com.example.firebaseauthsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ReceivedOTP extends AppCompatActivity {
    private EditText otpET;
    private Button submitOtpBtn;
    FirebaseAuth mAuth;
    String phoneNo;
    String otpID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_otp);

        phoneNo=getIntent().getStringExtra("phone").trim();
        otpET=findViewById(R.id.otpET);
        submitOtpBtn=findViewById(R.id.submitOtpBtn);
        mAuth=FirebaseAuth.getInstance();

        initiateOtp();

        submitOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpET.getText().toString().isEmpty()){
                    Toast.makeText(ReceivedOTP.this, "Blank field can not be processed", Toast.LENGTH_SHORT).show();
                }
                else if(otpET.getText().toString().length()!=6){
                    Toast.makeText(ReceivedOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
                else{
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(otpID,otpET.getText().toString());
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });


    }
    private void initiateOtp(){
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNo)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(ReceivedOTP.this)                 // (optional) Activity for callback binding
//                        // If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                otpID=s;
//
//                            }
//
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                signInWithPhoneAuthCredential(phoneAuthCredential);
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(ReceivedOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                Log.d("Failure",e.getMessage());
//                            }
//                        })          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo,
                60, TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        otpID=s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ReceivedOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Failure",e.getMessage());
                    }
                });
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(ReceivedOTP.this,Dashboard.class);
                            intent.putExtra("email",phoneNo);
                            intent.putExtra("userID",credential.getProvider());
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(ReceivedOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}