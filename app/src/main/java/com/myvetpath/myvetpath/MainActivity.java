package com.myvetpath.myvetpath;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.myvetpath.myvetpath.data.CategoryItem;
import com.myvetpath.myvetpath.data.PlanetItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    Intent create_sub_activity;
    Intent view_subs_activity;
    Button create_sub_button;
    Button view_subs_button;

    Intent create_account_activity;
    Button create_account_button;


    Intent view_drafts_activity;
    Button view_drafts_button;
    private EntryViewModel mEntryViewModel;

    //The database would be in this variable
    MyDBHandler dbHelper;
    private List<CategoryItem> mCategoryItems;
    ArrayList<PlanetItem> planetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setMenuOptionItemToRemove(this);
        setSupportActionBar(toolbar);

        //Initialize buttons and set the on click listeners
        create_sub_activity = new Intent(this, CreateSubActivity.class);
        view_subs_activity = new Intent(this, ViewSubsActivity.class);

        create_sub_button = findViewById(R.id.createSubButton);
        create_sub_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(create_sub_activity);
            }
        });
        /*
        * this will create a database by the name of db.
        * in the future we would probably do something along the lines of
        * If db.openExistingDatabse() = error
        * then create new database
        * else open the existing one.
         */
        dbHelper = new MyDBHandler(this);
        //dbHelper.dropTable(dbHelper.getWritableDatabase());
        //dbHelper.createTables(dbHelper.getWritableDatabase());

        /*
        * When you want to add a submission to the table it should look something like this:
        * db.addSubmission(yourNewSubmission);
        * where a submission has the value title filled, internal id could also be filled but
        *   the submission table auto increments the id so there is no need to maintain our own.
        *
        * When you want to view all of the submissions you would call the procedure as follows,
        * String results = db.selectAll("Submission");
        *
        * when you want to grab a specific submission
        * Submission sub = db.findSubmissionTitle("title of submission");
        * or if you want to find a submission based on the id
        * Submission sub = db.findSubmissionID(an int number);
        */


        create_account_activity = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_URL))); //TODO Just use placeholder web page for now, update later when we set up account creation page
        create_account_button = findViewById(R.id.createAccountButton);
        create_account_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(create_account_activity.resolveActivity(getPackageManager()) != null){ //if there is a web browser on phone, go ahead and navigate to that page
                    startActivity(create_account_activity);
                }
            }
        });

        view_subs_button = findViewById(R.id.viewSubsButton);
        view_subs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view_subs_activity);
            }
        });

        view_drafts_activity = new Intent(this, ViewDraftsActivity.class);
        view_drafts_button = findViewById(R.id.viewDraftsButton);
        view_drafts_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(view_drafts_activity);
            }
        });

        if(dbHelper.getNumberOfDrafts() == 0){
            view_drafts_button.setVisibility(View.INVISIBLE);
        }


        //query
        mEntryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);
        mCategoryItems = new ArrayList<>();
        setObserverCategory();
        mEntryViewModel.loadCategoryItems("planets", null); //query

        Log.d("k", "onCreate: list:");
    }


    public void setObserverCategory() {

        planetsList = new ArrayList<PlanetItem>();

        //planets
        mEntryViewModel.getPlanet().observe(this, new Observer<List<PlanetItem>>() {
            @Override
            public void onChanged(@Nullable List<PlanetItem> planetItems) {
                if(planetItems != null) {
                    for (PlanetItem item : planetItems) {
                        CategoryItem categoryItem = new CategoryItem();
                        categoryItem.name = item.name;
                        categoryItem.title = null;
                        categoryItem.url = item.url;
                        categoryItem.nextURL = item.nextUrl;
                        mCategoryItems.add(categoryItem);
                    }
                    //do another query
                    if(mCategoryItems.get(mCategoryItems.size()-1).nextURL != null){
                        mEntryViewModel.loadCategoryItems("planets", mCategoryItems.get(mCategoryItems.size()-1).nextURL);
                    }else{
                        List<CategoryItem> tempCategoryItemsList;
                        tempCategoryItemsList = new ArrayList<>();
                        for(PlanetItem item : planetItems){
                            CategoryItem temp = new CategoryItem();
                            temp.nextURL = item.nextUrl;
                            temp.name = item.name;
                            temp.title = null;
                            temp.url = item.url;
                            tempCategoryItemsList.add(temp);
                        }
                        int j= 3;
//                        mEntryAdapter.updateEntryItems(tempCategoryItemsList);
                    }


                }
            }
        });
    }



}
