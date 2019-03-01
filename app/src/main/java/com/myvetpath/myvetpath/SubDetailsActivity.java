package com.myvetpath.myvetpath;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.sql.Types.NULL;

public class SubDetailsActivity extends BaseActivity {


    Intent create_sub_activity;
    MyDBHandler myDBHandler;
    Submission currentSub;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private TextView mSamplesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);
        myDBHandler = new MyDBHandler(this);

        int internalId = getIntent().getIntExtra("internalID", 1);
        currentSub = myDBHandler.findSubmissionID(internalId);
        String title = currentSub.getTitle();


        ArrayList<Sample> samplesList = myDBHandler.findSamples(internalId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String sampleText = "";
        int index = 0;
        for(Sample tempSample: samplesList){
            calendar.setTimeInMillis(tempSample.getSampleCollectionDate());
            String tempSampleDate = simpleDateFormat.format(calendar.getTime());

            sampleText += "Sample " + tempSample.getNameOfSample() + ": " + tempSample.getNumberOfSamples() + " samples collected "
                    + " in " + tempSample.getLocation() + " on " + tempSampleDate + "\n";
            index++;
        }

        mSamplesTV = findViewById(R.id.subSamplesTV);
        mSamplesTV.setText(sampleText);

        String group = currentSub.getGroup();
        calendar.setTimeInMillis(currentSub.getDateOfCreation());
        String comment = currentSub.getComment();

        create_sub_activity = new Intent(this, CreateSubActivity.class);

        TextView titleText = findViewById(R.id.subTitle);
        titleText.setText(title);
        TextView dateText = findViewById(R.id.subDate);
        dateText.setText(simpleDateFormat.format(calendar.getTime()));
        TextView caseIDText = findViewById(R.id.subCaseID);
        if(currentSub.getCaseID() == NULL){
            caseIDText.setText(R.string.pending);
        } else {
            caseIDText.setText(String.valueOf(currentSub.getCaseID()));
        }
        if(!group.isEmpty()) {
            TextView groupText = findViewById(R.id.subGroupName);
            groupText.setText("Group: " + group);
        }
        TextView commentText = findViewById(R.id.subComment);
        commentText.setText(comment);
    }


}
