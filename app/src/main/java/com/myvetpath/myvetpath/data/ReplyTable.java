package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "reply_table")
public class ReplyTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int reply_ID; //internal id

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "internal_ID", childColumns = "internal_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public int internal_ID;

    public int senderID;
    public int receiverID;
    public String message;
    public long dateOfMessage;
}
