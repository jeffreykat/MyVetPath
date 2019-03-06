package com.myvetpath.myvetpath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//This screen will have all the settings that the user can view
public class SettingsActivity extends BaseActivity {
    private Button mLoginButton;
    private Button mLogoutButton;
    private Intent login_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setMenuOptionItemToRemove(this);
        toolbar.setTitle(R.string.action_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoginButton = findViewById(R.id.logInButton);
        mLogoutButton = findViewById(R.id.logOutButton);
        login_activity = new Intent(this, LoginActivity.class);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //navigate user to login activity
                startActivity(login_activity);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //log user out
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.username_preference_key), ""); //set the username to "", which indicates that the user is not logged in. A username cannot be ""
                editor.apply();
                Toast.makeText(SettingsActivity.this, "You are now logged out.", Toast.LENGTH_LONG).show();
            }
        });
    }


}
