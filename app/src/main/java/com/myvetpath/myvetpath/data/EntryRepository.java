package com.myvetpath.myvetpath.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.myvetpath.myvetpath.utils.MyVetPathUtils;

import java.util.ArrayList;
import java.util.List;

/*
//Documentation from CS 492

 * This is our Repository class for fetching  data from MyVetPath.  One of the keys
 * to this class is the usage of LiveData, which is a lifecycle-aware data container that can be
 * updated asynchronously and then "observed" from elsewhere.  LiveData observers can perform
 * whatever functionality they wish (e.g. updating the UI) in response to changes to the underlying
 * data in a LiveData object.  Within the Repository itself, we use MutableLiveData objects, so we
 * can update the data from within this class.  We return references to these from the Repository
 * methods as simply LiveData (the parent class of MutableLiveData), so the underlying data can't
 * be changed elsewhere.
 *
 * The Repository class contains two types of LiveData objects: the database items themselves and a loading
 * status field, which is updated from within the Repository class to designate whether a network
 * request is currently underway (Status.LOADING), has completed successfully (Status.SUCCESS), or
 * has failed (Status.ERROR).
 *
 * The Repository class also caches the most recently fetched batch of forecast items and returns
 * the cached version when appropriate.  See the docs for the method shouldFetchForecast() to see
 * when cached results are returned.
 */
public class EntryRepository implements LoaderAsyncTask.AsyncCallback {

    private static final String TAG = EntryRepository.class.getSimpleName();

    private MutableLiveData<List<PlanetItem>> mPlanetsResults;

    private List<PlanetItem> curPlanets;

    private MutableLiveData<List<GroupTable>> mGroupsResults;
    private List<GroupTable> curGroups;

    private MutableLiveData<List<PatientTable>> mPatientsResults;
    private List<PatientTable> curPatients;

    private MutableLiveData<List<PictureTable>> mPicturesResults;
    private List<PictureTable> curPictures;

    private MutableLiveData<List<ReplyTable>> mRepliesResults;
    private List<ReplyTable> curReplies;

    private MutableLiveData<List<ReportTable>> mReportsResults;
    private List<ReportTable> curReports;

    private MutableLiveData<List<SampleTable>> mSamplesResults;
    private List<SampleTable> curSamples;

    private MutableLiveData<List<SubmissionTable>> mSubmissionsResults;
    private List<SubmissionTable> curSubmissions;

    private MutableLiveData<List<UserTable>> mUsersResults;
    private List<UserTable> curUsers;

    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentCategory;


    public EntryRepository() {

        mCurrentCategory = null;

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mPlanetsResults = new MutableLiveData<>();
        mPlanetsResults.setValue(null);
        curPlanets = new ArrayList<>();

        mGroupsResults = new MutableLiveData<>();
        mGroupsResults.setValue(null);
        curGroups = new ArrayList<>();


        mPatientsResults = new MutableLiveData<>();
        mPatientsResults.setValue(null);
        curPatients = new ArrayList<>();


        mPicturesResults = new MutableLiveData<>();
        mPicturesResults.setValue(null);
        curPictures = new ArrayList<>();

        mRepliesResults = new MutableLiveData<>();
        mRepliesResults.setValue(null);
        curReplies = new ArrayList<>();


        mReportsResults = new MutableLiveData<>();
        mReportsResults.setValue(null);
        curReports = new ArrayList<>();

        mSamplesResults = new MutableLiveData<>();
        mSamplesResults.setValue(null);
        curSamples = new ArrayList<>();

        mSubmissionsResults = new MutableLiveData<>();
        mSubmissionsResults.setValue(null);
        curSubmissions = new ArrayList<>();

        mUsersResults = new MutableLiveData<>();
        mUsersResults.setValue(null);
        curUsers = new ArrayList<>();


    }

    /*
     * Returns the LiveData object containing the Repository's loading status.  An observer can be
     * hooked to this, e.g. to display a progress bar or error message when appropriate.
     */
    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    //Returns the current category (the type of query such as "groups")
    public String getCategory(){return mCurrentCategory;}


    //Returns the list of planets retrieved from the API query. Planets are only used to test the app's ability to use an API and get data... this has nothing to do with our actual project
    public LiveData<List<PlanetItem>> getPlanets(){
        return mPlanetsResults;
    }

    //Returns the list of groups retrieved from the API query
    public LiveData<List<GroupTable>> getGroups(){
        return mGroupsResults;
    }

    //Returns the list of patients retrieved from the API query
    public LiveData<List<PatientTable>> getPatients(){
        return mPatientsResults;
    }

    //Returns the list of pictures retrieved from the API query
    public LiveData<List<PictureTable>> getPictures(){
        return mPicturesResults;
    }

    //Returns the list of replies retrieved from the API query
    public LiveData<List<ReplyTable>> getReplies(){
        return mRepliesResults;
    }

    //Returns the list of reports retrieved from the API query
    public LiveData<List<ReportTable>> getReports(){
        return mReportsResults;
    }

    //Returns the list of samples retrieved from the API query
    public LiveData<List<SampleTable>> getSamples(){
        return mSamplesResults;
    }

    //Returns the list of submissions retrieved from the API query
    public LiveData<List<SubmissionTable>> getSubmissions(){
        return mSubmissionsResults;
    }

    //Returns the list of users retrieved from the API query
    public LiveData<List<UserTable>> getUsers(){
        return mUsersResults;
    }

    //This function will start the query. "next" should be null if the results from the API aren't paginated
    public void loadCategory(String category, String next){
        mCurrentCategory = category;
        mLoadingStatus.setValue(Status.LOADING);
        String url;
        if(next == null) {
            url = MyVetPathUtils.buildMyVetPathURL(mCurrentCategory);
        } else {
            url = next;
        }
        Log.d(TAG, "Executing search with url: " + url + " and Category: " + mCurrentCategory);
        new LoaderAsyncTask(url, this, mCurrentCategory).execute();
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onGroupLoadFinished(List<GroupTable> groups) {
        curGroups.addAll(groups);
        mGroupsResults.setValue(curGroups);

        if(groups != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onPatientLoadFinished(List<PatientTable> patients) {
        curPatients.addAll(patients);
        mPatientsResults.setValue(curPatients);

        if(patients != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onPictureLoadFinished(List<PictureTable> pictures) {
        curPictures.addAll(pictures);
        mPicturesResults.setValue(curPictures);

        if(pictures != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onReplyLoadFinished(List<ReplyTable> replies) {
        curReplies.addAll(replies);
        mRepliesResults.setValue(curReplies);

        if(replies != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onReportLoadFinished(List<ReportTable> reports) {
        curReports.addAll(reports);
        mReportsResults.setValue(curReports);

        if(reports != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onSampleLoadFinished(List<SampleTable> samples) {
        curSamples.addAll(samples);
        mSamplesResults.setValue(curSamples);

        if(samples != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onSubmissionsLoadFinished(List<SubmissionTable> submissions) {
        curSubmissions.addAll(submissions);
        mSubmissionsResults.setValue(curSubmissions);

        if(submissions != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onUserLoadFinished(List<UserTable> users) {
        curUsers.addAll(users);
        mUsersResults.setValue(curUsers);

        if(users != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    //This function is called after the query is executed. Set the local variable equal to the results
    @Override
    public void onPlanetLoadFinished(List<PlanetItem> planet) {
        curPlanets.addAll(planet);
        mPlanetsResults.setValue(curPlanets);
        Log.d(TAG, "First planet in list: " + planet.get(0).name + ", list size: " + Integer.toString(curPlanets.size()));
        if(planet != null){
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

}

