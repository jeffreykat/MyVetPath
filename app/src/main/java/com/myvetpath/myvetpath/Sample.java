package com.myvetpath.myvetpath;

import java.sql.Date;

public class Sample {
    //Table Fields
    public static final String TABLE_NAME = "Sample";
    public static final String COLUMN_ID = "SampleID";
    public static final String COLUMN_LOCATIONOFSAMPLE = "LocationOfSample";
    public static final String COLUMN_NUMBEROFSAMPLE = "NumberOfSamples";
    public static final String COLUMN_SAMPLECOLLECTIONDATE = "SampleCollectionDate";
    public static final String COLUMN_NAMEOFSAMPLE = "NameOfSample";

    //Create Table String
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
            "INTEGER PRIMARY KEY," + COLUMN_NAMEOFSAMPLE + "TEXT )";


    //Object Fields
    //Commented out variables in order to reduce the amount of testing needed.
    private int sampleID;
    //private String locationOfSample;
    //private int NumberOfSamples;
    //private Date SampleCollectionDate;
    private String NameOfSample;

    //Constructors
    public Sample() {}
    public Sample(int id, String sampleName) {
        this.sampleID = id;
        this.NameOfSample = sampleName;
    }
    //Modifiers
    public void setSamplelID(int newInternal){
        this.sampleID = newInternal;
    }
    public void setName(String newName){
        this.NameOfSample = newName;
    }

    //Accessors
    public int getSameplID(){
        return this.sampleID;
    }
    public String getNameOfSample(){
        return this.NameOfSample;
    }

}




