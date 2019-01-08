package com.myvetpath.myvetpath;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent create_sub_activity;
    Button create_sub_button;

    Intent create_account_activity;
    Button create_account_button;

    Intent view_submissions_activity;
    Button view_submissions_button;

    Intent view_drafts_activity;
    Button view_drafts_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        create_sub_activity = new Intent(this, CreateSubActivity.class);

        create_sub_button = findViewById(R.id.createSubButton);
        create_sub_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(create_sub_activity);
            }
        });

        create_account_activity = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_URL))); //TODO Just use placeholder web page for now, update later when we set up account creation page
        create_account_button = findViewById(R.id.createAccountButton);
        create_account_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(create_account_activity.resolveActivity(getPackageManager()) != null){ //if there is a web browser on phone, go ahead and navigate to that page
                    startActivity(create_account_activity);
                }
            }
        });

        view_submissions_activity = new Intent(this, ViewSubmissionsActivity.class);
        view_submissions_button = findViewById(R.id.viewSubsButton);
        view_submissions_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(view_submissions_activity);
            }
        });

        view_drafts_activity = new Intent(this, ViewDraftsActivity.class);
        view_drafts_button = findViewById(R.id.viewDraftsButton);
        view_drafts_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(view_drafts_activity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_info) {
            return true;
        }
        if (id == R.id.action_submission) {
            startActivity(create_sub_activity);
            return true;
        }
        if (id == R.id.action_viewsubs) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
