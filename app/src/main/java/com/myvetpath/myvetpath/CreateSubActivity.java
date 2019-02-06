package com.myvetpath.myvetpath;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//This is for the "Create Submission screen"
public class CreateSubActivity extends AppCompatActivity {
/*
Submission sub = new Submission();
MyDBHandler db = getwritabledatabase();
sub.setTitle(stuff from app)
db.addSubmission(sub)
 */

    Intent add_pictures_activity;
    Intent view_subs_activity;
    ImageButton add_pictures_button;
    Button save_draft_button;
    Button submit_button;
    EditText title_et;
    MyDBHandler dbHandler;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreateSub);
        toolbar.setTitle(R.string.action_submission);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view_subs_activity = new Intent(this, ViewSubsActivity.class);

        dbHandler = new MyDBHandler(this);
        final Submission newSub = new Submission();

        //For displaying the current date
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        //+ simpleDateFormat.format(Calendar.getInstance().getTime());

        //initialize submission elements
        title_et = findViewById(R.id.sub_title);
        save_draft_button = findViewById(R.id.save_draft_btn);
        save_draft_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                newSub.setTitle(title_et.getText().toString());
                newSub.setStatusFlag(0);
                newSub.setDateOfCreation(Calendar.getInstance().getTime().getTime());
                //Display confirmation Toast
                String content = title_et.getText().toString() + " Saved";
                Toast testToast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
                testToast.show();
                dbHandler.addSubmission(newSub);
            }
        });
        /*TODO: Add fragment to check if user is sure before submitting*/
        submit_button = findViewById(R.id.submit_btn);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                newSub.setTitle(title_et.getText().toString());
                newSub.setStatusFlag(1);
                newSub.setDateOfCreation(Calendar.getInstance().getTime().getTime());
                //Display confirmation Toast
                String content = title_et.getText().toString() + " Submitted";
                Toast testToast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
                testToast.show();
                dbHandler.addSubmission(newSub);
                startActivity(view_subs_activity);
            }
        });

        //initialize the camera button where users can add pictures
        add_pictures_activity = new Intent(this, AddPicturesActivity.class);
        add_pictures_button = findViewById(R.id.addPicturesButton);
        add_pictures_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(add_pictures_activity);
            }
        });
    }

    /**
     * Hides the soft keyboard on screen
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
