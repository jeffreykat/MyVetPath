package com.myvetpath.myvetpath;

import java.sql.Date;

public class SickElement {
    //Table Fields
    public static final String TABLE_NAME = "SickElement";
    public static final String COLUMN_ID = "SickElementID";
    public static final String COLUMN_INTERNAL = "InternalID";
    public static final String COLUMN_EUTHANIZED = "Euthanized";
    public static final String COLUMN_SEX = "Sex";
    public static final String COLUMN_SPECIES = "Species";
    public static final String COLUMN_SICKELEMENTNAME = "SickElementName";
    public static final String COLUMN_DATEOFBIRTH = "DateOfBirth";
    public static final String COLUMN_DATEOFDEATH = "DateOfDeath";

    //Create Table String
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_INTERNAL + " INTEGER, "
            + COLUMN_EUTHANIZED + " INTEGER, "
            + COLUMN_SEX + " TEXT, "
            + COLUMN_SPECIES + " TEXT, "
            + COLUMN_SICKELEMENTNAME + " TEXT, "
            + COLUMN_DATEOFBIRTH + " BIGINT, "
            + COLUMN_DATEOFDEATH + " BIGINT"
            + " )";


    //Object Fields

    private int sickID;
    private int internalID;
    private int Euthanized; //0 means no 1 means yes //will change once double check lite boolean
    private String sex;
    private String species;
    private String sickName;
    private long dateOfBirth;
    private long dateOfDeath;

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
    public void setInternalID (int newInternal){ this.internalID = newInternal;}
    public void setEuthanized (int newEuthan){this.Euthanized = newEuthan;}
    public void setSex (String newSex){this.sex = newSex;}
    public void setSpecies (String newSpecies){this.species = newSpecies;}
    public void setDateOfBirth (long newBirth){this.dateOfBirth = newBirth;}
    public void setDateOfDeath (long newDeath){this.dateOfDeath = newDeath;}
    //Accessors
    public int getSickID(){
        return this.sickID;
    }
    public String getNameOfSickElement(){
        return this.sickName;
    }
    public int getInternalID(){return this.internalID;}
    public int getEuthanized(){return this.Euthanized;}
    public String getSex(){return this.sex;}
    public String getSpecies(){return this.species;}
    public long getDateOfDeath(){return this.dateOfDeath;}
    public long getDateOfBirth(){return this.dateOfBirth;}
}




