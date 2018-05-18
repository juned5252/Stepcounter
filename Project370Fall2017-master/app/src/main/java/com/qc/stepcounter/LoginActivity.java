package com.qc.stepcounter;


import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_KEY = "sharedPrefsTesting";
    private EditText username;
    private EditText password;
    private SharedPreferences login;
//    EditText editText1;
//    EditText editText2;
    private CallbackManager CB;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken ;
    RelativeLayout mylayout;
    AnimationDrawable animationDrawable;
    private Button submitButton;
    private Button registerButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        editText1=findViewById(R.id.editTextEmail);
//        editText2=findViewById(R.id.editTextPassword);
        ///username=findViewById(R.)
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken == null) {
                    //return;
                } else {
                    Intent main = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(main);
                }
            }
        });

        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username_edittext);
        password = (EditText) findViewById(R.id.password_edittext);
        submitButton = (Button) findViewById(R.id.submit_button);
        registerButton=findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("testKey", SHARED_PREFS_KEY);
                startActivity(intent);
            }

        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

            }
        });


        mylayout=findViewById(R.id.mylayout);
        animationDrawable= (AnimationDrawable) mylayout.getBackground();;
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        CB = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;
            }
        };

        LoginButton loginButton = findViewById(R.id.login_button);

        loginButton.registerCallback(CB, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(main);

            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException exception) {

            }
        });
        List<String> permissionNeeds = Arrays.asList("user_friends","email","user_birthday");
        loginButton.setReadPermissions(permissionNeeds);
        accessTokenTracker.startTracking();
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        CB.onActivityResult(requestCode, responseCode, intent);

    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
    }

}
