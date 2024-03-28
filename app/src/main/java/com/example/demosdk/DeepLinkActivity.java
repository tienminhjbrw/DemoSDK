package com.example.demosdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tiktok.open.sdk.auth.AuthApi;
import com.tiktok.open.sdk.auth.AuthResponse;

public class DeepLinkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);

        AuthApi authApi = new AuthApi(DeepLinkActivity.this);
        String redirectUri = "http://www.example.com/demosdk";

        Intent intent = getIntent();
        AuthResponse authResponse = authApi.getAuthResponseFromIntent(intent, redirectUri);
        if (authResponse != null) {
            String authCode = authResponse.getAuthCode();
            String grantedPermissions = authResponse.getGrantedPermissions();
            Log.i("tiktoksdk", authResponse.getAuthError());
            Log.i("tiktoksdk", authResponse.getErrorMsg());
            Toast.makeText(getApplicationContext(), "auth : " + authCode + "\npermission : " + grantedPermissions, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();
        }
    }
}
