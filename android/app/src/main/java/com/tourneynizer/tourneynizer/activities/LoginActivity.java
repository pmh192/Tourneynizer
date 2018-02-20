package com.tourneynizer.tourneynizer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.requesters.UserRequester;

import static com.tourneynizer.tourneynizer.activities.LaunchActivity.CREDENTIAL;

public class LoginActivity extends AppCompatActivity {

	private static final int RESOLVE_CODE_WRITE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (getIntent() != null) {
			Credential credential = getIntent().getParcelableExtra(CREDENTIAL);
			if (credential != null) {
				TextView emailText = findViewById(R.id.email);
				TextView passwordText = findViewById(R.id.password);
				emailText.setText(credential.getId());
				passwordText.setText(credential.getPassword());
			}
		}
		View loginButton = findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TextView emailText = findViewById(R.id.email);
				TextView passwordText = findViewById(R.id.password);
				boolean ready = true;
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
				if (ready) {
					UserRequester.getUserFromEmailAndPassword(getApplicationContext(), emailText.getText().toString(), passwordText.getText().toString(), new UserRequester.OnUserLoadedListener() {
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
		View registerAccountLink = findViewById(R.id.registerAccountLink);
		registerAccountLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// switch activity to create account activity
				goToRegister();
			}
		});
	}

	private void showErrorMessage() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Couldn't Log In");
		alertDialog.setMessage("The credentials you entered are incorrect");
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		alertDialog.show();
	}

	private void goToRegister() {
		startActivity(new Intent(this, RegisterActivity.class));
	}

	private void goToMain(@NonNull User u) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.USER, u);
		startActivity(intent);
		finishAffinity();
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
					return;
				}
				Exception err = task.getException();
				if (err instanceof ResolvableApiException) {
					// Try to resolve the save request. This will prompt the user if
					// the credential is new.
					ResolvableApiException rae = (ResolvableApiException) err;
					try {
						rae.startResolutionForResult(LoginActivity.this, RESOLVE_CODE_WRITE);
					} catch (IntentSender.SendIntentException e) {
						// Could not resolve the request
						Log.e("Error", "Failed to send resolution.", e);
					}
				} else {
					// Request has no resolution
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESOLVE_CODE_WRITE) {
            if (resultCode == RESULT_OK) {
                Log.d("Hype", "SAVE: OK");
                Toast.makeText(this, "Credentials saved", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "SAVE: Canceled by user");
            }
        }
	}
}
