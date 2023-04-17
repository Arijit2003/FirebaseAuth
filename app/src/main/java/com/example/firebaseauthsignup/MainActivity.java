package com.example.firebaseauthsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
    private SignInButton signInBtn;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(MainActivity.this,Dashboard.class);
            intent.putExtra("email",currentUser.getEmail());
            intent.putExtra("userID",currentUser.getUid());
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        beginRequest();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogIn();
            }
        });


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
        signInBtn=findViewById(R.id.signInBtn);
        mAuth = FirebaseAuth.getInstance();
    }
    private void signUpInFirebaseAuth(){
        progressBar.setVisibility(View.VISIBLE);
        String email= Objects.requireNonNull(emailTIET.getText()).toString().trim();
        String password= Objects.requireNonNull(passwordTIET.getText()).toString();

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
    private void beginRequest(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);


    }

    private void processLogIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,101);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 101:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(this, "Error in getting google's information", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this,Dashboard.class);
                            assert user != null;
                            intent.putExtra("email",user.getEmail());
                            intent.putExtra("userID",user.getUid());
                            startActivity(intent);

                        }else{
                            Toast.makeText(MainActivity.this, "Problem in Firebase Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}