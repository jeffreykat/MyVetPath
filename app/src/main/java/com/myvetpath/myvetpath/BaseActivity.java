package com.myvetpath.myvetpath;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/*
    This is the class that implements the toolbar menu options. It also takes the appropriate action based on what was clicked,
    which is usually navigating to a different activity. If you want an activity to use these options, then have
    that activity inherit from this class (and make sure that activity doesn't overwrite any of the methods here).
*/
public class BaseActivity extends AppCompatActivity {
    Intent create_sub_activity;
    Intent view_subs_activity;
    Intent settings_activity;
    Intent instructions_activity;

    @Override
    //Called when the activity is first starting
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Initialize the intents
        create_sub_activity = new Intent(this, CreateSubActivity.class);
        view_subs_activity = new Intent(this, ViewSubsActivity.class);
        settings_activity = new Intent(this, SettingsActivity.class);
        instructions_activity = new Intent(this, InstructionsActivity.class);

    }

    @Override
    //Purpose: Inflates and populates a menu resource. it is called the first time the options menu is displayed
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //Purpose: Process which item in the options menu was selected by the user and then take the appropriate action
    //         This function is called whenever the user clicks on an item in the options menu.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){ //Check to see which menu option item was selected by the user
            case R.id.action_info:
                startActivity(instructions_activity);
                break;
            case R.id.action_settings: //create submission
                startActivity(settings_activity);
                break;
            case R.id.action_submission:
                startActivity(create_sub_activity);
                break;
            case R.id.action_viewsubs:
                startActivity(view_subs_activity);
                break;
            default: //This should happen if the user clicks the back button
                //Having the below code makes it so clicking the back code navigates the user to the previous activity instead of the parent activity (which is usually the main activity)
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
