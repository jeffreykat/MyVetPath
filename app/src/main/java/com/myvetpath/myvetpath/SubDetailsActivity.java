package com.myvetpath.myvetpath;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.MyVetPathAPI;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.ReplyTable;
import com.myvetpath.myvetpath.data.ReportTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;
import com.myvetpath.myvetpath.data.UserTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.Types.NULL;

public class SubDetailsActivity extends BaseActivity implements AddReplyCustomDialog.OnInputListener {

    @Override
    public void sendInput(String input) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SubDetailsActivity.this);
        String userName = preferences.getString(getString(R.string.username_preference_key), "");

        if(userName.equals("")){//if user isn't logged in, then make them login first
            Toast.makeText(SubDetailsActivity.this, "Please login first " + userName,
                    Toast.LENGTH_LONG).show();
            Intent login_activity;
            login_activity = new Intent(SubDetailsActivity.this, LoginActivity.class);
            startActivity(login_activity);
            return;
        }


        //Get current date, might want to change date to when it gets to server
        long curDate = Calendar.getInstance().getTime().getTime();
        calendar.setTimeInMillis(curDate);
        String currentDate = simpleDateFormat.format(calendar.getTime());

        //Create temporary reply and insert it into database
        final ReplyTable tempReply = new ReplyTable();
        tempReply.ContentsOfMessage = input;
        tempReply.DateOfMessage = curDate;
        tempReply.Master_ID = internalId;
        tempReply.Receiver_ID = 0; //probably won't need this

        viewModel.getUserByUsername(preferences.getString(getString(R.string.username_preference_key), "")).observe(this, new Observer<UserTable>() {
            @Override
            public void onChanged(@Nullable UserTable userTable) {
                if(userTable != null) {
                    tempReply.Sender_ID = userTable.User_ID;

                } else{
                    Log.d(LOG_TAG, "User error");
                }
            }
        });
        viewModel.insertReply(tempReply);



        //The block of code below that is commented out will be used to send the reply to the server.
        //The code is commented out because the API isn't ready

