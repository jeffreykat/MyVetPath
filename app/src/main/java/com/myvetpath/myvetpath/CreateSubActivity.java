package com.myvetpath.myvetpath;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.Types.NULL;

//This is for the "Create Submission screen" test
public class CreateSubActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{
/*
Submission sub = new Submission();
MyDBHandler db = getwritabledatabase();
sub.setTitle(stuff from app)
db.addSubmission(sub)
 */

    Intent add_pictures_activity;
    Intent view_subs_activity;
    Intent add_samples_activity;
    ImageButton add_pictures_button;
    Button save_draft_button;
    Button submit_button;
    Button add_samples_button;
    EditText title_et;
    EditText group_et;
    EditText comment_et;
    MyDBHandler dbHandler;

    ImageButton date_of_submission_button;
    Spinner sexSpinner;
    private String selectedSex;
    private boolean isEuthanized;
    private Date birthDate;
    private Date deathDate;

    ImageButton date_of_birth_button;
    ImageButton date_of_death_button;

    static final int BIRTH_DATE = 1;
    static final int DEATH_DATE = 2;
    private int selectedCalendar;

    static final String LOG_TAG = "CreateSubActivity";

    private final int ADD_SAMPLES_REQUEST_CODE = 2;
    private ArrayList<Sample> samplesList;

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
                long internalID = dbHandler.addSubmission(submission);

                for(Sample tempSample: samplesList){
                    tempSample.setSamplelID(Math.toIntExact(internalID));
                    dbHandler.addSample(tempSample);
                }

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
        //TODO: Fix Activity Lifecycle so up button restarts main activity
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        samplesList = new ArrayList<Sample>();

        view_subs_activity = new Intent(this, ViewSubsActivity.class);

        dbHandler = new MyDBHandler(this);
        final Submission newSub = new Submission();


        date_of_birth_button = (ImageButton) findViewById(R.id.BirthDateBTTN);
        date_of_birth_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                selectedCalendar = BIRTH_DATE;
            }
        });

        date_of_death_button = (ImageButton) findViewById(R.id.DeathDateBTTN);
        date_of_death_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                selectedCalendar = DEATH_DATE;
            }
        });

        sexSpinner = findViewById(R.id.SexSp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexString, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);
        sexSpinner.setOnItemSelectedListener(this);

        //initialize the camera button where users can add pictures
        add_pictures_activity = new Intent(this, AddPicturesActivity.class);
        add_pictures_button = findViewById(R.id.addPicturesButton);
        add_pictures_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(add_pictures_activity);
            }
        });

        //initialize submission elements
        title_et = findViewById(R.id.sub_title);
        group_et = findViewById(R.id.group_name_ET);
        comment_et = findViewById(R.id.Comment_ET);
        save_draft_button = findViewById(R.id.save_draft_btn);
        save_draft_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if(loadSubmissionData(0, newSub)) {
                    //Display confirmation Toast
                    String content = title_et.getText().toString() + " Saved";
                    Toast testToast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
                    testToast.show();
                    if (dbHandler.findSubmissionTitle(newSub.getTitle()) != null) {
                        dbHandler.updateSubmission(newSub);
                    } else {
                        dbHandler.addSubmission(newSub);
                    }
                }
                else {
                    Toast testToast = Toast.makeText(getApplicationContext(), R.string.create_error, Toast.LENGTH_LONG);
                    testToast.show();
                }
            }
        });
        submit_button = findViewById(R.id.submit_btn);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if(loadSubmissionData(1, newSub)) {
                    createDialog(newSub);
                }
                else{
                    Toast testToast = Toast.makeText(getApplicationContext(), R.string.create_error, Toast.LENGTH_LONG);
                    testToast.show();
                }
            }
        });

        add_samples_activity = new Intent(this, AddSamplesActivity.class);
        add_samples_button = findViewById(R.id.add_sample_bttn);
        add_samples_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                add_samples_activity.putExtra("samplesList", samplesList);
                startActivityForResult(add_samples_activity, ADD_SAMPLES_REQUEST_CODE);
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

    //This method is called whenever the user selects a date. It will change one of the three date objects depending on which calendar was changed
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (selectedCalendar == BIRTH_DATE){
            String currentDateString = "Animal Born On " + DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            TextView dateSelectedTV = (TextView) findViewById(R.id.Birth_Date_Message_TV);
            dateSelectedTV.setText(currentDateString);
            birthDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        }else if (selectedCalendar == DEATH_DATE){
            String currentDateString = "Animal Died On " + DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            TextView dateSelectedTV = (TextView) findViewById(R.id.Death_Date_Message_TV);
            dateSelectedTV.setText(currentDateString);
            deathDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        }
    }

    //The selectedSex value is set whenever the user changes the sex in the spinner menu.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedSex = adapterView.getItemAtPosition(i).toString();
    }

    //This function is needed to implement the spinner interface, but it will probably never be called
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    //This is called whenever the euthanized checkbox is clicked. Changes the data that will be stored in teh database
    public void onCheckboxClicked(View view){
        //Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        //Check which checkbox was clicked

        switch(view.getId()){
            case R.id.EuthanizedCB:
                isEuthanized = checked;
                break;
        }
    }

    // Purpose: check if string is an integer. This will be used to validate that the user entered an integer into a numeric field
    private boolean isInteger(String str) {
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    //This method stores all the data in a Submission. Called whenever the user wants to save or submit a submission
    //Todo: Add checks for empty inputs
    private boolean loadSubmissionData(int status, Submission newSub){
        long curDate = Calendar.getInstance().getTime().getTime();


        Log.d("s", "storeDataInDB: before logging " );
//        Log.d("s", "storeDataInDB: Number of samples: " + numberOfSamples);
        String sickElementName = ((EditText) findViewById(R.id.sick_element_name_ET)).getText().toString();
        String species = ((EditText) findViewById(R.id.species_ET)).getText().toString();

        //The following data should have been collected elsewhere in the app:
        //deathDate
        //birthDate
        //collectionDate
        // Boolean isEuthanized = already collected in checkbox listener
        //selectedSex

        //Here are the rest of the data we need for the submissions:
        //Client ID - probably going to get this from login
        //Submission Date
        //Report Complete Date
        //Sample ID - primary key in form of integer value. Generated with running total on the SQLite database
        //Internal ID - foreign key from Submission table
        //Sick element ID - running total
        //Internal ID for sick element table - foreign key from submission table

        if(title_et.getText().toString().isEmpty() || comment_et.getText().toString().isEmpty()){
            return false;
        }

        newSub.setCaseID(NULL);
        newSub.setMasterID(NULL);
        newSub.setTitle(title_et.getText().toString());
        newSub.setGroup(group_et.getText().toString());
        newSub.setStatusFlag(status);
        newSub.setDateOfCreation(curDate);
        newSub.setComment(comment_et.getText().toString());

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("pass", "onActivityResult: back in onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_SAMPLES_REQUEST_CODE){
            if(resultCode == RESULT_OK ){
                samplesList = (ArrayList<Sample>) data.getSerializableExtra("results");
            }
        }
    }

}
