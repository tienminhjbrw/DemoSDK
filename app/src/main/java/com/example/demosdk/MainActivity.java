package com.example.demosdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    public LoginButton loginButton;
    private static final String GAMINGPROFILE = "gaming_profile";
    private static final String GAMINGUSERPICTURE = "gaming_user_picture";
    private static final String OPENID = "openid";
    private static final String EMAIL = "email";
    private static final String PUBLICPROFILE = "public_profile";
    public CallbackManager callbackManager = CallbackManager.Factory.create();
    public AppEventsLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.fullyInitialize();
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(GAMINGUSERPICTURE, GAMINGPROFILE);

        getKeyhash(getApplicationContext());

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login succesfully" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();

                logger = AppEventsLogger.newLogger(getApplicationContext());
                Bundle params = new Bundle();
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "USD");
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "[{\"id\": \"1234\", \"quantity\": 2}, {\"id\": \"5678\", \"quantity\": 1}]");
                params.putString("MyParam", "this is the tested param");

                Log.i("test", "logging");

                logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED,
                        50,
                        params);
                logger.logEvent("MyTestEvent", 100, params);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    public static void getKeyhash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KEY HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}