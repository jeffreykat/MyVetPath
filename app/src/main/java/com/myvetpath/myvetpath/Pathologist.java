package com.myvetpath.myvetpath;

import java.sql.Date;

public class Pathologist {
    //Table Fields
    public static final String TABLE_NAME = "Pathologist";
    public static final String COLUMN_ID = "PathoID";
    public static final String COLUMN_POSITION = "Position";
    public static final String COLUMN_SECURITYLEVEL= "SecurityLevel";


    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    " INTEGER PRIMARY KEY, "
                    + COLUMN_POSITION + " TEXT, "
                    + COLUMN_SECURITYLEVEL + " INTEGER" + " )";

    //Object Fields
    private int pathoID;
    private String position;
    private int securitylevel;


    //Constructors
    public Pathologist() {}
    public Pathologist(int id, String newPosition, int newLevel) {
        this.pathoID = id;
        this.position = newPosition;
        this.securitylevel = newLevel;
    }
    //Modifiers
    public void setID(int newID){
        this.pathoID = newID;
    }
    public void setPosition(String newPosition){
        this.position = newPosition;
    }
    public void setSecuritylevel(int newLevel){
        this.securitylevel = newLevel;
    }

    //Accessors
    public int getPathoID(){
        return this.pathoID;
    }
    public String getPosition(){
        return this.position;
    }
    public int getSecuritylevel(){
        return this.securitylevel;
    }

}




