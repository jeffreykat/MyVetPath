package com.myvetpath.myvetpath.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MyVetPathDao {

    @Insert
    void insertGroup(GroupTable groupTable);

    @Delete
    void deleteGroup(GroupTable groupTable);

    @Update
    void updateGroup(GroupTable groupTable);

    @Query("SELECT * FROM group_table")
    LiveData<List<GroupTable>> getGroups();

    @Insert
    void insertPatient(PatientTable patientTable);

    @Delete
    void deletePatient(PatientTable patientTable);

    @Update
    void updatePatient(PatientTable patientTable);

    @Query("SELECT * FROM patient_table WHERE internal_ID = :id LIMIT 1")
    LiveData<PatientTable> getPatientByID(int id);

    @Insert
    void insertPicture(PictureTable pictureTable);

    @Delete
    void deletePicture(PictureTable pictureTable);

    @Update
    void updatePicture(PictureTable pictureTable);

    @Query("SELECT * FROM picture_table WHERE internal_ID = :id")
    LiveData<List<PictureTable>> getPicturesByID(int id);

    @Query("SELECT * FROM picture_table WHERE imageTitle = :title LIMIT 1")
    LiveData<PictureTable> getPictureByTitle(String title);

    @Insert
    void insertReply(ReplyTable replyTable);

    @Query("SELECT * FROM reply_table WHERE internal_ID = :id ORDER BY dateOfMessage")
    LiveData<List<ReplyTable>> getRepliesByID(int id);

    @Query("SELECT * FROM reply_table WHERE senderID = :id ORDER BY dateOfMessage")
    LiveData<List<ReplyTable>> getRepliesBySender(int id);

    @Insert
    void insertReport(ReportTable reportTable);

    @Delete
    void deleteReport(ReportTable reportTable);

    @Update
    void updateReport(ReportTable reportTable);

    @Query("SELECT * FROM report_table WHERE internal_ID = :id")
    LiveData<ReportTable> getReportByID(int id);

    @Insert
    void insertSample(SampleTable sampleTable);

    @Delete
    void deleteSample(SampleTable sampleTable);

    @Update
    void updateSample(SampleTable sampleTable);

    @Query("SELECT * FROM sample_table WHERE internal_ID = :id ORDER BY sampleCollectionDate")
    LiveData<List<SampleTable>> getSamplesByID(int id);

    @Query("SELECT * FROM sample_table WHERE nameOfSample = :name")
    LiveData<SampleTable> getSampleByName(String name);

    @Insert
    void insertSubmission(SubmissionTable submissionTable);

    @Delete
    void deleteSubmission(SubmissionTable submissionTable);

    @Update
    void updateSubmission(SubmissionTable submissionTable);

    @Query("SELECT * FROM submission_table WHERE statusFlag = 1 ORDER BY dateOfCreation")
    LiveData<List<SubmissionTable>> getSubmissions();

    @Query("SELECT * FROM submission_table WHERE statusFlag = 0 ORDER BY dateOfCreation")
    LiveData<List<SubmissionTable>> getDrafts();

    @Query("SELECT * FROM submission_table WHERE title = :title LIMIT 1")
    LiveData<SubmissionTable> getSubmissionByTitle(String title);

    @Query("SELECT * FROM submission_table WHERE internal_ID = :id LIMIT 1")
    LiveData<SubmissionTable> getSubmissionByID(int id);

    @Insert
    void insertUser(UserTable userTable);

    @Delete
    void deleteUser(UserTable userTable);

    @Update
    void updateUser(UserTable userTable);

    @Query("SELECT * FROM user_table")
    LiveData<List<UserTable>> getUsers();

    @Query("SELECT * FROM user_table WHERE username = :username")
    LiveData<UserTable> getUserByUsername(String username);
}
