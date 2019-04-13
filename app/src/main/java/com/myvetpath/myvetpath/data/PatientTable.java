package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "patient_table")
public class PatientTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int Patient_ID;

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "Master_ID", childColumns = "Master_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public long Master_ID;

    public int Euthanized; //0 means no 1 means yes
    public String Sex;
    public String Species;
    public String PatientName;
    public long DateOfBirth;
    public long DateOfDeath;
}
