package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "submission_table")
public class SubmissionTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long Master_ID; //Primary key for the internal database.

    @ForeignKey(entity = GroupTable.class, parentColumns = "Group_ID", childColumns = "Group_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public long Group_ID; //name of group

    @ForeignKey(entity = UserTable.class, parentColumns = "User_ID", childColumns = "User_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public int User_ID; //username of who created submission

    public int Case_ID; //An identifier currently used by

    public int StatusFlag; // Stage of the submission 0 = draft, 1 = submitted, 2 = received by server
    public String Title; //user created title
    public String UserComment; //Contains the comment of a submission.
    public long DateOfCreation; // Date the submission was created
    public long Submitted; // Date the submission was sent to the server
    public long ReportComplete; //Date the submission is closed and complete
}
