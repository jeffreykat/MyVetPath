package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "sample_table")
public class SampleTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int sample_ID;

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "internal_ID", childColumns = "internal_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public int internal_ID;

    public String locationOfSample;
    public int numberOfSamples;
    public long sampleCollectionDate;
    public String nameOfSample;
}
