package com.tourneynizer.tourneynizer.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResponse;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.requesters.UserRequester;

public class LaunchActivity extends AppCompatActivity {

    private static final int RESOLVE_CODE_READ = 1;

    private CredentialsClient credentialsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        credentialsClient = Credentials.getClient(this);
        CredentialRequest request = new CredentialRequest.Builder().setPasswordLoginSupported(true).build();
        credentialsClient.request(request).addOnCompleteListener(new OnCompleteListener<CredentialRequestResponse>() {
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
                } else {
                    // The user must create an account or sign in manually.
                    Log.e("Error", "Unsuccessful credential request.", e);
                    goToRegister();
                }
            }
        });
    }

    private void onCredentialRetrieved(Credential credential) {
        goToLogin(credential);
    }

    private void resolveResult(ResolvableApiException rae, int requestCode) {
        try {
            rae.startResolutionForResult(this, requestCode);
        } catch (IntentSender.SendIntentException e) {
            Log.e("Error", "Failed to send resolution.", e);
            goToRegister();
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
                goToRegister();
            }
        }
    }

    private void goToLogin(Credential credential) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.CREDENTIAL, credential);
        startActivity(intent);
        finishAffinity();
    }

    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void goToMain(@NonNull User u) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USER, u);
        startActivity(intent);
        finishAffinity();
    }
}
