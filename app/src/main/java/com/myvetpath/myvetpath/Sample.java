package com.myvetpath.myvetpath;

import java.io.Serializable;

public class Sample implements Serializable {
    //Table Fields
    public static final String TABLE_NAME = "Sample";
    public static final String COLUMN_ID = "SampleID";
    public static final String COLUMN_LOCATIONOFSAMPLE = "LocationOfSample";
    public static final String COLUMN_NUMBEROFSAMPLE = "NumberOfSamples";
    public static final String COLUMN_SAMPLECOLLECTIONDATE = "SampleCollectionDate";
    public static final String COLUMN_NAMEOFSAMPLE = "NameOfSample";
    public static final String COLUMN_INTERNAL = "InternalID";

    //Create Table String
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAMEOFSAMPLE + " TEXT, "
            + COLUMN_INTERNAL + " INTEGER, "
            + COLUMN_LOCATIONOFSAMPLE + " TEXT, "
            + COLUMN_NUMBEROFSAMPLE + " INTEGER, "
            + COLUMN_SAMPLECOLLECTIONDATE + " BIGINT"
            + " )";



    //Object Fields
    //Commented out variables in order to reduce the amount of testing needed.
    private int internalID;
    private String locationOfSample;
    private int NumberOfSamples;
    private long SampleCollectionDate;
    private String NameOfSample;

    //Constructors
    public Sample() {}
    public Sample(int id, String sampleName) {
        this.internalID = id;
        this.NameOfSample = sampleName;
    }
    //Modifiers
    public void setSamplelID(int newInternal){
        this.internalID = newInternal;
    }
    public void setName(String newName){
        this.NameOfSample = newName;
    }
    public void setLocation(String newLocation) {this.locationOfSample = newLocation;}
    public void setNumberOfSamples(int newNumber){this.NumberOfSamples = newNumber;}
    public void setSampleCollectionDate(long newDate){this.SampleCollectionDate = newDate;}
    //Accessors
    public int getInternalID(){
        return this.internalID;
    }
    public String getNameOfSample(){
        return this.NameOfSample;
    }
    public String getLocation(){return this.locationOfSample;}
    public int getNumberOfSamples(){return this.NumberOfSamples;}
    public long getSampleCollectionDate(){return this.SampleCollectionDate;}


}




