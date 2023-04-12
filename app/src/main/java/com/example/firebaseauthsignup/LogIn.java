package com.example.firebaseauthsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private TextInputEditText emailTIET;
    private TextInputEditText passwordTIET;
    Button btnLogIn;
    FirebaseAuth mAuth;
    ProgressBar progressBarLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initialize();
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= Objects.requireNonNull(emailTIET.getText()).toString().trim();
                String password= Objects.requireNonNull(passwordTIET.getText()).toString().trim();
                if(!email.equals("") && !password.equals("")) {
                    loginFireBaseAuth(email, password);
                }
                else if (email.equals("")&& password.equals("")) {
                    Toast.makeText(LogIn.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(LogIn.this, "Enter email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LogIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void initialize(){
        emailTIET=findViewById(R.id.emailLogIn);
        passwordTIET=findViewById(R.id.passwordLogIn);
        btnLogIn=findViewById(R.id.btnSaveLogIn);
        mAuth=FirebaseAuth.getInstance();
        progressBarLogIn=findViewById(R.id.progressBarLogIn);
    }
    private void loginFireBaseAuth(String email,String password){
        progressBarLogIn.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LogIn.this,Dashboard.class);
                            intent.putExtra("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                            intent.putExtra("userID",mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            progressBarLogIn.setVisibility(View.INVISIBLE);
                            emailTIET.setText("");
                            passwordTIET.setText("");
                            finish();
                        } else {
                            emailTIET.setText("");
                            passwordTIET.setText("");
                            Toast.makeText(LogIn.this, "Invalid password or email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}