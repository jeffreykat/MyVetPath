package com.myvetpath.myvetpath;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.MyVetPathAPI;
import com.myvetpath.myvetpath.data.LocalRepository;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;
import com.myvetpath.myvetpath.data.UserTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.Types.NULL;

//This is for the "Create submission screen"
public class CreateSubActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    MyVetPathViewModel viewModel;

    Intent add_pictures_activity;
    Intent view_subs_activity;
    Intent add_samples_activity;

    SubmissionTable newSub;
    PatientTable newPatient;
    GroupTable newGroup = new GroupTable();
    UserTable newUser;
    ArrayList<SampleTable> samplesList = new ArrayList<SampleTable>(5);
    ArrayList<SampleTable> originalSamples = new ArrayList<>();
    ArrayList<PictureTable> picturesList = new ArrayList<PictureTable>(5);
    ArrayList<PictureTable> originalPictures = new ArrayList<>(5);

    String userName;
    long global_master_id;

    ImageButton add_pictures_button;
    Button save_draft_button;
    Button submit_button;
    Button add_samples_button;
    ImageButton date_of_birth_button;
    ImageButton date_of_death_button;
    CheckBox euthanizedCB;
    EditText title_et;
    EditText group_et;
    EditText comment_et;
    EditText sickElementName;
    EditText species;
    Spinner sexSpinner;

    private String selectedSex;
    private int isEuthanized = 0;
    private Date birthDate;
    private Date deathDate;
    static final int BIRTH_DATE = 1;
    static final int DEATH_DATE = 2;
    private int selectedCalendar;

    static final String LOG_TAG = "CreateSubActivity";

    private final int ADD_SAMPLES_REQUEST_CODE = 2;

    private String draftName = "";
    boolean draftExists = false;
    boolean subInserted = false;

    SharedPreferences preferences;

    OnSubmissionInserted inserted = new OnSubmissionInserted() {
        @Override
        public void onSubmissionInserted(long master_id) {
            global_master_id = master_id;
            Log.d(LOG_TAG, "global master id set: " + Long.toString(master_id));
            subInserted = true;
        }
    };

    public void createDialog(final SubmissionTable submission, final PatientTable patient, final GroupTable group){
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
                long internalID;
                if(draftExists){
                    internalID = submission.Master_ID;
                    viewModel.updateSubmission(submission);
                    viewModel.updatePatient(patient);
                    for (SampleTable tempSample : samplesList) {
                        viewModel.updateSample(tempSample);
                    }

                    for (PictureTable tempPicture : picturesList) {
                        if (tempPicture != null) {
                            Log.d(LOG_TAG, "onClick: current internal id is: " + Long.toString(tempPicture.Master_ID));
                            viewModel.updatePicture(tempPicture);
                        }
                    }
                } else {
                    submission.Group_ID = viewModel.insertGroup(group);
                    internalID = viewModel.insertSubmission(submission, inserted);
                    Log.d(LOG_TAG, "internal id from view model: " + Long.toString(internalID));

                    patient.Master_ID = internalID;
                    viewModel.insertPatient(patient);

                    for (SampleTable tempSample : samplesList) {
                        tempSample.Master_ID = internalID;
                        viewModel.insertSample(tempSample);
                    }

                    for (PictureTable tempPicture : picturesList) {
                        if (tempPicture != null) {
                            tempPicture.Master_ID = internalID;
                            Log.d(LOG_TAG, "onClick: current internal id is: " + Long.toString(tempPicture.Master_ID));
                            viewModel.insertPicture(tempPicture);
                        }
                    }
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

    public void setupNew(){
        newSub = new SubmissionTable();
        newPatient = new PatientTable();
        newGroup = new GroupTable();
        newUser = new UserTable();
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
        add_pictures_activity = new Intent(this, AddPicturesActivity.class);
        add_samples_activity = new Intent(this, AddSamplesActivity.class);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(CreateSubActivity.this);

        if(extras != null){
            if(extras.containsKey("draft")) {
                long internalID = extras.getLong("draft", 0);
                Log.d(LOG_TAG, "draft id: " + Long.toString(internalID));
                setDraftObservers(internalID);
                draftExists = true;
            }
            else{
                setupNew();
            }
        } else {
            setupNew();
        }

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

        sickElementName = findViewById(R.id.sick_element_name_ET);
        species = findViewById(R.id.species_ET);
        euthanizedCB = findViewById(R.id.EuthanizedCB);

        //initialize submission elements
        title_et = findViewById(R.id.sub_title);
        group_et = findViewById(R.id.group_name_ET);
        comment_et = findViewById(R.id.Comment_ET);
        save_draft_button = findViewById(R.id.save_draft_btn);
        save_draft_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if(loadSubmissionData(0, newSub, newPatient)) {
                    //Display confirmation Toast
                    String content = title_et.getText().toString() + " Saved";
                    Toast testToast = Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
                    testToast.show();
                    long intID;
                    if(draftExists){
                        intID = newSub.Master_ID;
                        viewModel.updateSubmission(newSub);
                        viewModel.updatePatient(newPatient);
                        for(SampleTable tempSample: originalSamples){
                            viewModel.deleteSample(tempSample);
                        }
                        for(PictureTable tempPicture: originalPictures){
                            if(tempPicture != null) {
                                viewModel.deletePicture(tempPicture);
                            }
                        }
                    } else{
                        intID = viewModel.insertSubmission(newSub, inserted);
                        draftName = newSub.Title;
                        newPatient.Master_ID = intID;
                        viewModel.insertPatient(newPatient);
                        draftExists = true;
                    }
                    for(SampleTable tempSample: samplesList){
                        tempSample.Master_ID = intID;
                        viewModel.insertSample(tempSample);
                    }

                    for(PictureTable tempPicture: picturesList){
                        if(tempPicture != null){
                            tempPicture.Master_ID = intID;
                            Log.d(LOG_TAG, "onClick: current internal id is: " + Long.toString(tempPicture.Master_ID));
                            viewModel.insertPicture(tempPicture);
                        }
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

                userName = preferences.getString(getString(R.string.username_preference_key), "");

                if(userName.equals("")){//if user isn't logged in, then make them login first
                    Toast.makeText(CreateSubActivity.this, "Please save draft and login first " + userName,
                            Toast.LENGTH_LONG).show();
                    Intent login_activity;
                    login_activity = new Intent(CreateSubActivity.this, LoginActivity.class);

                    startActivity(login_activity);
                    setUser();
                    return;
                }else{ // set clientID if user is logged in
                    setUser();
                }

                if(loadSubmissionData(1, newSub, newPatient)) {
                    createDialog(newSub, newPatient, newGroup);
                }
                else{
                    Toast testToast = Toast.makeText(getApplicationContext(), R.string.create_error, Toast.LENGTH_LONG);
                    testToast.show();
                }
            }
        });

        //initialize the camera button where users can add pictures
        PictureTable p = new PictureTable();
        Log.d("pass", "onCreate: Title of P: " + p.Title);

        add_pictures_button = findViewById(R.id.addPicturesButton);
        add_pictures_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                add_pictures_activity.putExtra("pictureList", picturesList);
                startActivityForResult(add_pictures_activity, 1);
            }
        });

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

    public void draftExists(){
        title_et.setText(newSub.Title, TextView.BufferType.EDITABLE);
        if(newSub.Group_ID != NULL) {
            group_et.setText(newGroup.GroupName, TextView.BufferType.EDITABLE);
        }
        sickElementName.setText(newPatient.PatientName, TextView.BufferType.EDITABLE);
        species.setText(newPatient.Species, TextView.BufferType.EDITABLE);
        if(newPatient.Sex.matches("Female")) {
            sexSpinner.setSelection(2);
        }
        if(newPatient.Sex.matches("Male")){
            sexSpinner.setSelection(1);
        }
        if(newPatient.Euthanized == 1) {
            euthanizedCB.setChecked(true);
        }
        Long bDateMS = newPatient.DateOfBirth;
        Long dDateMS = newPatient.DateOfDeath;
        Calendar c = Calendar.getInstance();
        if(bDateMS != 0) {
            c.setTimeInMillis(bDateMS);
            birthDate = c.getTime();
            selectedCalendar = BIRTH_DATE;
            showDateText(c);
        }
        if(dDateMS != 0) {
            c.setTimeInMillis(dDateMS);
            deathDate = c.getTime();
            selectedCalendar = DEATH_DATE;
            showDateText(c);
        }
        comment_et.setText(newSub.UserComment, TextView.BufferType.EDITABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("pass", "onActivityResult: back in onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                picturesList = (ArrayList<PictureTable>) data.getSerializableExtra("pictureResults");
                Log.d("pass", "onActivityResult: result after returning to createsub is " + picturesList.get(0).Title + " longitude: " + picturesList.get(0).Longitude);
            }
        }else if(requestCode == ADD_SAMPLES_REQUEST_CODE){
            if(resultCode == RESULT_OK ){
                samplesList = (ArrayList<SampleTable>) data.getSerializableExtra("sampleResults");
            }
        }
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
            showDateText(c);
            birthDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        }else if (selectedCalendar == DEATH_DATE){
            showDateText(c);
            deathDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        }
    }

    public void showDateText(Calendar calendar){
        if(selectedCalendar == BIRTH_DATE){
            String currentDateString = "Animal Born On " + DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            TextView dateSelectedTV = (TextView) findViewById(R.id.Birth_Date_Message_TV);
            dateSelectedTV.setText(currentDateString);
        } else if(selectedCalendar == DEATH_DATE){
            String currentDateString = "Animal Died On " + DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            TextView dateSelectedTV = (TextView) findViewById(R.id.Death_Date_Message_TV);
            dateSelectedTV.setText(currentDateString);
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
        int checked = 0;
        if(euthanizedCB.isChecked()){
            checked = 1;
        }
        isEuthanized = checked;
    }

    //This method stores all the data in a submission. Called whenever the user wants to save or submit a submission
    private boolean loadSubmissionData(int status, SubmissionTable newSub, PatientTable newPatient){
        final long curDate = Calendar.getInstance().getTime().getTime();

        //The following data should have been collected elsewhere in the app:
        //deathDate
        //birthDate
        //collectionDate

        //Here are the rest of the data we need for the submissions:
        //SubmissionDate
        //ReportComplete Date
        //Sample_ID - primary key in form of integer value. Generated with running total on the SQLite database
        //Sick element ID - running total
        //Internal ID for sick element table - foreign key from submission table

        if(title_et.getText().toString().isEmpty() || comment_et.getText().toString().isEmpty() || species.getText().toString().isEmpty()){
            return false;
        }

        newSub.Case_ID = NULL;
        newSub.Title = title_et.getText().toString();
        newSub.StatusFlag = status;
        newSub.DateOfCreation = curDate;
        newSub.UserComment = comment_et.getText().toString();

        newGroup.GroupName = group_et.getText().toString();
        newGroup.DateOfCreation = curDate;

        newPatient.PatientName = sickElementName.getText().toString();
        newPatient.Sex = selectedSex;
        newPatient.Euthanized = isEuthanized;
        newPatient.Species = species.getText().toString();

        if(birthDate != null) {
            newPatient.DateOfBirth = birthDate.getTime();
        } else {
            newPatient.DateOfBirth = 0;
        }
        if(deathDate != null) {
            newPatient.DateOfDeath = deathDate.getTime();
        } else {
            newPatient.DateOfDeath = 0;
        }

        return true;
    }

    void setUser(){
        viewModel.getUserByUsername(preferences.getString(getString(R.string.username_preference_key), "")).observe(this, new Observer<UserTable>() {
            @Override
            public void onChanged(@Nullable UserTable userTable) {
                if(userTable != null) {
                    newUser = userTable;
                    Log.d(LOG_TAG, "User: " + newUser.Username);
                    newSub.User_ID = newUser.User_ID;
                } else{
                    Log.d(LOG_TAG, "User error");
                }
            }
        });
    }

    void setDraftObservers(long internalID){
        Log.d(LOG_TAG, "Setting observers");
        viewModel.setSubmissionInputID(internalID);
        viewModel.getSubmissionByID().observe(this, new Observer<SubmissionTable>() {
            @Override
            public void onChanged(@Nullable SubmissionTable submissionTable) {
                if(submissionTable != null) {
                    newSub = submissionTable;
                    Log.d(LOG_TAG, "submission observed: " + newSub.Title);
                    draftName = newSub.Title;
                } else {
                    Log.d(LOG_TAG, "newSub is null");
                }
            }
        });
        viewModel.getGroupByName(newGroup.GroupName).observe(this, new Observer<GroupTable>() {
            @Override
            public void onChanged(@Nullable GroupTable groupTable) {
                if(groupTable != null){
                    newGroup = groupTable;
                    Log.d(LOG_TAG, "group observed: " + newGroup.GroupName);
                } else {
                    Log.d(LOG_TAG, "group not saved");
                }
            }
        });
        viewModel.getPatientByID(internalID).observe(this, new Observer<PatientTable>() {
            @Override
            public void onChanged(@Nullable PatientTable patientTable) {
                if(patientTable != null){
                    newPatient = patientTable;
                    Log.d(LOG_TAG, "patient observed: " + newPatient.PatientName);
                } else {
                    Log.d(LOG_TAG, "newPatient is null");
                }
            }
        });
        viewModel.getSamplesByID(internalID).observe(this, new Observer<List<SampleTable>>() {
            @Override
            public void onChanged(@Nullable List<SampleTable> sampleTables) {
                if(sampleTables != null){
                    originalSamples.addAll(sampleTables);
                    samplesList.addAll(sampleTables);
                    Log.d(LOG_TAG, "samples added");
                } else {
                    Log.d(LOG_TAG, "samples null");
                }
            }
        });
        viewModel.getPicturesByID(internalID).observe(this, new Observer<List<PictureTable>>() {
            @Override
            public void onChanged(@Nullable List<PictureTable> pictureTables) {
                if(pictureTables != null){
                    originalPictures.addAll(pictureTables);
                    picturesList.addAll(pictureTables);
                    Log.d(LOG_TAG, "pictures added");
                } else {
                    Log.d(LOG_TAG, "pictures null");
                }
                draftExists();
            }
        });
    }

    //This will use the API (which we don't have at the moment) to send the submission to the server. Call this function after clicking on "submit"
    private void sendToServer(){
        String MyVetPath_Base_Url = getString(R.string.MVP_Base_API_URL);
        //build the retrofit that will make the query
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.MVP_Base_API_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyVetPathAPI myVetPathAPI = retrofit.create(MyVetPathAPI.class);

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");


        //Send the Submission
        Call<ResponseBody> call = myVetPathAPI.submission(headerMap, "submission", newSub.Group_ID, newSub.User_ID,
                newSub.Title, newSub.DateOfCreation, newSub.StatusFlag, newSub.Submitted, newSub.ReportComplete,
                newSub.UserComment, "json");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d(TAG, "onResponse: Server Response: " + response.toString());

                try{
                    String json = response.body().string();
//                            Log.d(TAG, "onResponse: json: " + json);
                    JSONObject data = null;
                    data = new JSONObject(json); //this is the response data that comes back.
                    Log.d("k", "onResponse: data: " + data.optString("json"));

                }catch (JSONException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                }catch (IOException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                Toast.makeText(CreateSubActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        //Send the Pictures:
        for(PictureTable picture : picturesList){
            call = myVetPathAPI.picture(headerMap, "picture", picture.Image_ID, picture.Master_ID, picture.ImagePath, picture.Title, picture.Latitude, picture.Longitude, picture.DateTaken, "json");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d(TAG, "onResponse: Server Response: " + response.toString());

                    try{
                        String json = response.body().string();
//                            Log.d(TAG, "onResponse: json: " + json);
                        JSONObject data = null;
                        data = new JSONObject(json); //this is the response data that comes back.
                        Log.d("k", "onResponse: data: " + data.optString("json"));

                    }catch (JSONException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                    }catch (IOException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                    Toast.makeText(CreateSubActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Send the Samples:
        for(SampleTable sample : samplesList){
            call = myVetPathAPI.sample(headerMap, "sample", sample.Sample_ID, sample.Master_ID, sample.LocationOfSample, sample.NumberOfSample, sample.NameOfSample, "json");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d(TAG, "onResponse: Server Response: " + response.toString());

                    try{
                        String json = response.body().string();
//                            Log.d(TAG, "onResponse: json: " + json);
                        JSONObject data = null;
                        data = new JSONObject(json); //this is the response data that comes back.
                        Log.d("k", "onResponse: data: " + data.optString("json"));

                    }catch (JSONException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                    }catch (IOException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                    Toast.makeText(CreateSubActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Send the patient
        call = myVetPathAPI.patient(headerMap, "patient", newPatient.Patient_ID, newPatient.Master_ID, newPatient.PatientName, newPatient.Species, newPatient.Sex, newPatient.Euthanized, newPatient.DateOfBirth, newPatient.DateOfDeath, "json");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d(TAG, "onResponse: Server Response: " + response.toString());

                try{
                    String json = response.body().string();
//                            Log.d(TAG, "onResponse: json: " + json);
                    JSONObject data = null;
                    data = new JSONObject(json); //this is the response data that comes back.
                    Log.d("k", "onResponse: data: " + data.optString("json"));

                }catch (JSONException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                }catch (IOException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                Toast.makeText(CreateSubActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
