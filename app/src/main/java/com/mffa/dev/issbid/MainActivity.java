package com.mffa.dev.issbid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button signin_signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin_signup_btn = findViewById(R.id.signin_signup_btn);
        signin_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityUtama = new Intent(MainActivity.this,ActivityUtama.class);
                startActivity(activityUtama);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
