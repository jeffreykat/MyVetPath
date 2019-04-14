package com.myvetpath.myvetpath;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import com.myvetpath.myvetpath.data.GroupTable;
import com.myvetpath.myvetpath.data.LocalRepository;
import com.myvetpath.myvetpath.data.PatientTable;
import com.myvetpath.myvetpath.data.PictureTable;
import com.myvetpath.myvetpath.data.ReplyTable;
import com.myvetpath.myvetpath.data.ReportTable;
import com.myvetpath.myvetpath.data.SampleTable;
import com.myvetpath.myvetpath.data.SubmissionTable;
import com.myvetpath.myvetpath.data.UserTable;

import java.util.List;

public class MyVetPathViewModel extends AndroidViewModel {
    private LocalRepository repo;
    MutableLiveData<String> groupInput = new MutableLiveData<>();
    MutableLiveData<Long> patientInputID = new MutableLiveData<>();
    MutableLiveData<Long> pictureInputID = new MutableLiveData<>();
    MutableLiveData<Long> replyInputID = new MutableLiveData<>();
    MutableLiveData<Long> reportInputID = new MutableLiveData<>();
    MutableLiveData<Long> sampleInputID = new MutableLiveData<>();
    MutableLiveData<Long> submissionInputID = new MutableLiveData<>();
    MutableLiveData<Integer> userInputID = new MutableLiveData<>();
    MutableLiveData<String> userInputString = new MutableLiveData<>();

    public MyVetPathViewModel(@NonNull Application application) {
        super(application);
        repo = new LocalRepository(application);
    }

    public long insertGroup(GroupTable groupTable){return repo.insertGroup(groupTable);}

    public void deleteGroup(GroupTable groupTable){repo.deleteGroup(groupTable);}

    public void updateGroup(GroupTable groupTable){repo.updateGroup(groupTable);}

    public LiveData<List<GroupTable>> getGroups(){return repo.getGroups();}

    public LiveData<GroupTable> group = Transformations.switchMap(groupInput, new Function<String, LiveData<GroupTable>>() {
        @Override
        public LiveData<GroupTable> apply(String input) {
            return repo.getGroupByName(input);
        }
    });

    public LiveData<GroupTable> getGroupByName(String name){
        groupInput.setValue(name);
        return group;
    }

    public LiveData<GroupTable> getGroupByID(long id){return repo.getGroupByID(id);}

    public void insertPatient(PatientTable patientTable){repo.insertPatient(patientTable);}

    public void deletePatient(PatientTable patientTable){repo.deletePatient(patientTable);}

    public void updatePatient(PatientTable patientTable){repo.updatePatient(patientTable);}

    public LiveData<PatientTable> patient = Transformations.switchMap(patientInputID, new Function<Long, LiveData<PatientTable>>() {
        @Override
        public LiveData<PatientTable> apply(Long input) {
            return repo.getPatientByID(input);
        }
    });

    public LiveData<PatientTable> getPatientByID(long id){
        patientInputID.setValue(id);
        return patient;
    }

    public void insertPicture(PictureTable pictureTable){repo.insertPicture(pictureTable);}

    public void deletePicture(PictureTable pictureTable){repo.deletePicture(pictureTable);}

    public void updatePicture(PictureTable pictureTable){repo.updatePicture(pictureTable);}

    LiveData<List<PictureTable>> pictureList = Transformations.switchMap(pictureInputID, new Function<Long, LiveData<List<PictureTable>>>() {
        @Override
        public LiveData<List<PictureTable>> apply(Long input) {
            return repo.getPicturesByID(input);
        }
    });

    public LiveData<List<PictureTable>> getPicturesByID(long id){
        pictureInputID.setValue(id);
        return pictureList;
    }

    public LiveData<PictureTable> getPictureByTitle(String title){return repo.getPictureByTitle(title);}

    public void insertReply(ReplyTable replyTable){repo.insertReply(replyTable);}

    public LiveData<List<ReplyTable>> getRepliesByID(long id){return repo.getRepliesByID(id);}

    public LiveData<List<ReplyTable>> getRepliesBySender(int senderID){return repo.getRepliesBySender(senderID);}

    public void insertReport(ReportTable reportTable){repo.insertReport(reportTable);}

    public void updateReport(ReportTable reportTable){repo.updateReport(reportTable);}

    public LiveData<ReportTable> report = Transformations.switchMap(reportInputID, new Function<Long, LiveData<ReportTable>>() {
        @Override
        public LiveData<ReportTable> apply(Long input) {
            return repo.getReportByID(input);
        }
    });

    public LiveData<ReportTable> getReportByID(long id){
        reportInputID.setValue(id);
        return report;
    }

    public void insertSample(SampleTable sampleTable){repo.insertSample(sampleTable);}

    public void deleteSample(SampleTable sampleTable){repo.deleteSample(sampleTable);}

    public void updateSample(SampleTable sampleTable){repo.updateSample(sampleTable);}

    public LiveData<List<SampleTable>> sampleList = Transformations.switchMap(sampleInputID, new Function<Long, LiveData<List<SampleTable>>>() {
        @Override
        public LiveData<List<SampleTable>> apply(Long input) {
            return repo.getSamplesByID(input);
        }
    });

    public LiveData<List<SampleTable>> getSamplesByID(long id){
        sampleInputID.setValue(id);
        return sampleList;
    }

    public LiveData<SampleTable> getSampleByName(String name){return repo.getSampleByName(name);}

    public long insertSubmission(SubmissionTable submissionTable, OnSubmissionInserted listener){return repo.insertSubmission(submissionTable, listener);}

    public void deleteSubmission(SubmissionTable submissionTable){repo.deleteSubmission(submissionTable);}

    public void updateSubmission(SubmissionTable submissionTable){repo.updateSubmission(submissionTable);}

    public LiveData<List<SubmissionTable>> getSubmissions(){return repo.getSubmissions();}

    public LiveData<List<SubmissionTable>> getDrafts(){return repo.getDrafts();}

    public LiveData<SubmissionTable> submission = Transformations.switchMap(submissionInputID, new Function<Long, LiveData<SubmissionTable>>() {
        @Override
        public LiveData<SubmissionTable> apply(Long l) {
            return repo.getSubmissionByID(l);
        }
    });

    public LiveData<SubmissionTable> getSubmissionByTitle(String title){return repo.getSubmissionByTitle(title);}

    public void setSubmissionInputID(long id){
        Log.d("ViewModel", "setSubmission:");
        submissionInputID.setValue(id);
    }

    public LiveData<SubmissionTable> getSubmissionByID(){
        //submissionInputID.setValue(id);
        Log.d("ViewModel", "getSubmission:");
        return submission;
    }

    public void insertUser(UserTable userTable){repo.insertUser(userTable);}

    public void deleteUser(UserTable userTable){repo.deleteUser(userTable);}

    public void updateUser(UserTable userTable){repo.updateUser(userTable);}

    public LiveData<List<UserTable>> getUsers(){return repo.getUsers();}

    public LiveData<UserTable> user = Transformations.switchMap(userInputString, new Function<String, LiveData<UserTable>>() {
        @Override
        public LiveData<UserTable> apply(String input) {
            return repo.getUserByUsername(input);
        }
    });

    public LiveData<UserTable> getUserByUsername(String username){
        userInputString.setValue(username);
        return user;
    }
}
