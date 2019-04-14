package com.myvetpath.myvetpath;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.UserTable;

import java.util.List;

public class LoginActivity extends BaseActivity {
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginButton;
    List<UserTable> savedUsers;

    MyVetPathViewModel viewModel;
    boolean userSaved = false;

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

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        viewModel.getUsers().observe(this, new Observer<List<UserTable>>() {
            @Override
            public void onChanged(@Nullable List<UserTable> userTables) {
                if(userTables != null){
                    savedUsers = userTables;
                } else{
                    savedUsers = null;
                }
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Store username as sharedpreference... may be a better way to do this
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.username_preference_key), mUsernameET.getText().toString());
                editor.apply();


                String name = preferences.getString(getString(R.string.username_preference_key), ""); //retrieve username from sharedpreferences
                checkSavedUser(name);
                Toast.makeText(LoginActivity.this, "Logged in as: " + name,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    UserTable checkSavedUser(String username){
        for(UserTable tempUser: savedUsers){
            if(tempUser.Username == username){
                userSaved = true;
                return tempUser;
            }
        }
        //TODO: Once we get the API, authenticate and verify the login info was correct. Right now we just assume that the login info is right
        UserTable newUser = new UserTable();
        newUser.Username = username;
        newUser.Password = mPasswordET.getText().toString();
        viewModel.insertUser(newUser);
        return newUser;
    }

}
