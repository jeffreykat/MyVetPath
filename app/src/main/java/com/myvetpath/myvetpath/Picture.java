package com.myvetpath.myvetpath;

import java.sql.Date;

public class Picture { //remaned to picture so we don't get it confused with Image object from android
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
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
            "INTEGER PRIMARY KEY," + COLUMN_IMAGETITLE + "TEXT )";


    //Object Fields
    //Commented out variables in order to reduce the amount of testing needed.
    private int imageID;
    private int internalID;
    //private Bitmap imageLink; May be able to use the object image?
    private String imageTitle;
    //private string latitude; not sure about what type to make latitude and longitude
    //private string longitude;
    //private Date dateTaken;



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

    //Accessors
    public int getImageID(){
        return this.imageID;
    }
    public String getImageTitle(){
        return this.imageTitle;
    }

}




