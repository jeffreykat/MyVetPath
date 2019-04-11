package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "report_table")
public class ReportTable implements Serializable {

    @PrimaryKey
    @ForeignKey(entity = UserTable.class, parentColumns = "User_ID", childColumns = "User_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public int User_ID; //The pathologist responsible

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "Master_ID", childColumns = "Master_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public long Master_ID; //The submission

    public String SubmissionReview; //initial pathologist review
    public String Conclusion; //final pathologist review
    public long DateClosed; //if filled, case closed
    public long ReportDate; //date report is made
    public String Attachments;
}
