package com.example.demosdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
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
import java.util.HashMap;
import java.util.Map;

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

        Button button = findViewById(R.id.test_btn);
        Button button2 = findViewById(R.id.test_btn2);
        Button button3 = findViewById(R.id.test_btn3);
        Button button4 = findViewById(R.id.test_btn4);

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

        // Appflyer
        AppsFlyerLib.getInstance().init("C4xBYd7TwxFggGyocWvQA3", null, this);
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().start(getApplicationContext(), "C4xBYd7TwxFggGyocWvQA3", new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("APPFLYER", "Launch sent successfully, got 200 response code from server");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.d("APPFLYER", "Launch failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> eventValues = new HashMap<String, Object>();
                eventValues.put(AFInAppEventParameterName.PRICE, "200000");
                eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, "test_content_type");
                eventValues.put(AFInAppEventParameterName.CONTENT_ID, "test_content_id");
                eventValues.put(AFInAppEventParameterName.REVENUE, 1);

                AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                        AFInAppEventType.CONTENT_VIEW, eventValues, new AppsFlyerRequestListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("APPFLYER", "Event contentview sent successfully");
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Log.d("APPFLYER", "Event contentview failed to be sent:\nError code: " + i + "\nError description: " + s);
                            }
                        });
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> eventValues1 = new HashMap<String, Object>();
                eventValues1.put(AFInAppEventParameterName.CONTENT_ID, "test_content_id2");
                eventValues1.put(AFInAppEventParameterName.CONTENT_TYPE, "test_content_type2");
                eventValues1.put(AFInAppEventParameterName.CURRENCY, "VND");
                eventValues1.put(AFInAppEventParameterName.REVENUE, 100000);

                AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                        AFInAppEventType.ADD_TO_CART, eventValues1, new AppsFlyerRequestListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("APPFLYER", "Event add to cart sent successfully");
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Log.d("APPFLYER", "Event add to cart failed to be sent:\nError code: " + i + "\nError description: " + s);
                            }
                        });
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> eventValues2 = new HashMap<String, Object>();
                eventValues2.put("my_readings_1", "my_data");
                eventValues2.put("my_readings_2", 12345);
                eventValues2.put(AFInAppEventParameterName.REVENUE, 1);

                AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                        "my_event2", eventValues2, new AppsFlyerRequestListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("APPFLYER", "My event sent successfully");
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Log.d("APPFLYER", "My event revenue failed to be sent:\nError code: " + i + "\nError description: " + s);
                            }
                        });
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> eventValues2 = new HashMap<String, Object>();
                eventValues2.put(AFInAppEventParameterName.AD_REVENUE_AD_SIZE, "ad_size");
                eventValues2.put(AFInAppEventParameterName.AD_REVENUE_NETWORK_NAME, "ad_networkname");
                eventValues2.put(AFInAppEventParameterName.REVENUE, 1);

                AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                        AFInAppEventType.AD_CLICK, eventValues2, new AppsFlyerRequestListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("APPFLYER", "Event AD sent successfully");
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Log.d("APPFLYER", "Event AD failed to be sent:\nError code: " + i + "\nError description: " + s);
                            }
                        });
            }
        });

        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("id_may", m_androidId);
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