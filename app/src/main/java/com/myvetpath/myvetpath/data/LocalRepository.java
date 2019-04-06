package com.myvetpath.myvetpath.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class LocalRepository {
    private static String TAG = LocalRepository.class.getSimpleName();

    private MyVetPathDao dao;

    public LocalRepository(Application application){}

    public void insertGroup(GroupTable groupTable){new InsertGroupAsyncTask(dao).execute(groupTable);}

    public void deleteGroup(GroupTable groupTable){new DeleteGroupAsyncTask(dao).execute(groupTable);}

    public void updateGroup(GroupTable groupTable){new UpdateGroupAsyncTask(dao).execute(groupTable);}

    public LiveData<List<GroupTable>> getGroups(){
        return dao.getGroups();
    }

    public void insertPatient(PatientTable patientTable){new InsertPatientAsyncTask(dao).execute(patientTable);}

    public void deletePatient(PatientTable patientTable){new DeletePatientAsyncTask(dao).execute(patientTable);}

    public void updatePatient(PatientTable patientTable){new UpdatePatientAsyncTask(dao).execute(patientTable);}

    public LiveData<PatientTable> getPatientByID(int id){
        return dao.getPatientByID(id);
    }

    public void insertPicture(PictureTable pictureTable){new InsertPictureAsyncTask(dao).execute(pictureTable);}

    public void deletePicture(PictureTable pictureTable){new DeletePictureAsyncTask(dao).execute(pictureTable);}

    public void updatePicture(PictureTable pictureTable){new UpdatePictureAsyncTask(dao).execute(pictureTable);}

    public LiveData<List<PictureTable>> getPicturesByID(int id){
        return dao.getPicturesByID(id);
    }

    public LiveData<PictureTable> getPictureByTitle(String title){
        return dao.getPictureByTitle(title);
    }

    public void insertReply(ReplyTable replyTable){new InsertReplyAsyncTask(dao).execute(replyTable);}

    public LiveData<List<ReplyTable>> getRepliesByID(int id){
        return dao.getRepliesByID(id);
    }

    public LiveData<List<ReplyTable>> getRepliesBySender(int id){
        return dao.getRepliesBySender(id);
    }

    public void insertReport(ReportTable reportTable){}

    public void updateReport(ReportTable reportTable){}

    public LiveData<ReportTable> getReportByID(int id){
        return dao.getReportByID(id);
    }

    public void insertSample(SampleTable sampleTable){}

    public void deleteSample(SampleTable sampleTable){}

    public void updateSample(SampleTable sampleTable){}

    public LiveData<List<SampleTable>> getSamplesByID(int id){
        return dao.getSamplesByID(id);
    }

    public LiveData<SampleTable> getSampleByName(String name){
        return dao.getSampleByName(name);
    }

    public void insertSubmission(SubmissionTable submissionTable){}

    public void deleteSubmission(SubmissionTable submissionTable){}

    public void updateSubmission(SubmissionTable submissionTable){}

    public LiveData<List<SubmissionTable>> getSubmissions(){
        return dao.getSubmissions();
    }

    public LiveData<List<SubmissionTable>> getDrafts(){
        return dao.getDrafts();
    }

    public LiveData<SubmissionTable> getSubmissionByTitle(String title){
        return dao.getSubmissionByTitle(title);
    }

    public LiveData<SubmissionTable> getSubmissionByID(int id){
        return dao.getSubmissionByID(id);
    }

    public void insertUser(UserTable userTable){}

    public void deleteUser(UserTable userTable){}

    public void updateUser(UserTable userTable){}

    public LiveData<List<UserTable>> getUsers(){
        return dao.getUsers();
    }

    public LiveData<UserTable> getUserByUsername(String username){
        return dao.getUserByUsername(username);
    }

    private static class InsertGroupAsyncTask extends AsyncTask<GroupTable, Void, Void>{
        MyVetPathDao dao;

        InsertGroupAsyncTask(MyVetPathDao myVetPathDao){
            dao = myVetPathDao;
        }

        @Override
        protected Void doInBackground(GroupTable... groupTables) {
            dao.insertGroup(groupTables[0]);
            return null;
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
}
