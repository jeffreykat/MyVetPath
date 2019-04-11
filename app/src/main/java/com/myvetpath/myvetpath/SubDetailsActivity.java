package com.myvetpath.myvetpath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        //Set text and add to database
        mReplies += "(" + currentDate + ")" + input + "\n \n";
        mRepliesTV.setText(mReplies);
    }

    Intent create_sub_activity;
    MyDBHandler myDBHandler;
    Submission currentSub;
    SickElement currentSickElement;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private TextView mSamplesTV;
    private ImageButton[] images;
    private Button add_replies_BTTN;
    private TextView mRepliesTV;
    private String mReplyInput;
    private String mReplies;
    private ArrayList<Reply> mRepliesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);
        myDBHandler = new MyDBHandler(this);

        int internalId = getIntent().getIntExtra("internalID", 1);
        currentSub = myDBHandler.findSubmissionID(internalId);

        currentSickElement = myDBHandler.findSickElementID(internalId);
        Log.d("SubDetails", "Name: " + currentSickElement.getNameOfSickElement());

        ArrayList<Picture> pictures = myDBHandler.findPictures(internalId);
        Log.d("details", "onCreate: number of pictures in DB: " + myDBHandler.getNumberOfPictures());
        ArrayList<Sample> samplesList = myDBHandler.findSamples(internalId);

        String title = currentSub.getTitle();

        calendar.setTimeInMillis(currentSub.getDateOfCreation());
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
                    + " in " + tempSample.getLocation() + " on " + tempSampleDate + "\n\n";
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

        //Set up Replies
        mRepliesTV = findViewById(R.id.subRepliesTV);
        add_replies_BTTN = findViewById(R.id.add_reply_BTTN);
        mReplies = "";
        mRepliesList = new ArrayList<>();// set this equal to what we have in database

        Reply tempReply = new Reply();
        tempReply.setMessage("Here's my first message");
        mRepliesList.add(tempReply);

        for(Reply reply : mRepliesList){
            mReplies += reply.getMessage() + "\n";
        }

        mRepliesTV.setText(mReplies);

        add_replies_BTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReplyCustomDialog dialog = new AddReplyCustomDialog();
                dialog.show(getFragmentManager(), "AddReplyCustomDialog");


            }
        });


        //set images
        images = new ImageButton[]{findViewById(R.id.first_ImageDetails_bttn), findViewById(R.id.second_ImageDetails_bttn),
                findViewById(R.id.third_ImageDetails_bttn), findViewById(R.id.fourth_ImageDetails_bttn),
                findViewById(R.id.fifth_ImageDetails_bttn)};

        for(int i = 0; i < 5; i++){
            if(pictures.size() > i && pictures.get(i).getImageTitle() != null){
                Bitmap bmp = null;
                ImageButton bttn = (ImageButton) findViewById(R.id.first_ImageDetails_bttn);

                String filename = pictures.get(i).getPicturePath();
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
