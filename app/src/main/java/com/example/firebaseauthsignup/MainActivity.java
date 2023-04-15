package com.example.firebaseauthsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView forgetPassword;
    TextInputEditText emailTIET;
    TextInputEditText passwordTIET;
    CircleImageView userImage;
    Button btnSave;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    private TextView alreadyRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();



        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LogIn.class));
                emailTIET.setText("");
                passwordTIET.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpInFirebaseAuth();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MobileVerification.class);
                startActivity(intent);
            }
        });

    }


    private void initialize(){
        emailTIET=findViewById(R.id.email);
        passwordTIET=findViewById(R.id.password);
        userImage=findViewById(R.id.userImage);
        btnSave=findViewById(R.id.btnSave);
        progressBar=findViewById(R.id.progressBar);
        alreadyRegistered=findViewById(R.id.alreadyRegistered);
        forgetPassword=findViewById(R.id.forgetPassword);
    }
    private void signUpInFirebaseAuth(){
        progressBar.setVisibility(View.VISIBLE);
        String email= Objects.requireNonNull(emailTIET.getText()).toString().trim();
        String password= Objects.requireNonNull(passwordTIET.getText()).toString();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            emailTIET.setText("");
                            passwordTIET.setText("");
                            Toast.makeText(MainActivity.this, "User successfully signed up", Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            emailTIET.setText("");
                            passwordTIET.setText("");
                            Toast.makeText(MainActivity.this, "Process error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}