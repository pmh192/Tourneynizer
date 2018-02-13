package com.tourneynizer.tourneynizer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.tourneynizer.tourneynizer.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable nameText = ((EditText) findViewById(R.id.name)).getText();
                Editable emailText = ((EditText) findViewById(R.id.email)).getText();
                Editable passwordText = ((EditText) findViewById(R.id.password)).getText();
                Editable confirmPasswordText = ((EditText) findViewById(R.id.confirmPassword)).getText();
                // attempt to create account through info by sending data to back end
                // if works, advance to next activity (home page of app)
                // if not, display error message
                if (passwordText == confirmPasswordText || passwordText.toString().equals(confirmPasswordText.toString())) {
                    startActivity(MainActivity.class);
                }
            }
        });
        View registerAccountLink = findViewById(R.id.loginLink);
        registerAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch activity to create account activity
                startActivity(LoginActivity.class);
            }
        });
    }

    private void startActivity(Class<?> c) {
        startActivity(new Intent(this, c));
        // remove activities from stack if user is logged in
        if (!c.equals(LoginActivity.class)) {
            finishAffinity();
        }
    }
}
