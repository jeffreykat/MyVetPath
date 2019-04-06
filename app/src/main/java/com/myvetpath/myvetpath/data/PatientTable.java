package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "patient_table")
public class PatientTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int patient_ID;

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "internal_ID", childColumns = "internal_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public int internal_ID;

    public int euthanized; //0 means no 1 means yes
    public String sex;
    public String species;
    public String patientName;
    public long dateOfBirth;
    public long dateOfDeath;
}
