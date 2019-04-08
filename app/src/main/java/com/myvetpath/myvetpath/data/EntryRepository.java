package com.myvetpath.myvetpath.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.myvetpath.myvetpath.utils.StarWarsUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * This is our Repository class for fetching forecast data from OpenWeatherMap.  One of the keys
 * to this class is the usage of LiveData, which is a lifecycle-aware data container that can be
 * updated asynchronously and then "observed" from elsewhere.  LiveData observers can perform
 * whatever functionality they wish (e.g. updating the UI) in response to changes to the underlying
 * data in a LiveData object.  Within the Repository itself, we use MutableLiveData objects, so we
 * can update the data from within this class.  We return references to these from the Repository
 * methods as simply LiveData (the parent class of MutableLiveData), so the underlying data can't
 * be changed elsewhere.
 *
 * The Repository class contains two LiveData objects: the forecast items themselves and a loading
 * status field, which is updated from within the Repository class to designate whether a network
 * request is currently underway (Status.LOADING), has completed successfully (Status.SUCCESS), or
 * has failed (Status.ERROR).
 *
 * The Repository class also caches the most recently fetched batch of forecast items and returns
 * the cached version when appropriate.  See the docs for the method shouldFetchForecast() to see
 * when cached results are returned.
 */
public class EntryRepository implements LoadForecastTask.AsyncCallback {

    private static final String TAG = EntryRepository.class.getSimpleName();

    private MutableLiveData<List<PlanetItem>> mPlanetsResults;

    private List<PlanetItem> curPlanets;

    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentCategory;


    public EntryRepository() {

        mCurrentCategory = null;

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mPlanetsResults = new MutableLiveData<>();
        mPlanetsResults.setValue(null);


        curPlanets = new ArrayList<>();


    }

    /*
     * Returns the LiveData object containing the Repository's loading status.  An observer can be
     * hooked to this, e.g. to display a progress bar or error message when appropriate.
     */
    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    public String getCategory(){return mCurrentCategory;}



    public LiveData<List<PlanetItem>> getPlanets(){
        return mPlanetsResults;
    }



    public void loadCategory(String category, String next){
        mCurrentCategory = category;
        mLoadingStatus.setValue(Status.LOADING);
        String url;
        if(next == null) {
            url = StarWarsUtils.buildStarWarsURL(mCurrentCategory);
        } else {
            url = next;
        }
        Log.d(TAG, "Executing search with url: " + url + " and Category: " + mCurrentCategory);
        new LoadForecastTask(url, this, mCurrentCategory).execute();
    }


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

