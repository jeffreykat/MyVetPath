package com.myvetpath.myvetpath.data;

import android.os.AsyncTask;
import android.util.Log;


import com.myvetpath.myvetpath.utils.NetworkUtils;
import com.myvetpath.myvetpath.utils.StarWarsUtils;

import java.io.IOException;
import java.util.List;

/*
 * This is our AsyncTask for loading forecast data from OWM.  It mirrors the AsyncTask we used
 * previously for loading the forecast, except here, we specify an interface AsyncCallback to
 * provide the functionality to be performed in the main thread when the task is finished.
 * This is needed because, to avoid memory leaks, the AsyncTask class is no longer an inner class,
 * so it can no longer directly access the fields it needs to update when loading is finished.
 * Instead, we provide a callback function (using AsyncCallback) to perform those updates.
 */
public class LoadForecastTask extends AsyncTask<Void, Void, String> {

    private static String TAG = LoadForecastTask.class.getSimpleName();

    public interface AsyncCallback {

        void onPlanetLoadFinished(List<PlanetItem> planets);

    }

    private String mURL;
    private String mNextURL;
    private AsyncCallback mCallback;
    private String mCurrentCategory;

    public LoadForecastTask(String url, AsyncCallback callback, String category) {
        mURL = url;
        mCallback = callback;
        mCurrentCategory = category;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String starwarsJSON = null;
        try {
            starwarsJSON = NetworkUtils.doHTTPGet(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return starwarsJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            if (mCurrentCategory.equals("planets")) {
                Log.d(TAG, "Planets Loading: " + s);
                List<PlanetItem> planets = StarWarsUtils.parsePlanetsJSON(s);
                mCallback.onPlanetLoadFinished(planets);
                return;
            }
        }
    }
}
