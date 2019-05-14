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
import android.widget.Toast;

import com.myvetpath.myvetpath.data.CategoryItem;
import com.myvetpath.myvetpath.data.MyVetPathAPI;
import com.myvetpath.myvetpath.data.PlanetItem;
import com.myvetpath.myvetpath.data.SubmissionTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.Types.NULL;

public class MainActivity extends BaseActivity {

    Intent create_sub_activity;
    Intent view_subs_activity;
    Button create_sub_button;
    Button view_subs_button;

    Intent create_account_activity;
    Button create_account_button;


    Intent view_drafts_activity;
    Button view_drafts_button;

    //access to the database
    MyVetPathViewModel viewModel;
    private EntryViewModel mEntryViewModel;
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
        create_sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(create_sub_activity);
            }
        });

        viewModel = ViewModelProviders.of(this).get(MyVetPathViewModel.class);

        create_account_activity = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_URL))); //TODO Just use placeholder web page for now, update later when we set up account creation page
        create_account_button = findViewById(R.id.createAccountButton);
        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create_account_activity.resolveActivity(getPackageManager()) != null) { //if there is a web browser on phone, go ahead and navigate to that page
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
        view_drafts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view_drafts_activity);
            }
        });

        //checks if contents of drafts has changed and sets the button visibility accordingly
        viewModel.getDrafts().observe(this, new Observer<List<SubmissionTable>>() {
            @Override
            public void onChanged(@Nullable List<SubmissionTable> submissionTables) {
                if (submissionTables == null || submissionTables.size() < 1) {
                    view_drafts_button.setVisibility(View.INVISIBLE);
                } else {
                    view_drafts_button.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    //This is the function that makes an API call, but it is commented out because it is just an example that won't be used with
    //the app
    /*
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
*/


}
