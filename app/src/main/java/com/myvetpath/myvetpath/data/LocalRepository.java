package com.myvetpath.myvetpath.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.myvetpath.myvetpath.OnSubmissionInserted;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocalRepository {
    private static String TAG = LocalRepository.class.getSimpleName();

    private MyVetPathDao dao;
    private OnSubmissionInserted listener;

    public LocalRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.myVetPathDao();
    }

    public long insertGroup(GroupTable groupTable){
        long id = 0;
        try {
            id = new InsertGroupAsyncTask(dao).execute(groupTable).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void deleteGroup(GroupTable groupTable){new DeleteGroupAsyncTask(dao).execute(groupTable);}

    public void updateGroup(GroupTable groupTable){new UpdateGroupAsyncTask(dao).execute(groupTable);}

    public LiveData<List<GroupTable>> getGroups(){
        return dao.getGroups();
    }

    public LiveData<GroupTable> getGroupByName(String name){return dao.getGroupByName(name);}

    public LiveData<GroupTable> getGroupByID(long id){return dao.getGroupByID(id);}

    public void insertPatient(PatientTable patientTable){new InsertPatientAsyncTask(dao).execute(patientTable);}

    public void deletePatient(PatientTable patientTable){new DeletePatientAsyncTask(dao).execute(patientTable);}

    public void updatePatient(PatientTable patientTable){new UpdatePatientAsyncTask(dao).execute(patientTable);}

    public LiveData<PatientTable> getPatientByID(long id){
        return dao.getPatientByID(id);
    }

    public void insertPicture(PictureTable pictureTable){
        new InsertPictureAsyncTask(dao).execute(pictureTable);
        Log.d("LocalRepo", "Insert " + pictureTable.Title + " Master_ID: " + Long.toString(pictureTable.Master_ID));
    }

    public void deletePicture(PictureTable pictureTable){new DeletePictureAsyncTask(dao).execute(pictureTable);}

    public void updatePicture(PictureTable pictureTable){new UpdatePictureAsyncTask(dao).execute(pictureTable);}

    public LiveData<List<PictureTable>> getPicturesByID(long id){
        return dao.getPicturesByID(id);
    }

    public LiveData<PictureTable> getPictureByTitle(String title){
        return dao.getPictureByTitle(title);
    }

    public void insertReply(ReplyTable replyTable){new InsertReplyAsyncTask(dao).execute(replyTable);}

    public LiveData<List<ReplyTable>> getRepliesByID(long id){
        return dao.getRepliesByID(id);
    }

    public LiveData<List<ReplyTable>> getRepliesBySender(int id){
        return dao.getRepliesBySender(id);
    }

    public void insertReport(ReportTable reportTable){new InsertReportAsyncTask(dao).execute(reportTable);}

    public void updateReport(ReportTable reportTable){new UpdateReportAsyncTask(dao).execute(reportTable);}

    public LiveData<ReportTable> getReportByID(long id){
        return dao.getReportByID(id);
    }

    public void insertSample(SampleTable sampleTable){
        new InsertSampleAsyncTask(dao).execute(sampleTable);
        Log.d("LocalRepo", "Insert " + sampleTable.NameOfSample + " Master_ID: " + Long.toString(sampleTable.Master_ID));
    }

    public void deleteSample(SampleTable sampleTable){new DeleteSampleAsyncTask(dao).execute(sampleTable);}

    public void updateSample(SampleTable sampleTable){new UpdateSampleAsyncTask(dao).execute(sampleTable);}

    public LiveData<List<SampleTable>> getSamplesByID(long id){
        return dao.getSamplesByID(id);
    }

    public LiveData<SampleTable> getSampleByName(String name){
        return dao.getSampleByName(name);
    }

    public long insertSubmission(SubmissionTable submissionTable, OnSubmissionInserted inserted){
        long id = 0;
        try {
            id = new InsertSubmissionAsyncTask(dao, inserted).execute(submissionTable).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("LocalRepo", "Insert " + submissionTable.Title + " Sub Master_ID: " + Long.toString(id));
        return id;
    }

    public void deleteSubmission(SubmissionTable submissionTable){new DeleteSubmissionAsyncTask(dao).execute(submissionTable);}

    public void updateSubmission(SubmissionTable submissionTable){new UpdateSubmissionAsyncTask(dao).execute(submissionTable);}

    public LiveData<List<SubmissionTable>> getSubmissions(){
        return dao.getSubmissions();
    }

    public LiveData<List<SubmissionTable>> getDrafts(){
        return dao.getDrafts();
    }

    public LiveData<SubmissionTable> getSubmissionByTitle(String title){
        return dao.getSubmissionByTitle(title);
    }

    public LiveData<SubmissionTable> getSubmissionByID(long id){
        return dao.getSubmissionByID(id);
    }

    public void insertUser(UserTable userTable){new InsertUserAsyncTask(dao).execute(userTable);}

    public void deleteUser(UserTable userTable){new DeleteUserAsyncTask(dao).execute(userTable);}

    public void updateUser(UserTable userTable){new UpdateUserAsyncTask(dao).execute(userTable);}

    public LiveData<List<UserTable>> getUsers(){
        return dao.getUsers();
    }

    public LiveData<UserTable> getUserByUsername(String username){
        return dao.getUserByUsername(username);
    }

    private static class InsertGroupAsyncTask extends AsyncTask<GroupTable, Void, Long>{
        MyVetPathDao dao;

        InsertGroupAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Long doInBackground(GroupTable... groupTables) {
            return dao.insertGroup(groupTables[0]);
        }
    }

    private static class DeleteGroupAsyncTask extends AsyncTask<GroupTable, Void, Void>{
        MyVetPathDao dao;

        DeleteGroupAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(GroupTable... groupTables) {
            dao.deleteGroup(groupTables[0]);
            return null;
        }
    }

    private static class UpdateGroupAsyncTask extends AsyncTask<GroupTable, Void, Void>{
        MyVetPathDao dao;

        UpdateGroupAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(GroupTable... groupTables) {
            dao.updateGroup(groupTables[0]);
            return null;
        }
    }

    private static class InsertPatientAsyncTask extends AsyncTask<PatientTable, Void, Void>{
        MyVetPathDao dao;

        InsertPatientAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(PatientTable... patientTables) {
            dao.insertPatient(patientTables[0]);
            return null;
        }
    }

    private static class DeletePatientAsyncTask extends AsyncTask<PatientTable, Void, Void>{
        MyVetPathDao dao;

        DeletePatientAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(PatientTable... patientTables) {
            dao.deletePatient(patientTables[0]);
            return null;
        }
    }

    private static class UpdatePatientAsyncTask extends AsyncTask<PatientTable, Void, Void>{
        MyVetPathDao dao;

        UpdatePatientAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(PatientTable... patientTables) {
            dao.updatePatient(patientTables[0]);
            return null;
        }
    }

    private static class InsertPictureAsyncTask extends AsyncTask<PictureTable, Void, Void>{
        MyVetPathDao dao;

        InsertPictureAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(PictureTable... pictureTables) {
            dao.insertPicture(pictureTables[0]);
            return null;
        }
    }

    private static class DeletePictureAsyncTask extends AsyncTask<PictureTable, Void, Void>{
        MyVetPathDao dao;

        DeletePictureAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(PictureTable... pictureTables) {
            dao.deletePicture(pictureTables[0]);
            return null;
        }
    }

    private static class UpdatePictureAsyncTask extends AsyncTask<PictureTable, Void, Void>{
        MyVetPathDao dao;

        UpdatePictureAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(PictureTable... pictureTables) {
            dao.updatePicture(pictureTables[0]);
            return null;
        }
    }

    private static class InsertReplyAsyncTask extends AsyncTask<ReplyTable, Void, Void>{
        MyVetPathDao dao;

        InsertReplyAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(ReplyTable... replyTables) {
            dao.insertReply(replyTables[0]);
            return null;
        }
    }

    private static class InsertReportAsyncTask extends AsyncTask<ReportTable, Void, Void>{
        MyVetPathDao dao;

        InsertReportAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(ReportTable... reportTables) {
            dao.insertReport(reportTables[0]);
            return null;
        }
    }

    private static class UpdateReportAsyncTask extends AsyncTask<ReportTable, Void, Void>{
        MyVetPathDao dao;

        UpdateReportAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(ReportTable... reportTables) {
            dao.updateReport(reportTables[0]);
            return null;
        }
    }

    private static class InsertSampleAsyncTask extends AsyncTask<SampleTable, Void, Void>{
        MyVetPathDao dao;

        InsertSampleAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(SampleTable... sampleTables) {
            dao.insertSample(sampleTables[0]);
            return null;
        }
    }

    private static class DeleteSampleAsyncTask extends AsyncTask<SampleTable, Void, Void>{
        MyVetPathDao dao;

        DeleteSampleAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(SampleTable... sampleTables) {
            dao.deleteSample(sampleTables[0]);
            return null;
        }
    }

    private static class UpdateSampleAsyncTask extends AsyncTask<SampleTable, Void, Void>{
        MyVetPathDao dao;

        UpdateSampleAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(SampleTable... sampleTables) {
            dao.updateSample(sampleTables[0]);
            return null;
        }
    }

    private static class InsertSubmissionAsyncTask extends AsyncTask<SubmissionTable, Void, Long>{
        MyVetPathDao dao;
        OnSubmissionInserted submissionInserted;

        InsertSubmissionAsyncTask(MyVetPathDao myVetPathDao, OnSubmissionInserted inserted){
            dao = myVetPathDao;
            submissionInserted = inserted;
        }

        @Override
        protected Long doInBackground(SubmissionTable... submissionTables) {
            return dao.insertSubmission(submissionTables[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            submissionInserted.onSubmissionInserted(aLong);
            Log.d("Local Repo", "Insert Sub Async task id: " + Long.toString(aLong));
        }
    }

    private static class DeleteSubmissionAsyncTask extends AsyncTask<SubmissionTable, Void, Void>{
        MyVetPathDao dao;

        DeleteSubmissionAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(SubmissionTable... submissionTables) {
            dao.deleteSubmission(submissionTables[0]);
            return null;
        }
    }

    private static class UpdateSubmissionAsyncTask extends AsyncTask<SubmissionTable, Void, Void>{
        MyVetPathDao dao;

        UpdateSubmissionAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(SubmissionTable... submissionTables) {
            dao.updateSubmission(submissionTables[0]);
            return null;
        }
    }

    private static class InsertUserAsyncTask extends AsyncTask<UserTable, Void, Void>{
        MyVetPathDao dao;

        InsertUserAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(UserTable... userTables) {
            dao.insertUser(userTables[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<UserTable, Void, Void>{
        MyVetPathDao dao;

        DeleteUserAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(UserTable... userTables) {
            dao.deleteUser(userTables[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<UserTable, Void, Void>{
        MyVetPathDao dao;

        UpdateUserAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(UserTable... userTables) {
            dao.updateUser(userTables[0]);
            return null;
        }
    }
}
