package com.dreamteam.tourneynizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.dreamteam.tourneynizer.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable emailText = ((EditText) findViewById(R.id.email)).getText();
                Editable passwordText = ((EditText) findViewById(R.id.password)).getText();
                // Check to see if username and password are valid
                // if so, advance to next activity (home page of app)
                // if not, display error message
                startActivity(TournamentListActivity.class);
            }
        });
        View registerAccountLink = findViewById(R.id.registerAccountLink);
        registerAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch activity to create account activity
                startActivity(RegisterActivity.class);
            }
        });
    }

    private void startActivity(Class<?> c) {
        startActivity(new Intent(this, c));
    }
}
