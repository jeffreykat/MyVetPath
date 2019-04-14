package com.myvetpath.myvetpath.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class UserTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int User_ID;

    public String Username;
    public String Password;
    public int Authorized; //flag variable 0 means no 1 means yes.
    public String FirstName;
    public String LastName;
    public int PhoneNumber;
    public String Email;
    public String Position;
    public int SecurityLevel; //0 is basic client, 1 is pathologist
}
