package com.myvetpath.myvetpath;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

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

    public MyVetPathViewModel(@NonNull Application application) {
        super(application);
        repo = new LocalRepository(application);
    }

    public void insertGroup(GroupTable groupTable){repo.insertGroup(groupTable);}

    public void deleteGroup(GroupTable groupTable){repo.deleteGroup(groupTable);}

    public void updateGroup(GroupTable groupTable){repo.updateGroup(groupTable);}

    public LiveData<List<GroupTable>> getGroups(){return repo.getGroups();}

    public LiveData<GroupTable> getGroupByName(String name){return repo.getGroupByName(name);}

    public LiveData<GroupTable> getGroupByID(int id){return repo.getGroupByID(id);}

    public void insertPatient(PatientTable patientTable){repo.insertPatient(patientTable);}

    public void deletePatient(PatientTable patientTable){repo.deletePatient(patientTable);}

    public void updatePatient(PatientTable patientTable){repo.updatePatient(patientTable);}

    public LiveData<PatientTable> getPatientByID(long id){return repo.getPatientByID(id);}

    public void insertPicture(PictureTable pictureTable){repo.insertPicture(pictureTable);}

    public void deletePicture(PictureTable pictureTable){repo.deletePicture(pictureTable);}

    public void updatePicture(PictureTable pictureTable){repo.updatePicture(pictureTable);}

    public LiveData<List<PictureTable>> getPicturesByID(long id){return repo.getPicturesByID(id);}

    public LiveData<PictureTable> getPictureByTitle(String title){return repo.getPictureByTitle(title);}

    public void insertReply(ReplyTable replyTable){repo.insertReply(replyTable);}

    public LiveData<List<ReplyTable>> getRepliesByID(long id){return repo.getRepliesByID(id);}

    public LiveData<List<ReplyTable>> getRepliesBySender(int senderID){return repo.getRepliesBySender(senderID);}

    public void insertReport(ReportTable reportTable){repo.insertReport(reportTable);}

    public void updateReport(ReportTable reportTable){repo.updateReport(reportTable);}

    public LiveData<ReportTable> getReportByID(long id){return repo.getReportByID(id);}

    public void insertSample(SampleTable sampleTable){repo.insertSample(sampleTable);}

    public void deleteSample(SampleTable sampleTable){repo.deleteSample(sampleTable);}

    public void updateSample(SampleTable sampleTable){repo.updateSample(sampleTable);}

    public LiveData<List<SampleTable>> getSamplesByID(long id){return repo.getSamplesByID(id);}

    public LiveData<SampleTable> getSampleByName(String name){return repo.getSampleByName(name);}

    public long insertSubmission(SubmissionTable submissionTable){return repo.insertSubmission(submissionTable);}

    public void deleteSubmission(SubmissionTable submissionTable){repo.deleteSubmission(submissionTable);}

    public void updateSubmission(SubmissionTable submissionTable){repo.updateSubmission(submissionTable);}

    public LiveData<List<SubmissionTable>> getSubmissions(){return repo.getSubmissions();}

    public LiveData<List<SubmissionTable>> getDrafts(){return repo.getDrafts();}

    public LiveData<SubmissionTable> getSubmissionByTitle(String title){return repo.getSubmissionByTitle(title);}

    public LiveData<SubmissionTable> getSubmissionByID(long id){return repo.getSubmissionByID(id);}

    public void insertUser(UserTable userTable){repo.insertUser(userTable);}

    public void deleteUser(UserTable userTable){repo.deleteUser(userTable);}

    public void updateUser(UserTable userTable){repo.updateUser(userTable);}

    public LiveData<List<UserTable>> getUsers(){return repo.getUsers();}

    public LiveData<UserTable> getUserByUsername(String username){return repo.getUserByUsername(username);}
}
