package com.myvetpath.myvetpath.data;

import android.os.AsyncTask;
import android.util.Log;


import com.myvetpath.myvetpath.utils.NetworkUtils;
import com.myvetpath.myvetpath.utils.MyVetPathUtils;

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
public class LoaderAsyncTask extends AsyncTask<Void, Void, String> {

    private static String TAG = LoaderAsyncTask.class.getSimpleName();

    public interface AsyncCallback {
        void onGroupLoadFinished(List<GroupTable> groups);
        void onPatientLoadFinished(List<PatientTable> patients);
        void onPictureLoadFinished(List<PictureTable> pictures);
        void onReplyLoadFinished(List<ReplyTable> replies);
        void onReportLoadFinished(List<ReportTable> reports);
        void onSampleLoadFinished(List<SampleTable> samples);
        void onSubmissionsLoadFinished(List<SubmissionTable> submissions);
        void onUserLoadFinished(List<UserTable> users);
        void onPlanetLoadFinished(List<PlanetItem> planets);

    }

    private String mURL;
    private String mNextURL;
    private AsyncCallback mCallback;
    private String mCurrentCategory;

    public LoaderAsyncTask(String url, AsyncCallback callback, String category) {
        mURL = url;
        mCallback = callback;
        mCurrentCategory = category;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String MyVetPathJSON = null;
        try {
            MyVetPathJSON = NetworkUtils.doHTTPGet(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyVetPathJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            //This is using a different API, but it's here to show that the app is able to use an API to get data
//            if (mCurrentCategory.equals("planets")) {
//                Log.d(TAG, "Planets Loading: " + s);
//                List<PlanetItem> planets = MyVetPathUtils.parsePlanetsJSON(s);
//                mCallback.onPlanetLoadFinished(planets);
//                return;
//            }
            if(mCurrentCategory.equals("groups")){
                List<GroupTable> groups = MyVetPathUtils.parseGroupsJSON(s);
                mCallback.onGroupLoadFinished(groups);
                return;
            }else if(mCurrentCategory.equals("patient")){
                List<PatientTable> patients = MyVetPathUtils.parsePatientsJSON(s);
                mCallback.onPatientLoadFinished(patients);
            }else if(mCurrentCategory.equals("picture")){
                List<PictureTable> pictures = MyVetPathUtils.parsePicturesJSON(s);
                mCallback.onPictureLoadFinished(pictures);
            }else if(mCurrentCategory.equals("reply")){
                List<ReplyTable> replies = MyVetPathUtils.parseRepliesJSON(s);
                mCallback.onReplyLoadFinished(replies);
            }else if(mCurrentCategory.equals("report")){
                List<ReportTable> reports = MyVetPathUtils.parseReportsJSON(s);
                mCallback.onReportLoadFinished(reports);
            }else if(mCurrentCategory.equals("sample")){
                List<SampleTable> samples = MyVetPathUtils.parseSamplesJSON(s);
                mCallback.onSampleLoadFinished(samples);
            }else if(mCurrentCategory.equals("submissions")){
                List<SubmissionTable> submissions = MyVetPathUtils.parseSubmissionsJSON(s);
                mCallback.onSubmissionsLoadFinished(submissions);
            }else if(mCurrentCategory.equals("users")){
                List<UserTable> users = MyVetPathUtils.parseUsersJSON(s);
                mCallback.onUserLoadFinished(users);
            }
        }
    }
}
