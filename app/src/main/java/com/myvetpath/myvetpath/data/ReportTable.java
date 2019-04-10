package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "report_table")
public class ReportTable implements Serializable {

    @PrimaryKey
    @ForeignKey(entity = UserTable.class, parentColumns = "user_ID", childColumns = "user_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public int user_ID; //The pathologist responsible

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "internal_ID", childColumns = "internal_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public int internal_ID; //The submission

    public String submissionReview; //initial pathologist review
    public String conclusion; //final pathologist review
    public long dateClosed; //if filled, case closed
    public long reportDate; //date report is made
    public String attachments;
}
