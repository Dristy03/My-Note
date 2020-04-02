package com.example.mynoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton=findViewById(R.id.loginButtonID);
        registerTv=findViewById(R.id.registerTextViewId);

        loginButton.setOnClickListener(this);
        registerTv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.loginButtonID:
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;

                case R.id.registerTextViewId:
                Intent intent2=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
