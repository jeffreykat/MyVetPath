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

public class SubDetailsActivity extends BaseActivity {

    Intent create_sub_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_details);
        int position = getIntent().getIntExtra("pos", 0);
        String title = getResources().getStringArray(R.array.subsTitles)[position];
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        create_sub_activity = new Intent(this, CreateSubActivity.class);

        TextView titleText = findViewById(R.id.subTitle);
        titleText.setText(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
