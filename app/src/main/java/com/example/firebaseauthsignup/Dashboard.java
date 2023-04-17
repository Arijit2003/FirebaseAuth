package com.example.firebaseauthsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    TextView emailIdDashboardTV;
    TextView userIdDashboardTV;
    Button logOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialize();
        emailIdDashboardTV.setText(getIntent().getStringExtra("email"));
        userIdDashboardTV.setText(getIntent().getStringExtra("userID"));
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });
    }

    private void initialize(){
        emailIdDashboardTV=findViewById(R.id.emailIdDashboardTV);
        userIdDashboardTV=findViewById(R.id.userIdDashboardTV);
        logOutBtn=findViewById(R.id.logOutBtn);
    }

}