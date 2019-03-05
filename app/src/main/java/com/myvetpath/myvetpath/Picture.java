package com.myvetpath.myvetpath;

import java.io.Serializable;

public class Picture implements Serializable { //remaned to picture so we don't get it confused with Image object from android
    //Table Fields
    public static final String TABLE_NAME = "Image";
    public static final String COLUMN_ID = "ImageID";
    public static final String COLUMN_INTERNAL = "InternalID";
    public static final String COLUMN_IMAGELINK = "ImageLink";
    public static final String COLUMN_IMAGETITLE = "Title";
    public static final String COLUMN_LATITUDE = "Latitude";
    public static final String COLUMN_LONGITUDE = "Longitude";
    public static final String COLUMN_DATETAKEN = "DateTaken";



    //Create Table String
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_IMAGETITLE + " TEXT, "
            + COLUMN_INTERNAL + " INTEGER, "
            + COLUMN_IMAGELINK + " TEXT, " //will have to double check how this works
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, " //forgot what we where doing with these.
            + COLUMN_DATETAKEN + " BIGINT"
            + " )";


    //Object Fields
    private int imageID;
    private int internalID;
    private String imageTitle;
    private String latitude; //not sure about what type to make latitude and longitude
    private String longitude;
    private long dateTaken;
    private String picturePath;

    //Constructors
    public Picture() {}
    public Picture(int id, String newTitle) {
        this.imageID = id;
        this.imageTitle = newTitle;
    }
    //Modifiers
    public void setImageID(int newID){
        this.imageID = newID;
    }
    public void setImageTitle(String newTitle){
        this.imageTitle = newTitle;
    }
    public void setInternalID(int newInteranl) { this.internalID = newInteranl;}
    public void setLatitude(String newLat) {this.latitude = newLat;}
    public void setLongitude(String newLon) {this.longitude = newLon;}
    public void setDateTaken(long newDate) {this.dateTaken = newDate;}
    public void setPicturePath(String newLink){this.picturePath = newLink;}
    //Accessors
    public int getImageID(){
        return this.imageID;
    }
    public String getImageTitle(){
        return this.imageTitle;
    }
    public int getInternalID(){return this.internalID;}
    public String getLatitude(){return this.latitude;}
    public String getLongitude(){return this.longitude;}
    public long getDateTaken(){return this.dateTaken;}
    public String getPicturePath(){return this.picturePath;}
}




