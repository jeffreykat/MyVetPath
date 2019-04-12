package com.myvetpath.myvetpath;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.ReplyTable;
import com.myvetpath.myvetpath.data.ReportTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        ReplyTable tempReply = new ReplyTable();
        tempReply.ContentsOfMessage = input;
        tempReply.DateOfMessage = curDate;
        tempReply.Master_ID = internalId;
        viewModel.insertReply(tempReply);

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
    private Button add_replies_BTTN;
    private TextView mRepliesTV;
    private String mReplyInput;
    private String mReplies;
    private ArrayList<ReplyTable> mRepliesList = new ArrayList<>(1);

    String group = new String();
    private long internalId;

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

    public void setObservers(long internalId){
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

                    sampleText += "Sample " + tempSample.NameOfSample + ": " + tempSample.NameOfSample + " samples collected "
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
