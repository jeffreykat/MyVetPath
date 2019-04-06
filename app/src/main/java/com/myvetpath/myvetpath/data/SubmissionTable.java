package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "submission_table")
public class SubmissionTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int internal_ID; //Primary key for the internal database.

    @ForeignKey(entity = GroupTable.class, parentColumns = "groupName", childColumns = "groupName", onUpdate = CASCADE, onDelete = CASCADE)
    public String groupName; //name of group

    @ForeignKey(entity = UserTable.class, parentColumns = "user_ID", childColumns = "user_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public String user_ID; //username of who created submission

    public int caseID; //An identifier currently used by Pathologist
    public int masterID; //Primary key for the Server database

    public int statusFlag; // Stage of the submission 0 = draft, 1 = submitted, 2 = received by server
    public String title; //user created title
    public String comment; //Contains the comment of a submission.
    public long dateOfCreation; // Date the submission was created
    public long dateOfSubmission; // Date the submission was sent to the server
    public long dateOfCompletion; //Date the submission is closed and complete
}
