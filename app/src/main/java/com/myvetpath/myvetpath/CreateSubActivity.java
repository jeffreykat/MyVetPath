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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.Types.NULL;

//This is for the "Create Submission screen"
public class CreateSubActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{
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

    ImageButton date_of_submission_button;
    Spinner sexSpinner;
    private String selectedSex;
    private boolean isEuthanized;
    private Date collectionDate;
    private Date birthDate;
    private Date deathDate;

    ImageButton date_of_birth_button;
    ImageButton date_of_death_button;

    static final int SAMPLE_COLLECTED_DATE = 0;
    static final int BIRTH_DATE = 1;
    static final int DEATH_DATE = 2;
    private int selectedCalendar;


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

        date_of_submission_button = (ImageButton) findViewById(R.id.CollectionDateBTTN);
        date_of_submission_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                selectedCalendar = SAMPLE_COLLECTED_DATE;
            }
        });

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

        view_subs_activity = new Intent(this, ViewSubsActivity.class);

        dbHandler = new MyDBHandler(this);
        final Submission newSub = new Submission();

        //For displaying the current date
//        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        //+ simpleDateFormat.format(Calendar.getInstance().getTime());

        //initialize submission elements
        title_et = findViewById(R.id.sub_title);
        save_draft_button = findViewById(R.id.save_draft_btn);
        save_draft_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDataInDB();
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
        /*TODO: Add fragment to check if user is sure before submitting*/
        submit_button = findViewById(R.id.submit_btn);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDataInDB();
                hideSoftKeyboard();
                long curDate = Calendar.getInstance().getTime().getTime();
                newSub.setCaseID(NULL);
                newSub.setTitle(title_et.getText().toString());
                newSub.setStatusFlag(1);
                newSub.setDateOfCreation(curDate);
                createDialog(newSub);
            }
        });
//
        view_subs_activity = new Intent(this, ViewSubsActivity.class);



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

    //This method is called whenever the user selects a date. It will change one of the three date objects depending on which calendar was changed
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(selectedCalendar == SAMPLE_COLLECTED_DATE){
            String currentDateString = "Collected On " + DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            TextView dateSelectedTV = (TextView) findViewById(R.id.CollectionDateTV);
            dateSelectedTV.setText(currentDateString);
            collectionDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        }else if (selectedCalendar == BIRTH_DATE){
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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

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

    //This method stores all the data into the database. Called whenever the user wants to save a submission
    //Todo: store everything into database. May need to make changes to this to accomodate differences between submit button and save as draft button. Will have to modify the newsub variable
    private void storeDataInDB(){
        int numberOfSamples;
        String submissionTitle = ((EditText) findViewById(R.id.sub_title)).getText().toString();
        String groupName = ((EditText) findViewById(R.id.group_name_ET)).getText().toString();
        String sampleLocation = ((EditText) findViewById(R.id.location_sample_ET)).getText().toString();

        //check if user entered an integer into the number of samples text field. We many want to add some input validation later
        if (isInteger(((EditText) findViewById(R.id.number_of_samples_ET)).getText().toString())){
            Log.d("s", "storeDataInDB: Number of samples: in if ");
            numberOfSamples = Integer.parseInt(((EditText) findViewById(R.id.number_of_samples_ET)).getText().toString());
        }else{
            Log.d("s", "storeDataInDB: Number of samples: in else ");
            numberOfSamples = 0; //for now set it to 0. May want to warn the user and stop the submission from happening later
        }
        Log.d("s", "storeDataInDB: before logging " );
        Log.d("s", "storeDataInDB: Number of samples: " + numberOfSamples);
        String sampleName = ((EditText) findViewById(R.id.name_of_samples_ET)).getText().toString();
        String sickElementName = ((EditText) findViewById(R.id.sick_element_name_ET)).getText().toString();
        String species = ((EditText) findViewById(R.id.species_ET)).getText().toString();
        String comment = ((EditText) findViewById(R.id.Comment_ET)).getText().toString();

        //The following data should have been collected elsewhere in the app:
        //deathDate
        //birthDate
        //collectionDate
        // Boolean isEuthanized = already collected in checkbox listener
        //selectedSex

        //Here are the rest of the data we need for the submissions:
        //Client ID - probably going to get this from login
        //Internal ID - created automatically using SQLite
        //CaseID
        //Master ID
        //Date of Creation
        //Submission Date
        //Report Complete Date
        //Status flag?
        //Sample ID - primary key in form of integer value. Generated with running total on the SQLite database
        //Internal ID - foreign key from Submission table
        //Sick element ID - running total
        //Internal ID for sick element table - foreign key from submission table

    }

}
