package com.example.mynoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private EditText registerEmailEt, registerPasswordEt;
    private Button signinButton;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerEmailEt = findViewById(R.id.registerEmailId);
        registerPasswordEt = findViewById(R.id.registerPasswordId);
        signinButton = findViewById(R.id.signInbuttonId);

        signinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userRegister();
    }

    private void userRegister() {
        email = registerEmailEt.getText().toString().trim();
        password = registerPasswordEt.getText().toString().trim();

        if (email.isEmpty()) {
            registerEmailEt.setError("Enter an email address");
            registerEmailEt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerEmailEt.setError("Enter a valid email address");
            registerEmailEt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            registerPasswordEt.setError("Enter a password");
            registerPasswordEt.requestFocus();
            return;
        }
        if (password.length() < 6) {
            registerPasswordEt.setError("Minimum length of a password should be 6");
            registerPasswordEt.requestFocus();
            return;
        }
        //signupprogressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //signupprogressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onComplete: regi completed");
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            addToDatabase();

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User id already registered.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        // ...
                    }
                });
    }

    private void addToDatabase() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("boolean",true);
        assert email != null;
        FirebaseFirestore.getInstance().collection("Users").document(email).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //pd.dismiss();
                        startActivity(new Intent(getApplicationContext(), NoteActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //pd.dismiss();
                Log.d(TAG, "onFailure: " + e.getMessage());
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
