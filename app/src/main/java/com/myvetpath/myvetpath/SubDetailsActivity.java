package com.myvetpath.myvetpath;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.sql.Types.NULL;

public class SubDetailsActivity extends BaseActivity {


    Intent create_sub_activity;
    MyDBHandler myDBHandler;
    Submission currentSub;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private ImageButton[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);
        myDBHandler = new MyDBHandler(this);

        int internalId = getIntent().getIntExtra("internalID", 1);
        currentSub = myDBHandler.findSubmissionID(internalId);

        ArrayList<Picture> pictures = myDBHandler.findPictures(internalId);
        Log.d("details", "onCreate: number of pictures in DB: " + myDBHandler.getNumberOfPictures());

        String title = currentSub.getTitle();
        calendar.setTimeInMillis(currentSub.getDateOfCreation());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        TextView internalIDText = findViewById(R.id.subInternalID);
        internalIDText.setText(Integer.toString(internalId));

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
