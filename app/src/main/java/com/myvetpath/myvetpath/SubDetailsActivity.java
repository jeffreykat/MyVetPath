package com.myvetpath.myvetpath;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.sql.Types.NULL;

public class SubDetailsActivity extends BaseActivity {

    MyVetPathViewModel viewModel;

    Intent create_sub_activity;
    SubmissionTable currentSub;
    PatientTable currentPatient;
    GroupTable currentGroup;
    List<PictureTable> pictures;
    List<SampleTable> samples;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private TextView mSamplesTV;
    private ImageButton[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);

        ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        int internalId = getIntent().getIntExtra("internalID", 1);

        viewModel.getSubmissionByID(internalId).observe(this, new Observer<SubmissionTable>() {
            @Override
            public void onChanged(@Nullable SubmissionTable submissionTable) {
                currentSub = submissionTable;
            }
        });

        viewModel.getPatientByID(internalId).observe(this, new Observer<PatientTable>() {
            @Override
            public void onChanged(@Nullable PatientTable patientTable) {
                currentPatient = patientTable;
                Log.d("SubDetails", "Name: " + currentPatient.PatientName);
            }
        });

        viewModel.getPicturesByID(internalId).observe(this, new Observer<List<PictureTable>>() {
            @Override
            public void onChanged(@Nullable List<PictureTable> pictureTables) {
                pictures = pictureTables;
                Log.d("details", "onCreate: number of pictures in DB: " + pictures.size());
            }
        });

        viewModel.getSamplesByID(internalId).observe(this, new Observer<List<SampleTable>>() {
            @Override
            public void onChanged(@Nullable List<SampleTable> sampleTables) {
                samples = sampleTables;
            }
        });

        viewModel.getGroupByID(currentSub.Group_ID).observe(this, new Observer<GroupTable>() {
            @Override
            public void onChanged(@Nullable GroupTable groupTable) {
                currentGroup = groupTable;
            }
        });

        String title = currentSub.Title;

        calendar.setTimeInMillis(currentSub.DateOfCreation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        String group = currentGroup.GroupName;
        calendar.setTimeInMillis(currentSub.DateOfCreation);
        String comment = currentSub.UserComment;

        create_sub_activity = new Intent(this, CreateSubActivity.class);

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
        TextView sickElementName = findViewById(R.id.sickElementName);
        sickElementName.setText(currentPatient.PatientName);
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
        TextView commentText = findViewById(R.id.subComment);
        commentText.setText(comment);


        //set images
        images = new ImageButton[]{findViewById(R.id.first_ImageDetails_bttn), findViewById(R.id.second_ImageDetails_bttn),
                findViewById(R.id.third_ImageDetails_bttn), findViewById(R.id.fourth_ImageDetails_bttn),
                findViewById(R.id.fifth_ImageDetails_bttn)};

        for(int i = 0; i < 5; i++){
            if(pictures.size() > i && pictures.get(i).Title != null){
                Bitmap bmp = null;
                ImageButton bttn = (ImageButton) findViewById(R.id.first_ImageDetails_bttn);

                String filename = pictures.get(i).ImagePath;
                try { //try to get the bitmap and set the image button to it
                    FileInputStream is = this.openFileInput(filename);
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
