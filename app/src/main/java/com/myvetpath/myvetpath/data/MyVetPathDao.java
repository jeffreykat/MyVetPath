package com.myvetpath.myvetpath.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MyVetPathDao {

    @Insert
    long insertGroup(GroupTable groupTable);

    @Delete
    void deleteGroup(GroupTable groupTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGroup(GroupTable groupTable);

    @Query("SELECT * FROM group_table")
    LiveData<List<GroupTable>> getGroups();

    @Query("SELECT * FROM group_table WHERE GroupName = :name LIMIT 1")
    LiveData<GroupTable> getGroupByName(String name);

    @Query("SELECT * FROM group_table WHERE Group_ID = :id LIMIT 1")
    LiveData<GroupTable> getGroupByID(long id);

    @Insert
    void insertPatient(PatientTable patientTable);

    @Delete
    void deletePatient(PatientTable patientTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePatient(PatientTable patientTable);

    @Query("SELECT * FROM patient_table WHERE Master_ID = :id LIMIT 1")
    LiveData<PatientTable> getPatientByID(long id);

    @Insert
    void insertPicture(PictureTable pictureTable);

    @Delete
    void deletePicture(PictureTable pictureTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePicture(PictureTable pictureTable);

    @Query("SELECT * FROM picture_table WHERE Master_ID = :id")
    LiveData<List<PictureTable>> getPicturesByID(long id);

    @Query("SELECT * FROM picture_table WHERE Title = :title LIMIT 1")
    LiveData<PictureTable> getPictureByTitle(String title);

    @Insert
    void insertReply(ReplyTable replyTable);

    @Query("SELECT * FROM reply_table WHERE Master_ID = :id ORDER BY DateOfMessage DESC")
    LiveData<List<ReplyTable>> getRepliesByID(long id);

    @Query("SELECT * FROM reply_table WHERE Sender_ID = :id ORDER BY DateOfMessage DESC")
    LiveData<List<ReplyTable>> getRepliesBySender(int id);

    @Insert
    void insertReport(ReportTable reportTable);

    @Delete
    void deleteReport(ReportTable reportTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReport(ReportTable reportTable);

    @Query("SELECT * FROM report_table WHERE Master_ID = :id")
    LiveData<ReportTable> getReportByID(long id);

    @Insert
    void insertSample(SampleTable sampleTable);

    @Delete
    void deleteSample(SampleTable sampleTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSample(SampleTable sampleTable);

    @Query("SELECT * FROM sample_table WHERE Master_ID = :id ORDER BY SampleCollectionDate")
    LiveData<List<SampleTable>> getSamplesByID(long id);

    @Query("SELECT * FROM sample_table WHERE NameOfSample = :name")
    LiveData<SampleTable> getSampleByName(String name);

    @Insert
    long insertSubmission(SubmissionTable submissionTable);

    @Delete
    void deleteSubmission(SubmissionTable submissionTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSubmission(SubmissionTable submissionTable);

    @Query("SELECT * FROM submission_table WHERE StatusFlag = 1 ORDER BY DateOfCreation DESC")
    LiveData<List<SubmissionTable>> getSubmissions();

    @Query("SELECT * FROM submission_table WHERE StatusFlag = 0 ORDER BY DateOfCreation DESC")
    LiveData<List<SubmissionTable>> getDrafts();

    @Query("SELECT * FROM submission_table WHERE Title = :title LIMIT 1")
    LiveData<SubmissionTable> getSubmissionByTitle(String title);

    @Query("SELECT * FROM submission_table WHERE Master_ID = :id LIMIT 1")
    LiveData<SubmissionTable> getSubmissionByID(long id);

    @Insert()
    void insertUser(UserTable userTable);

    @Delete
    void deleteUser(UserTable userTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(UserTable userTable);

    @Query("SELECT * FROM user_table")
    LiveData<List<UserTable>> getUsers();

    @Query("SELECT * FROM user_table WHERE Username = :username")
    LiveData<UserTable> getUserByUsername(String username);
}
