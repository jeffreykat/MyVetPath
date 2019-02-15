package com.myvetpath.myvetpath;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.Types.NULL;

//This is for the "Create Submission screen"
public class CreateSubActivity extends BaseActivity {

    Intent add_pictures_activity;
    Intent view_subs_activity;
    ImageButton add_pictures_button;
    Button save_draft_button;
    Button submit_button;
    EditText title_et;
    MyDBHandler dbHandler;

    public void createDialog(final Submission submission){
        AlertDialog.Builder dialog = new AlertDialog.Builder(CreateSubActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.action_submit_conformation);
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Display confirmation Toast
                String content = title_et.getText().toString() + " Submitted";
                Toast testToast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
                testToast.show();
                dbHandler.addSubmission(submission);
                startActivity(view_subs_activity);
            }
        })
                .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Close
                    }
                });
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreateSub);
        setMenuOptionItemToRemove(this);
        toolbar.setTitle(R.string.action_submission);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view_subs_activity = new Intent(this, ViewSubsActivity.class);

        dbHandler = new MyDBHandler(this);
        final Submission newSub = new Submission();

        //For displaying the current date
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        //initialize submission elements
        title_et = findViewById(R.id.sub_title);
        save_draft_button = findViewById(R.id.save_draft_btn);
        save_draft_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                long curDate = Calendar.getInstance().getTime().getTime();
                newSub.setCaseID(NULL);
                newSub.setTitle(title_et.getText().toString());
                newSub.setStatusFlag(0);
                newSub.setDateOfCreation(curDate);
                //Display confirmation Toast
                String content = title_et.getText().toString() + " Saved";
                Toast testToast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
                testToast.show();
                dbHandler.addSubmission(newSub);
            }
        });
        submit_button = findViewById(R.id.submit_btn);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                long curDate = Calendar.getInstance().getTime().getTime();
                newSub.setCaseID(NULL);
                newSub.setTitle(title_et.getText().toString());
                newSub.setStatusFlag(1);
                newSub.setDateOfCreation(curDate);
                createDialog(newSub);
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


}
