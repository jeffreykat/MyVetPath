package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class UserTable implements Serializable {

    @PrimaryKey
    public int user_ID;

    public String username;
    public String password;
    public int authorized; //flag variable 0 means no 1 means yes.
    public String firstName;
    public String lastName;
    public int phoneNumber;
    public String email;
    public String position;
    public int securityLevel; //0 is basic client, 1 is pathologist
}
