package com.myvetpath.myvetpath;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
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

public class SubDetailsActivity extends BaseActivity {

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
    private TextView mReportReview;
    private CheckBox mReportCheck;

    String group = new String();
    boolean reportExists = false;

    public void createReportDialog(){
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder dialog = new AlertDialog.Builder(SubDetailsActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.report_check);
        dialog.setView(inflater.inflate(R.layout.report_dialog, null)).setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*String review = mReportReview.getText().toString();
                boolean closed = mReportCheck.isChecked();
                currentReport.SubmissionReview = review;
                currentReport.ReportDate = calendar.getTimeInMillis();
                if(reportExists){
                    viewModel.updateReport(currentReport);
                } else {
                    viewModel.insertReport(currentReport);
                }*/
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

        mReportReview = findViewById(R.id.report_dialog_tv);
        mReportCheck = findViewById(R.id.report_dialog_check);

        FloatingActionButton fab = findViewById(R.id.details_report_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "clicked report button");
                createReportDialog();
            }
        });

        create_sub_activity = new Intent(this, CreateSubActivity.class);

        long internalId = currentSub.Master_ID;
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
                } else {
                    currentReport.Master_ID = internalId;
                }
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
