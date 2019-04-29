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









        SubmissionTable newSub = new SubmissionTable();
        newSub.Title = "from android";
        newSub.Case_ID = 0;
        newSub.Group_ID = 0;
        newSub.Master_ID = 0;
        newSub.DateOfCreation = 1;
        newSub.StatusFlag = 0;
        newSub.ReportComplete = 0;
        newSub.Submitted = 1;
        newSub.UserComment = "user comment";


//        String MyVetPath_Base_Url = getString(R.string.MVP_Base_API_URL);
//        //build the retrofit that will make the query
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://208.113.134.137:8000/v1/submissions/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        MyVetPathAPI myVetPathAPI = retrofit.create(MyVetPathAPI.class);
//
//        HashMap<String, String> headerMap = new HashMap<String, String>();
//        headerMap.put("Content-Type", "application/json");
//        //Send the Submission original method
//        Call<ResponseBody> call = myVetPathAPI.submission(headerMap, newSub.Group_ID, newSub.User_ID,
//                newSub.Title, newSub.DateOfCreation, newSub.StatusFlag, newSub.Submitted, newSub.ReportComplete,
//                newSub.UserComment, "json");
//
////        call.enqueue(new Callback<ResponseBody>() {
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d("s", "onResponse: kjkl");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("s", "onResponse: fail");
//            }
//        });


    //method 2
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo4LCJpYXQiOjE1NTY0OTA2MzksImV4cCI6MTU1NjU3NzAzOX0.E8z1rPMxzK2HpIBU2NMSE28awUexLxn8VSE-s91KhG0";


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://208.113.134.137:8000/v1/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        MyVetPathAPI userClient = retrofit.create(MyVetPathAPI.class);

        Call<SubmissionTable> call =userClient.submission(newSub);
        call.enqueue(new Callback<SubmissionTable>() {
            @Override
            public void onResponse(Call<SubmissionTable> call, Response<SubmissionTable> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.body().Title, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "error nto correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmissionTable> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
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
