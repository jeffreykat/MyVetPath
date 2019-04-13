package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "sample_table")
public class SampleTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int Sample_ID;

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "Master_ID", childColumns = "Master_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public long Master_ID;

    public String LocationOfSample;
    public int NumberOfSample;
    public long SampleCollectionDate;
    public String NameOfSample;
}
