package com.myvetpath.myvetpath;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsernameET = findViewById(R.id.UserNameET);
        mPasswordET = findViewById(R.id.PasswordET);
        mLoginButton = findViewById(R.id.loginBTTN);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Once we get the API, authenticate and verify the login info was correct. Right now we just assume that the login info is right

                //Store username as sharedpreference... may be a better way to do this
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.username_preference_key), mUsernameET.getText().toString());
                editor.apply();


                String name = preferences.getString(getString(R.string.username_preference_key), ""); //retrieve username from sharedpreferences
                Toast.makeText(LoginActivity.this, "Logged in as: " + name,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

}
