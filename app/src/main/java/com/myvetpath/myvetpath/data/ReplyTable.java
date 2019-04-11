package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "reply_table")
public class ReplyTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int Reply_ID; //internal id

    @ForeignKey(entity = SubmissionTable.class, parentColumns = "Master_ID", childColumns = "Master_ID", onDelete = CASCADE, onUpdate = CASCADE)
    public long Master_ID;

    public int Sender_ID;
    public int Receiver_ID;
    public String ContentsOfMessage;
    public long DateOfMessage;
}