//        //Send the reply to the server
//        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(getString(R.string.MVP_Base_API_URL))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        final MyVetPathAPI myVetPathAPI = retrofit.create(MyVetPathAPI.class);
//
//        final HashMap<String, String> headerMap = new HashMap<String, String>();
//        headerMap.put("Content-Type", "application/json");
//
//
//        LiveData<PatientTable> newPatient = viewModel.getPatientByID(internalId);
//        Call<ResponseBody> call = myVetPathAPI.reply(headerMap, "reply", tempReply.Reply_ID, tempReply.Master_ID,
//                tempReply.Sender_ID, tempReply.Receiver_ID, tempReply.ContentsOfMessage, tempReply.DateOfMessage, "json");
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////                        Log.d(TAG, "onResponse: Server Response: " + response.toString());
//
//                try{
//                    String json = response.body().string();
////                            Log.d(TAG, "onResponse: json: " + json);
//                    JSONObject data = null;
//                    data = new JSONObject(json); //this is the response data that comes back.
//                    Log.d("k", "onResponse: data: " + data.optString("json"));
//
//                }catch (JSONException e){
////                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
//                }catch (IOException e){
////                            Log.e(TAG, "onResponse: JSONException: " + e.getMessage() );
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
////                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
//                Toast.makeText(SubDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    MyVetPathViewModel viewModel;

    final String LOG_TAG = "SubDetailsActivity";

    Intent create_sub_activity;
    SubmissionTable currentSub = new SubmissionTable();
    PatientTable currentPatient = new PatientTable();
    GroupTable currentGroup = new GroupTable();
    ReportTable currentReport = new ReportTable();
    ArrayList<PictureTable> pictures = new ArrayList<>(5);
    ArrayList<SampleTable> samples = new ArrayList<>(1);
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private TextView mSamplesTV;
    private ImageButton[] images;
    private EditText mReportReview;
    private CheckBox mReportCheck;
    private Button add_replies_BTTN;
    private TextView mRepliesTV;
    private TextView mReportTV;
    private String mReplyInput;
    private String mReplies;
    private ArrayList<ReplyTable> mRepliesList = new ArrayList<>(1);

    String group = new String();
    boolean reportExists = false;
    String reportText;
    private long internalId;

    public void createReportDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.report_dialog, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(SubDetailsActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.report_check);
        mReportReview = view.findViewById(R.id.report_dialog_et);
        mReportCheck = view.findViewById(R.id.report_dialog_check);
        dialog.setView(view);
        dialog.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reportText = mReportReview.getText().toString();
                boolean closed = mReportCheck.isChecked();
                currentReport.SubmissionReview = reportText;
                currentReport.ReportDate = calendar.getTimeInMillis();
                if(closed){
                    currentReport.DateClosed = calendar.getTimeInMillis();
                }
                if(reportExists){
                    viewModel.updateReport(currentReport);
                } else {
                    currentReport.Master_ID = internalId;
                    viewModel.insertReport(currentReport);
                }
                mReportTV.setText(reportText);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close
            }
        });

        AlertDialog ad = dialog.create();
        ad.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        currentSub = (SubmissionTable) getIntent().getSerializableExtra("submission");

        String title = currentSub.Title;

        calendar.setTimeInMillis(currentSub.DateOfCreation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReportTV = findViewById(R.id.subReportTV);

        FloatingActionButton fab = findViewById(R.id.details_report_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "clicked report button");
                createReportDialog();
            }
        });

        create_sub_activity = new Intent(this, CreateSubActivity.class);

        internalId = currentSub.Master_ID;
        setObservers(internalId);
        Log.d(LOG_TAG, "Master_ID in details: " + Long.toString(internalId));

        Log.d(LOG_TAG, "Patient name 3: " + currentPatient.PatientName);

        calendar.setTimeInMillis(currentSub.DateOfCreation);
        String comment = currentSub.UserComment;

        TextView titleText = findViewById(R.id.subTitle);
        titleText.setText(title);
        TextView dateText = findViewById(R.id.subDate);
        dateText.setText(simpleDateFormat.format(calendar.getTime()));
        TextView caseIDText = findViewById(R.id.subCaseID);
        if(currentSub.Case_ID == NULL){
            caseIDText.setText(R.string.pending);
        } else {
            caseIDText.setText(String.valueOf(currentSub.Case_ID));
        }
        if(!group.isEmpty()) {
            TextView groupText = findViewById(R.id.subGroupName);
            groupText.setText("Group: " + group);
        }
        TextView commentText = findViewById(R.id.subComment);
        commentText.setText(comment);


        //set images
        images = new ImageButton[]{findViewById(R.id.first_ImageDetails_bttn), findViewById(R.id.second_ImageDetails_bttn),
                findViewById(R.id.third_ImageDetails_bttn), findViewById(R.id.fourth_ImageDetails_bttn),
                findViewById(R.id.fifth_ImageDetails_bttn)};

        //Set up Replies
        mRepliesTV = findViewById(R.id.subRepliesTV);
        add_replies_BTTN = findViewById(R.id.add_reply_BTTN);
        mReplies = "";


        mRepliesTV.setText(mReplies);

        add_replies_BTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReplyCustomDialog dialog = new AddReplyCustomDialog();
                dialog.show(getFragmentManager(), "AddReplyCustomDialog");


            }
        });

    }

    public void setObservers(final long internalId){
        viewModel.getPatientByID(internalId).observe(this, new Observer<PatientTable>() {
            @Override
            public void onChanged(@Nullable PatientTable patientTable) {
                currentPatient = patientTable;
                Log.d(LOG_TAG, "Patient name 1: " + currentPatient.PatientName);
                TextView sickElementName = findViewById(R.id.sickElementName);
                sickElementName.setText(currentPatient.PatientName);
                Log.d(LOG_TAG, "Patient name 2: " + currentPatient.PatientName);
                TextView sickElementSpecies = findViewById(R.id.sickElementSpecies);
                sickElementSpecies.setText(currentPatient.Species);
                TextView sickElementSex = findViewById(R.id.sickElementSex);
                sickElementSex.setText(currentPatient.Sex);
                TextView sickElementEuthanized = findViewById(R.id.sickElementEuthanized);
                if(currentPatient.Euthanized == 0){
                    sickElementEuthanized.setText(R.string.euthanized_neg);
                }
                else {
                    sickElementEuthanized.setText(R.string.euthanized_pos);
                }
                if(patientTable == null){
                    Log.d(LOG_TAG, "patient is null");
                }
            }
        });
        Log.d(LOG_TAG, "Patient name 2: " + currentPatient.PatientName);

        viewModel.getPicturesByID(internalId).observe(this, new Observer<List<PictureTable>>() {
            @Override
            public void onChanged(@Nullable List<PictureTable> pictureTables) {
                if(pictureTables != null) {
                    pictures.addAll(pictureTables);
                    for(int i = 0; i < 5; i++){
                        if(pictures.size() > i && pictures.get(i).Title != null){
                            Bitmap bmp = null;
                            ImageButton bttn = (ImageButton) findViewById(R.id.first_ImageDetails_bttn);

                            String filename = pictures.get(i).ImagePath;
                            try { //try to get the bitmap and set the image button to it
                                FileInputStream is = loadingPictures(filename);
                                bmp = BitmapFactory.decodeStream(is);
                                is.close();
                                images[i].setImageBitmap(bmp);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            images[i].setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });

        viewModel.getSamplesByID(internalId).observe(this, new Observer<List<SampleTable>>() {
            @Override
            public void onChanged(@Nullable List<SampleTable> sampleTables) {
                if(sampleTables != null){
                    samples.addAll(sampleTables);
                } else{
                    Log.d(LOG_TAG, "no samples");
                }
                String sampleText = "";
                int index = 0;
                for(SampleTable tempSample: samples){
                    calendar.setTimeInMillis(tempSample.SampleCollectionDate);
                    String tempSampleDate = simpleDateFormat.format(calendar.getTime());

                    sampleText += "Sample " + tempSample.NameOfSample + ": " + Integer.toString(tempSample.NumberOfSample) + " samples collected "
                            + " in " + tempSample.LocationOfSample + " on " + tempSampleDate + "\n";
                    index++;
                }

                mSamplesTV = findViewById(R.id.subSamplesTV);
                mSamplesTV.setText(sampleText);
            }
        });

        viewModel.getGroupByID(currentSub.Group_ID).observe(this, new Observer<GroupTable>() {
            @Override
            public void onChanged(@Nullable GroupTable groupTable) {
                if(groupTable != null){
                    currentGroup = groupTable;
                    group = currentGroup.GroupName;
                } else{
                    group = "";
                }
            }
        });

        viewModel.getReportByID(internalId).observe(this, new Observer<ReportTable>() {
            @Override
            public void onChanged(@Nullable ReportTable reportTable) {
                if(reportTable != null){
                    currentReport = reportTable;
                    reportExists = true;
                    reportText = currentReport.SubmissionReview;
                    mReportTV.setText(reportText);
                } else {
                    currentReport.Master_ID = internalId;
                }
            }
        });

        viewModel.getRepliesByID(internalId).observe(this, new Observer<List<ReplyTable>>() {
            @Override
            public void onChanged(@Nullable List<ReplyTable> replyTables) {
                mReplies = "";
                String replies = "";
                if(replyTables.size() > 0){
                    mRepliesList = (ArrayList<ReplyTable>) replyTables;
                    mRepliesTV.setText("");
                    for(ReplyTable tempReply: mRepliesList){
                        calendar.setTimeInMillis(tempReply.DateOfMessage);
                        String date = simpleDateFormat.format(calendar.getTime());
                        replies += "(" + date + "): " + tempReply.ContentsOfMessage + "\n\n";
                    }

                }else{
                    Log.d(LOG_TAG, "no replies");
                    replies = "No replies";
                }
                mRepliesTV.setText("");
                mRepliesTV.setText(replies);
                String text = mRepliesTV.getText().toString();
            }
        });

    }

    public FileInputStream loadingPictures(String filename){
        FileInputStream is = null;
        try {
            is = this.openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }


}
