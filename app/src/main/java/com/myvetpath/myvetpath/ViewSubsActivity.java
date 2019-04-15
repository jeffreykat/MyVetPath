package com.myvetpath.myvetpath;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.myvetpath.myvetpath.adapters.SubmissionAdapter;
import com.myvetpath.myvetpath.data.CategoryItem;
import com.myvetpath.myvetpath.data.MyVetPathAPI;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.ReplyTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;
import com.myvetpath.myvetpath.data.UserTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


public class ViewSubsActivity extends BaseActivity implements SubmissionAdapter.OnSubmissionClickListener {

    private MyVetPathViewModel viewModel;

    Intent create_sub_activity;
    Intent sub_details_activity;
    boolean subTableExists;

    private RecyclerView mRecyclerView;
    private SubmissionAdapter mAdapter;
    private List<SubmissionTable> submissions;

    private EntryViewModel mEntryViewModel;
    private List<CategoryItem> mCategoryItems;
    private long masterID;

    OnSubmissionInserted inserted = new OnSubmissionInserted() {
        @Override
        public void onSubmissionInserted(long master_id) {
            masterID = master_id;
            Log.d("submission", "global master id set: " + Long.toString(master_id));
        }
    };

    public void createDeleteDialog(final SubmissionTable submission){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewSubsActivity.this);
        dialog.setCancelable(true);
        String title = getString(R.string.action_delete_confirmation_prompt_first_part)
                + submission.Title
                + getString(R.string.action_delete_confirmation_second_part);
        dialog.setTitle(title);
        dialog.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ViewSubsActivity.this, getString(R.string.deleted_message) + submission.Title,
                        Toast.LENGTH_LONG).show();
                viewModel.deleteSubmission(submission);
            }
        }).setNegativeButton(R.string.action_no, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public void onSubmissionClick(SubmissionTable sub) {
        sub_details_activity.putExtra("submission", sub);
        Log.d("ViewSubsActivity", "Master_ID in list: "+ Long.toString(sub.Master_ID));
        startActivity(sub_details_activity);
    }

    @Override
    public void onSubmissionLongClick(SubmissionTable sub) {
        createDeleteDialog(sub);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setMenuOptionItemToRemove(this);
        toolbar.setTitle("Submissions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        create_sub_activity = new Intent(this, CreateSubActivity.class);
        sub_details_activity = new Intent(this, SubDetailsActivity.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.subsRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SubmissionAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        viewModel.getSubmissions().observe(this, new Observer<List<SubmissionTable>>() {
            @Override
            public void onChanged(@Nullable List<SubmissionTable> submissionTables) {
                mAdapter.updateSubs(submissionTables);
                //sendToServer(submissionTables); //This will auto send data (that hasn't been sent to the server yet) to the server. This is commented out right now because the API isn't ready
            }
        });
        masterID = 0;

    }



    //This function will automatically send data to the server, provided that the data hasn't already been sent before. The parameter is the lsit of submissions that are stored in the local database
    public void sendToServer(ArrayList<SubmissionTable> submissions){
        String MyVetPath_Base_Url = getString(R.string.MVP_Base_API_URL);
        //build the retrofit that will make the query
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.MVP_Base_API_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MyVetPathAPI myVetPathAPI = retrofit.create(MyVetPathAPI.class);

        final HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");


       for(final SubmissionTable newSub : submissions) {
            if(newSub.Submitted == 0) { //only send to server if the submission hasn't been sent yet
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
                            //update submission in database
                            newSub.Submitted = 1;
                            viewModel.updateSubmission(newSub);
                        }catch (JSONException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                        }catch (IOException e){
//                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                        Toast.makeText(ViewSubsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


                viewModel.getPicturesByID(newSub.Master_ID).observe(this, new Observer<List<PictureTable>>() {
                    @Override
                    public void onChanged(@Nullable List<PictureTable> pictureTables) {
                        //Send the Pictures:
                        MyVetPathAPI myVetPathAPI = retrofit.create(MyVetPathAPI.class);
                        for(PictureTable picture : pictureTables){
                            Call<ResponseBody> call = myVetPathAPI.picture(headerMap, "picture", picture.Image_ID, picture.Master_ID, picture.ImagePath, picture.Title, picture.Latitude, picture.Longitude, picture.DateTaken, "json");

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
                                    Toast.makeText(ViewSubsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });


                viewModel.getSamplesByID(newSub.Master_ID).observe(this, new Observer<List<SampleTable>>() {
                    @Override
                    public void onChanged(@Nullable List<SampleTable> sampleTables) {
                        //Send the Samples:
                        MyVetPathAPI myVetPathAPI = retrofit.create(MyVetPathAPI.class);
                        for(SampleTable sample : sampleTables){
                            Call<ResponseBody> call = myVetPathAPI.sample(headerMap, "sample", sample.Sample_ID, sample.Master_ID, sample.LocationOfSample, sample.NumberOfSample, sample.NameOfSample, "json");

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
                                    Toast.makeText(ViewSubsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });



                //Send the patient
                LiveData<PatientTable> newPatient = viewModel.getPatientByID(newSub.Master_ID);
                call = myVetPathAPI.patient(headerMap, "patient",  newPatient.getValue().Patient_ID, newPatient.getValue().Master_ID, newPatient.getValue().PatientName, newPatient.getValue().Species,
                        newPatient.getValue().Sex, newPatient.getValue().Euthanized, newPatient.getValue().DateOfBirth, newPatient.getValue().DateOfDeath, "json");
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
                        Toast.makeText(ViewSubsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


            }

       }

    }


    //This function syncs the data by inserting the results from the query into the database
    public void sync(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ViewSubsActivity.this);
        String userName = preferences.getString(getString(R.string.username_preference_key), "");
        if(userName.equals("")){//if user isn't logged in, then make them login first
            Toast.makeText(ViewSubsActivity.this, "Please login first " + userName,
                    Toast.LENGTH_LONG).show();
            Intent login_activity;
            login_activity = new Intent(ViewSubsActivity.this, LoginActivity.class);
            startActivity(login_activity);
            return;
        }
        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);
        mCategoryItems = new ArrayList<>();
        viewModel.getUserByUsername(preferences.getString(getString(R.string.username_preference_key), "")).observe(this, new Observer<UserTable>() {
            @Override
            public void onChanged(@Nullable UserTable userTable) {
                if(userTable != null) {
                    setObserverCategory(userTable);

                } else{
                    Log.d("Error", "User error");
                }
            }
        });



        mEntryViewModel.loadCategoryItems("submissions", null); //query
        mEntryViewModel.loadCategoryItems("picture", null); //query
        mEntryViewModel.loadCategoryItems("patient", null); //query
        mEntryViewModel.loadCategoryItems("sample", null); //query
        mEntryViewModel.loadCategoryItems("reply", null); //query
        mEntryViewModel.loadCategoryItems("report", null); //query
    }

    //This is a helper function that does the actual insertions
    private void setObserverCategory(final UserTable user) {

        //insert submissions
        mEntryViewModel.getSubmission().observe(this, new Observer<List<SubmissionTable>>() {
            @Override
            public void onChanged(@Nullable List<SubmissionTable> submissionItems) {
                if(submissionItems != null) {
                    for (SubmissionTable item : submissionItems) {
                        if(user.User_ID == item.User_ID){ //check the logged in user to see if they are allowed to have the submission before inserting
                            viewModel.insertSubmission(item, inserted);
                        }
                    }
                }
            }
        });

        //insert pictures
        mEntryViewModel.getPicture().observe(this, new Observer<List<PictureTable>>() {
            @Override
            public void onChanged(@Nullable List<PictureTable> picturesList) {
                if(picturesList != null) {
                    for (PictureTable item : picturesList) {
                        item.Master_ID = masterID;
                        viewModel.insertPicture(item);
                    }
                }
            }
        });

        //insert patients
        mEntryViewModel.getPatient().observe(this, new Observer<List<PatientTable>>() {
            @Override
            public void onChanged(@Nullable List<PatientTable> patientItems) {
                if(patientItems != null) {
                    for (PatientTable item : patientItems) {
                        item.Master_ID = masterID;
                        viewModel.insertPatient(item);
                    }
                }
            }
        });

        //insert samples
        mEntryViewModel.getSample().observe(this, new Observer<List<SampleTable>>() {
            @Override
            public void onChanged(@Nullable List<SampleTable> sampleItems) {
                if(sampleItems != null) {
                    for (SampleTable item : sampleItems) {
                        item.Master_ID = masterID;
                        viewModel.insertSample(item);
                    }
                }
            }
        });

        //insert replies
        mEntryViewModel.getReply().observe(this, new Observer<List<ReplyTable>>() {
            @Override
            public void onChanged(@Nullable List<ReplyTable> repliesList) {
                if(repliesList != null) {
                    for (ReplyTable item : repliesList) {
                        item.Master_ID = masterID;
                        viewModel.insertReply(item);
                    }
                }
            }
        });
    }

    @Override
    //Purpose: Process which item in the options menu was selected by the user and then take the appropriate action
    //         This function is called whenever the user clicks on an item in the options menu.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){ //Check to see which menu option item was selected by the user
            case R.id.action_home:
                startActivity(home_activity);
                break;
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
            case R.id.action_sync:
          //      sync(); clicking on "Sync" should sync the data. This line is commented out because the API isn't ready
                return true;
//                break;
            default: //This should happen if the user clicks the back button
                //Having the below code makes it so clicking the back code navigates the user to the previous activity instead of the parent activity (which is usually the main activity)
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
