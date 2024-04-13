package com.mirea.bykonyaia.mireaproject.labs.lab_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirea.bykonyaia.mireaproject.MainActivity;
import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();
    private ActivitySignInBinding binding = null;
    private FirebaseAuth mAuth = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckCurrentState();
    }
    private void CheckCurrentState() {
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void OnCreateAccountButtonClicked(View v) {
        createAccount(binding.authorizationEmailInput.getText().toString(), binding.authorizationPasswordInput.getText().toString());
    }
    public void OnSignInButtonClicked(View v) {
        signIn(binding.authorizationEmailInput.getText().toString(), binding.authorizationPasswordInput.getText().toString());
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        CheckCurrentState();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        CheckCurrentState();
                    }
                }
            });
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        CheckCurrentState();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        CheckCurrentState();
                    }

                }
            });
    }
}