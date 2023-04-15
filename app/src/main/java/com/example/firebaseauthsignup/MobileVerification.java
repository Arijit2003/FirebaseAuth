package com.example.firebaseauthsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class MobileVerification extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText phoneNumberET;
    private Button sendOtpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        initialize();
        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MobileVerification.this,ReceivedOTP.class);
                intent.putExtra("phone",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
                finish();
            }
        });
    }
    private void initialize(){
        ccp=findViewById(R.id.ccp);
        phoneNumberET=findViewById(R.id.phoneNumberET);
        sendOtpBtn=findViewById(R.id.sendOtpBtn);
        ccp.registerCarrierNumberEditText(phoneNumberET);

    }
}