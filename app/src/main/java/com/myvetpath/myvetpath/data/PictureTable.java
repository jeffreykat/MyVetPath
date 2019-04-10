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
    public int image_ID;

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "internal_ID", childColumns = "internal_ID", onUpdate = CASCADE, onDelete = CASCADE)
    public int internal_ID;

    public String imageTitle;
    public String latitude;
    public String longitude;
    public long dateTaken;
    public String picturePath;
}
