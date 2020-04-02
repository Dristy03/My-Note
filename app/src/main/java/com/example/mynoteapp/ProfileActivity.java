package com.example.mynoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextInputEditText displayNameEditText;
    Button upPRofileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circleImageView=findViewById(R.id.imageId);
        displayNameEditText=findViewById(R.id.dispalyNameTvId);
        upPRofileButton=findViewById(R.id.upProfileButtonID);
    }

    public void updateProfile(View view) {
    }
}
