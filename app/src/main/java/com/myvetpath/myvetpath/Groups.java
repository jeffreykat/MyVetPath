package com.myvetpath.myvetpath;

import java.sql.Date;

public class Groups {
    //Table Fields
    public static final String TABLE_NAME = "Groups";
    public static final String COLUMN_ID = "GroupID";
    public static final String COLUMN_NAME = "GroupName";
    public static final String COLUMN_DATEOFCREATION = "DateOfCreation";



    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    "INTEGER PRIMARY KEY, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_DATEOFCREATION + " BIGINT" + " )";

    //Object Fields
    private int groupID;
    private String groupName;
    private long dateOfCreation;


    //Constructors
    public Groups() {}
    public Groups(int id, String newName, long newDate) {
        this.groupID = id;
        this.groupName = newName;
        this.dateOfCreation = newDate;
    }
    //Modifiers
    public void setID(int newID){
        this.groupID = newID;
    }
    public void setGroupName(String newName){
        this.groupName = newName;
    }
    public void setDateOfCreation(long newDate){
        this.dateOfCreation = newDate;
    }

    //Accessors
    public int getGroupID(){
        return this.groupID;
    }
    public String getGroupName(){
        return this.groupName;
    }
    public long getDateOfCreation(){
        return this.dateOfCreation;
    }

}




