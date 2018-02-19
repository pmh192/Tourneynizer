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
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResponse;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.requesters.UserRequester;

public class LoginActivity extends AppCompatActivity {

	private static final int RESOLVE_CODE_READ = 1;
	private static final int RESOLVE_CODE_WRITE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		View loginButton = findViewById(R.id.loginButton);
		final CredentialsClient credentialsClient = Credentials.getClient(this);
		CredentialRequest request = new CredentialRequest.Builder().setPasswordLoginSupported(true).build();
		credentialsClient.request(request).addOnCompleteListener(
				new OnCompleteListener<CredentialRequestResponse>() {
					@Override
					public void onComplete(@NonNull Task<CredentialRequestResponse> task) {

						if (task.isSuccessful()) {
							// See "Handle successful credential requests"
							onCredentialRetrieved(task.getResult().getCredential());
							return;
						}
						Exception e = task.getException();
						if (e instanceof ResolvableApiException) {
							// This is most likely the case where the user has multiple saved
							// credentials and needs to pick one. This requires showing UI to
							// resolve the read request.
							ResolvableApiException rae = (ResolvableApiException) e;
							resolveResult(rae, RESOLVE_CODE_READ);
						} else if (e instanceof ApiException) {
							// The user must create an account or sign in manually.
							Log.e("Error", "Unsuccessful credential request.", e);
						}
					}
				});
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final TextView emailText = findViewById(R.id.email);
				final TextView passwordText = findViewById(R.id.password);
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
								Credential credential = new Credential.Builder(emailText.getText().toString()).setPassword(passwordText.getText().toString()).build();
								credentialsClient.save(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										if (task.isSuccessful()) {
											Log.d("Hype", "SAVE: OK");
											Toast.makeText(getApplicationContext(), "Credentials saved", Toast.LENGTH_SHORT).show();
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
												Toast.makeText(getApplicationContext(), "Save failed", Toast.LENGTH_SHORT).show();
											}
										} else {
											// Request has no resolution
											Toast.makeText(getApplicationContext(), "Save failed", Toast.LENGTH_SHORT).show();
										}
									}
								});
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

	private void onCredentialRetrieved(Credential credential) {
		UserRequester.getUserFromEmailAndPassword(getApplicationContext(), credential.getId(), credential.getPassword(), new UserRequester.OnUserLoadedListener() {
			@Override
			public void onUserLoaded(User user) {
				if (user != null) {
					goToMain(user);
				} else {
					showErrorMessage();
				}
			}
		});
	}

	private void resolveResult(ResolvableApiException rae, int requestCode) {
		try {
			rae.startResolutionForResult(this, requestCode);
			//mIsResolving = true;
		} catch (IntentSender.SendIntentException e) {
			Log.e("Error", "Failed to send resolution.", e);
			//hideProgress();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESOLVE_CODE_READ) {
			if (resultCode == RESULT_OK) {
				Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
				onCredentialRetrieved(credential);
			} else {
				Log.e("Error", "Credential Read: NOT OK");
				Toast.makeText(this, "Credential Read Failed", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == RESOLVE_CODE_WRITE) {
			if (resultCode == RESULT_OK) {
				Log.d("Hype", "SAVE: OK");
				Toast.makeText(this, "Credentials saved", Toast.LENGTH_SHORT).show();
			} else {
				Log.e("Error", "SAVE: Canceled by user");
			}
		}
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
}
