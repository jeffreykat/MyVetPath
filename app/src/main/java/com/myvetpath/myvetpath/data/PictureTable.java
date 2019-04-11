package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "picture_table")
public class PictureTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int Image_ID;

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "Master_ID", childColumns = "Master_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public long Master_ID;

    public String Title;
    public String Latitude;
    public String Longitude;
    public long DateTaken;
    public String ImagePath;
}
