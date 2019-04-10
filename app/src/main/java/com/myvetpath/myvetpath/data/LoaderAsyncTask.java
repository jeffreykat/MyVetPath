package com.myvetpath.myvetpath.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class LoaderAsyncTask extends AsyncTask<Void, Void, String> {

    public static String TAG = LoaderAsyncTask.class.getSimpleName();

    public interface AsyncCallback{
        void onGroupLoadFinished(List<GroupTable> groups);
        void onPatientLoadFinished(List<PatientTable> patients);
        void onPictureLoadFinished(List<PictureTable> pictures);
        void onReplyLoadFinished(List<ReplyTable> replies);
        void onReportLoadFinished(List<ReportTable> reports);
        void onSampleLoadFinished(List<SampleTable> samples);
        void onSubmissionsLoadFinished(List<SubmissionTable> submissions);
        void onUserLoadFinished(List<UserTable> users);
    }

    private String mURL;
    private AsyncCallback mCallback;

    public LoaderAsyncTask(String url, AsyncCallback callback){
        mURL = url;
        mCallback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String myvetpathJSON = null;
        return myvetpathJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
