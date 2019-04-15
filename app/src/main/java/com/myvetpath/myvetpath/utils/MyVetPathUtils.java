package com.myvetpath.myvetpath.utils;

import android.net.Uri;


import com.google.gson.Gson;
import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.PlanetItem;
import com.myvetpath.myvetpath.data.ReplyTable;
import com.myvetpath.myvetpath.data.ReportTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;
import com.myvetpath.myvetpath.data.UserTable;

import java.util.ArrayList;

public class MyVetPathUtils {
    //These two local variables are only here to test that the app can use an API. They aren't actually used and are unrelated to the final project.
    private final static String BASE_API = "https://swapi.co/api/";
    private final static String PLANETS_API = "planets";

    //These are the local variables that will be used for the API.
    private final static String BASE_MVP_API = "http://208.113.134.137:8000/v1/";
    private final static String USERS_API = "users";
    private final static String PATIENTS_API = "patient";
    private final static String SUBMISSION_API = "submissions";
    private final static String GROUPS_API = "groups";
    private final static String REPORTS_API = "report";
    private final static String PICTURE_API = "picture";
    private final static String REPLY_API = "reply";
    private final static String SAMPLES_API = "sample";
    private final static String SYNC_API = "sync"; //there is a sync route, but we probably won't use it

    //The results from the API query will be stored in this class
    static class MyVetPathGroupsResults{
        ArrayList<GroupTable> results;
    }

    //This will parse the json results and will return an array of GroupTables
    public static ArrayList<GroupTable> parseGroupsJSON(String url) {
        Gson gson = new Gson();
        MyVetPathGroupsResults MVPResults = gson.fromJson(url, MyVetPathGroupsResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathPatientsResults{
        ArrayList<PatientTable> results;
    }


    //This will parse the json results and will return an array of PatientTables
    public static ArrayList<PatientTable> parsePatientsJSON(String url) {
        Gson gson = new Gson();
        MyVetPathPatientsResults MVPResults = gson.fromJson(url, MyVetPathPatientsResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathPicturesResults{
        ArrayList<PictureTable> results;
    }

    //This will parse the json results and will return an array of PictureTable
    public static ArrayList<PictureTable> parsePicturesJSON(String url) {
        Gson gson = new Gson();
        MyVetPathPicturesResults MVPResults = gson.fromJson(url, MyVetPathPicturesResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathRepliesResults{
        ArrayList<ReplyTable> results;
    }

    //This will parse the json results and will return an array of ReplyTables
    public static ArrayList<ReplyTable> parseRepliesJSON(String url) {
        Gson gson = new Gson();
        MyVetPathRepliesResults MVPResults = gson.fromJson(url, MyVetPathRepliesResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathReportsResults{
        ArrayList<ReportTable> results;
    }

    //This will parse the json results and will return an array of ReportTables
    public static ArrayList<ReportTable> parseReportsJSON(String url) {
        Gson gson = new Gson();
        MyVetPathReportsResults MVPResults = gson.fromJson(url, MyVetPathReportsResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathSamplesResults{
        ArrayList<SampleTable> results;
    }


    //This will parse the json results and will return an array of SampleTables
    public static ArrayList<SampleTable> parseSamplesJSON(String url) {
        Gson gson = new Gson();
        MyVetPathSamplesResults MVPResults = gson.fromJson(url, MyVetPathSamplesResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathSubmissionResults{
        ArrayList<SubmissionTable> results;
    }

    //This will parse the json results and will return an array of SubmissionTables
    public static ArrayList<SubmissionTable> parseSubmissionsJSON(String url) {
        Gson gson = new Gson();
        ArrayList<UserTable> allSubmissions = new ArrayList<>();
        MyVetPathSubmissionResults MVPResults = gson.fromJson(url, MyVetPathSubmissionResults.class);
        if(MVPResults != null){
            return MVPResults.results;
        }
        return null;
    }

    //The results from the API query will be stored in this class
    static class MyVetPathUsersResults{
       ArrayList<UserTable> results;
    }

    //This will parse the json results and will return an array of UserTables
    public static ArrayList<UserTable> parseUsersJSON(String url) {
        Gson gson = new Gson();
        ArrayList<UserTable> allUsers = new ArrayList<>();
        MyVetPathUsersResults userResults = gson.fromJson(url, MyVetPathUsersResults.class);
        if(userResults != null){
            return userResults.results;
        }
        return null;
    }



    //This is the function that builds the URL string that will be used by the API to make the necessary queries
    public static String buildMyVetPathURL(String category) { //todo add mvp item
        if(category.equals(PLANETS_API)){
            return Uri.parse(BASE_API).buildUpon().appendPath(PLANETS_API).build().toString();
        }else if(category.equals(USERS_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(USERS_API).build().toString();
        }else if(category.equals(PATIENTS_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(PATIENTS_API).build().toString();
        }else if(category.equals(SUBMISSION_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(SUBMISSION_API).build().toString();
        }else if(category.equals(GROUPS_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(GROUPS_API).build().toString();
        }else if(category.equals(REPORTS_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(REPORTS_API).build().toString();
        }else if(category.equals(PICTURE_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(PICTURE_API).build().toString();
        }else if(category.equals(REPLY_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(REPLY_API).build().toString();
        }else if(category.equals(SAMPLES_API)){
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(SAMPLES_API).build().toString();
        }else{ //if(category.equals("sync")
            return Uri.parse(BASE_MVP_API).buildUpon().appendPath(SUBMISSION_API).build().toString();
        }

//            }
        }




    /*
     * The below class was used with an entirely different API and is completely unrelated to our project. This is only here to test
     * The app's ability to use an API
     */
    static class StarWarsSearchPlanetsResults {
        Integer count;
        String next;
        ArrayList<PlanetItem> results;
    }

    //This parses results from an unrelated API. This is only here to test the app's ability to use an API
    public static ArrayList<PlanetItem> parsePlanetsJSON(String url){
        Gson gson = new Gson();
        //EntryRepository mEntryRepo = new EntryRepository();
        ArrayList<PlanetItem> allPlanets = new ArrayList<>();
        StarWarsSearchPlanetsResults planetsResults = gson.fromJson(url, StarWarsSearchPlanetsResults.class);
        if(planetsResults != null && planetsResults.results != null){
            planetsResults.results.get(planetsResults.results.size()-1).nextUrl = planetsResults.next;
          //  allPlanets.addAll(planetsResults.results);
            /*String next = planetsResults.next;
            if(next != null){
                mEntryRepo.loadCategory("Planets", planetsResults.next);
            }*/
            return planetsResults.results;
        } else {
            return null;
        }
    }

}
