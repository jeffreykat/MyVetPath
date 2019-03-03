package com.myvetpath.myvetpath;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.sql.Types.NULL;

public class SubDetailsActivity extends BaseActivity {


    Intent create_sub_activity;
    MyDBHandler myDBHandler;
    Submission currentSub;
    SickElement currentSickElement;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);
        myDBHandler = new MyDBHandler(this);

        int internalId = getIntent().getIntExtra("internalID", 1);
        currentSub = myDBHandler.findSubmissionID(internalId);
        int sickID = currentSub.getSickElementID();
        currentSickElement = myDBHandler.findSickElementID(sickID);
        Log.d("SubDetails", "Name: " + currentSickElement.getNameOfSickElement());
        String title = currentSub.getTitle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        TextView sickElementName = findViewById(R.id.sickElementName);
        sickElementName.setText(currentSickElement.getNameOfSickElement());
        TextView sickElementSpecies = findViewById(R.id.sickElementSpecies);
        sickElementSpecies.setText(currentSickElement.getSpecies());
        TextView sickElementSex = findViewById(R.id.sickElementSex);
        sickElementSex.setText(currentSickElement.getSex());
        TextView sickElementEuthanized = findViewById(R.id.sickElementEuthanized);
        if(currentSickElement.getEuthanized() == 0){
            sickElementEuthanized.setText(R.string.euthanized_neg);
        }
        else {
            sickElementEuthanized.setText(R.string.euthanized_pos);
        }
        TextView commentText = findViewById(R.id.subComment);
        commentText.setText(comment);
    }


}
