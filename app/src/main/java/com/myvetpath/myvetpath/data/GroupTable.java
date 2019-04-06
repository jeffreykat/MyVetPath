package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "group_table")
public class GroupTable implements Serializable {

    @PrimaryKey
    public String groupName;

    public long dateOfCreation;
}
