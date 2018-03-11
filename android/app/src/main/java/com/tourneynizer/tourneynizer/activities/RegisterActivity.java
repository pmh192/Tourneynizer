package com.tourneynizer.tourneynizer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.UserService;

import static com.tourneynizer.tourneynizer.activities.LaunchActivity.CREDENTIAL;

public class RegisterActivity extends AppCompatActivity {

    private static final int RESOLVE_CODE_WRITE = 1;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userService = new UserService();
        if (getIntent() != null) {
            Credential credential = getIntent().getParcelableExtra(CREDENTIAL);
            if (credential != null) {
                TextView emailText = findViewById(R.id.email);
                TextView nameText = findViewById(R.id.name);
                emailText.setText(credential.getId());
                nameText.setText(credential.getName());
            }
        }
        View registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameText = findViewById(R.id.name);
                TextView emailText = findViewById(R.id.email);
                TextView passwordText = findViewById(R.id.password);
                TextView confirmPasswordText = findViewById(R.id.confirmPassword);
                boolean ready = true;
                if (nameText.getText().toString().equals("")) {
                    nameText.setError("Please enter your name");
                    ready = false;
                }
                if (emailText.getText().toString().equals("")) {
					emailText.setError("Enter your email");
					ready = false;
				} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.getText()).matches()) {
					emailText.setError("Please enter a valid email");
					ready = false;
				}
                if (passwordText.getText().toString().equals("")) {
                    passwordText.setError("Please enter a password");
                    ready = false;
                }
                if (!passwordText.getText().toString().equals(confirmPasswordText.getText().toString())) {
                    confirmPasswordText.setError("The two passwords do not match");
                    ready = false;
                }
                if (ready) {
                    userService.createUser(nameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString(), new UserService.OnUserLoadedListener() {
                        @Override
                        public void onUserLoaded(User user) {
                            if (user != null) {
                                storeCredentials();
                                goToMain(user);
                            } else {
                                showErrorMessage();
                            }
                        }
                    });
                }
            }
        });
        View loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }

	private void storeCredentials() {
		TextView emailText = findViewById(R.id.email);
		TextView passwordText = findViewById(R.id.password);
		Credential credential = new Credential.Builder(emailText.getText().toString()).setPassword(passwordText.getText().toString()).build();
		CredentialsClient credentialsClient = Credentials.getClient(this);
		credentialsClient.save(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					Log.d("Hype", "SAVE: OK");
				} else {
                    Exception err = task.getException();
                    if (err instanceof ResolvableApiException) {
                        // Try to resolve the save request. This will prompt the user if
                        // the credential is new.
                        ResolvableApiException rae = (ResolvableApiException) err;
                        try {
                            rae.startResolutionForResult(RegisterActivity.this, RESOLVE_CODE_WRITE);
                        } catch (IntentSender.SendIntentException e) {
                            // Could not resolve the request
                            Log.e("Error", "Failed to send resolution.", e);
                        }
                    } else {
                        Log.e("Error", "Failed to send resolution.", err);
                        // Request has no resolution
                    }
                }
			}
		});
	}

    private void showErrorMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Couldn't Create Account");
        alertDialog.setMessage("Can't create an account with the given information. Make sure that your email address hasn't already been used for another account");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void goToMain(@NonNull User u) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USER, u);
        startActivity(intent);
        finishAffinity();
    }
}
