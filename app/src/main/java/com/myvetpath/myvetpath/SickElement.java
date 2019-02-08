package com.myvetpath.myvetpath;

import java.sql.Date;

public class SickElement {
    //Table Fields
    public static final String TABLE_NAME = "SickElement";
    public static final String COLUMN_ID = "SickElementID";
    public static final String COLUMN_INTERNAL = "InternalID";
    public static final String COLUMN_EUTAHNIZED = "Euthanized";
    public static final String COLUMN_SEX = "Sex";
    public static final String COLUMN_SPECIES = "Species";
    public static final String COLUMN_SICKELEMENTNAME = "SickElementName";
    public static final String COLUMN_DATEOFBIRTH = "DateOfBirth";
    public static final String COLUMN_DATEOFDEATH = "DateOfDeath";

    //Create Table String
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
            "INTEGER PRIMARY KEY," + COLUMN_SICKELEMENTNAME + "TEXT )";


    //Object Fields
    //Commented out variables in order to reduce the amount of testing needed.
    private int sickID;
    private int internalID;
    private boolean Euthanized;
    private String sex;
    private String species;
    private String sickName;
    private Date dateOfBirth;
    private Date dateOfDeath;

    //Constructors
    public SickElement() {}
    public SickElement(int id, String SickElementName) {
        this.sickID = id;
        this.sickName = SickElementName;
    }
    //Modifiers
    public void setSickID(int newID){
        this.sickID = newID;
    }
    public void setName(String newName){
        this.sickName = newName;
    }

    //Accessors
    public int getSickID(){
        return this.sickID;
    }
    public String getNameOfSickElement(){
        return this.sickName;
    }

}




