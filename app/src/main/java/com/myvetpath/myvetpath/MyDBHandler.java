package com.myvetpath.myvetpath;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myVetPath.db";
    private static MyDBHandler mInstance = null;
    private final Context context;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    //initialize the database
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Creates a database with the table Submission.
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Submission.CREATE_TABLE);
        //added below tables
        db.execSQL(Picture.CREATE_TABLE);
        db.execSQL(SickElement.CREATE_TABLE);
        db.execSQL(Sample.CREATE_TABLE);
        //added below tables
        db.execSQL(Client.CREATE_TABLE);
        db.execSQL(Groups.CREATE_TABLE);
        db.execSQL(Pathologist.CREATE_TABLE);
        db.execSQL(RepliesForASubmission.CREATE_TABLE);
        db.execSQL(Reply.CREATE_TABLE);
        db.execSQL(Report.CREATE_TABLE);
        db.execSQL(User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("onUpgrade", "Drop table");
        db.execSQL("DROP TABLE IF EXISTS '" + Submission.TABLE_NAME + "'");
        db.setVersion(newVersion);
        onCreate(db);
    }

    public void createTables(SQLiteDatabase db){
        db.execSQL(Submission.CREATE_TABLE);
        Log.v("tableCreate", Submission.CREATE_TABLE);
        //added below tables;
        db.execSQL(Picture.CREATE_TABLE);
        Log.v("tableCreatePicture", Picture.CREATE_TABLE);
        db.execSQL(SickElement.CREATE_TABLE);
        Log.v("tableCreateSickElement", SickElement.CREATE_TABLE);
        db.execSQL(Sample.CREATE_TABLE);
        Log.v("tableCreateSample", Sample.CREATE_TABLE);
        db.execSQL(Client.CREATE_TABLE);
        Log.v("tableCreateClient", Client.CREATE_TABLE);
        db.execSQL(Groups.CREATE_TABLE);
        Log.v("tableCreateGroup", Groups.CREATE_TABLE);
        db.execSQL(Pathologist.CREATE_TABLE);
        Log.v("tableCreatePathologist", Pathologist.CREATE_TABLE);
        db.execSQL(RepliesForASubmission.CREATE_TABLE);
        Log.v("tableCreateRFAS", RepliesForASubmission.CREATE_TABLE);
        db.execSQL(Reply.CREATE_TABLE);
        Log.v("tableCreateReply", Reply.CREATE_TABLE);
        db.execSQL(Report.CREATE_TABLE);
        Log.v("tableCreateReport", Report.CREATE_TABLE);
        db.execSQL(User.CREATE_TABLE);
        Log.v("tableCreateUser", User.CREATE_TABLE);
    }

    //Remove submission table
    //Remove the Picture table, SickElement, Sample
    public void dropTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + Client.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Groups.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Pathologist.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Submission.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Picture.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RepliesForASubmission.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Reply.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Report.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Sample.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SickElement.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public boolean doesTableExist(String tableName){
        String query = "SELECT DISTINCT tbl_name FROM sqlite_master where tbl_name = " + "'" + tableName + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if(cursor.getCount() > 0){
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    //This is to display all of the contents in a specific table
    //This is done inside a string.
    //Future will include a clause to only display tables for a user.
    public String selectAll(String tableName) {
        String result = "";
        String query = "Select * FROM " + tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            result += String.valueOf(result_0) + " " + result_1 +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    //adders This is to add the respective table into the database.
    //Id would be added in automatically
    public long addSubmission(Submission submission) {
        ContentValues values = new ContentValues();
        values.put(Submission.COLUMN_CASE_ID, submission.getCaseID());
        values.put(Submission.COLUMN_MASTER_ID, submission.getMasterID());
        values.put(Submission.COLUMN_TITLE, submission.getTitle());
        values.put(Submission.COLUMN_GROUP, submission.getGroup());
        values.put(Submission.COLUMN_DATE_CREATION, submission.getDateOfCreation());
        values.put(Submission.COLUMN_STATUS_FLAG, submission.getStatusFlag());
        values.put(Submission.COLUMN_COMMENT, submission.getComment());
        values.put(Submission.COLUMN_CLIENT_ID, submission.getClientID());
        SQLiteDatabase db = this.getWritableDatabase();
        long internalID = db.insert(Submission.TABLE_NAME, null, values);
        Log.d("SQLite Database", "addSubmission: " + submission.getTitle());
        db.close();
        return internalID;
    }

    //This adds the given sample into the db
    public void addSample(Sample sample) {
        ContentValues values = new ContentValues();
        values.put(Sample.COLUMN_NAMEOFSAMPLE, sample.getNameOfSample());
        values.put(Sample.COLUMN_LOCATIONOFSAMPLE, sample.getLocation());
        values.put(Sample.COLUMN_NUMBEROFSAMPLE, sample.getNumberOfSamples());
        values.put(Sample.COLUMN_SAMPLECOLLECTIONDATE, sample.getSampleCollectionDate());
        values.put(Sample.COLUMN_INTERNAL, sample.getInternalID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Sample.TABLE_NAME, null, values);
        db.close();
    }

    public void addSickElement(SickElement sickElement) {
        ContentValues values = new ContentValues();
        values.put(SickElement.COLUMN_INTERNAL, sickElement.getInternalID());
        values.put(SickElement.COLUMN_SICKELEMENTNAME, sickElement.getNameOfSickElement());
        values.put(SickElement.COLUMN_EUTHANIZED, sickElement.getEuthanized());
        values.put(SickElement.COLUMN_SEX, sickElement.getSex());
        values.put(SickElement.COLUMN_SPECIES, sickElement.getSpecies());
        values.put(SickElement.COLUMN_DATEOFBIRTH, sickElement.getDateOfBirth());
        values.put(SickElement.COLUMN_DATEOFDEATH, sickElement.getDateOfDeath());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(SickElement.TABLE_NAME, null, values);
        db.close();
    }

    public void addPicture(Picture picture) {
        ContentValues values = new ContentValues();
        values.put(Picture.COLUMN_IMAGETITLE, picture.getImageTitle());
        values.put(Picture.COLUMN_DATETAKEN, picture.getDateTaken());
        values.put(Picture.COLUMN_IMAGELINK, picture.getPicturePath());
        values.put(Picture.COLUMN_LATITUDE, picture.getLatitude());
        values.put(Picture.COLUMN_LONGITUDE, picture.getLongitude());
        values.put(Picture.COLUMN_INTERNAL, picture.getInternalID());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(picture.TABLE_NAME, null, values);
        db.close();
    }


    //Searches the database to to find a row based on a title.
    public Submission findSubmissionTitle(String nTitle) {
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_TITLE + " = " + "'" + nTitle + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Submission sub = new Submission();
        if (cursor.moveToFirst()) {
            sub.setInternalID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_ID)));
            sub.setCaseID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_CASE_ID)));
            sub.setMasterID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_MASTER_ID)));
            sub.setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
            sub.setGroup(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_GROUP)));
            sub.setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
            sub.setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
            sub.setComment(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_COMMENT)));
            sub.setClientID(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_CLIENT_ID)));
        } else {
            sub = null;
        }
        cursor.close();
        db.close();
        return sub;
    }

    //Searches the database for all samples related to a case's internal ID
    public ArrayList<Sample> findSamples(int internalID) {
        String query = "Select * FROM " + Sample.TABLE_NAME + " WHERE " + Sample.COLUMN_INTERNAL + " = " + "'" + internalID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Sample> samplesList = new ArrayList<Sample>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Sample tempSample = new Sample();
            tempSample.setSamplelID(cursor.getInt(cursor.getColumnIndex(Sample.COLUMN_INTERNAL)));
            tempSample.setName(cursor.getString(cursor.getColumnIndex(Sample.COLUMN_NAMEOFSAMPLE)));
            tempSample.setLocation(cursor.getString(cursor.getColumnIndex(Sample.COLUMN_LOCATIONOFSAMPLE)));
            tempSample.setNumberOfSamples(cursor.getInt(cursor.getColumnIndex(Sample.COLUMN_NUMBEROFSAMPLE)));
            tempSample.setSampleCollectionDate(cursor.getLong(cursor.getColumnIndex(Sample.COLUMN_SAMPLECOLLECTIONDATE)));

            samplesList.add(tempSample);
        }

        cursor.close();
        db.close();
        return samplesList;
    }

    //Searches the database for all pictures related to a case's internal ID
    public ArrayList<Picture> findPictures(int internalID) {
        String query = "Select * FROM " + Picture.TABLE_NAME + " WHERE " + Picture.COLUMN_INTERNAL + " = " + "'" + internalID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Picture> pictures = new ArrayList<Picture>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Picture tempPic = new Picture();
            tempPic.setInternalID(cursor.getInt(cursor.getColumnIndex(Picture.COLUMN_INTERNAL)));
            tempPic.setPicturePath(cursor.getString(cursor.getColumnIndex(Picture.COLUMN_IMAGELINK)));
            tempPic.setLongitude(cursor.getString(cursor.getColumnIndex(Picture.COLUMN_LONGITUDE)));
            tempPic.setLatitude(cursor.getString(cursor.getColumnIndex(Picture.COLUMN_LATITUDE)));
            tempPic.setImageID(cursor.getInt(cursor.getColumnIndex(Picture.COLUMN_ID)));
            tempPic.setImageTitle(cursor.getString(cursor.getColumnIndex(Picture.COLUMN_IMAGETITLE)));
            tempPic.setDateTaken(cursor.getLong(cursor.getColumnIndex(Picture.COLUMN_DATETAKEN)));

            pictures.add(tempPic);
        }

        cursor.close();
        db.close();
        return pictures;
    }



    //Searches the database to find a row based on the id.
    public Submission findSubmissionID(int id) {
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Submission sub = new Submission();
        if (cursor.moveToFirst()) {
            sub.setInternalID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_ID)));
            sub.setCaseID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_CASE_ID)));
            sub.setMasterID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_MASTER_ID)));
            sub.setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
            sub.setGroup(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_GROUP)));
            sub.setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
            sub.setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
            sub.setComment(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_COMMENT)));
            sub.setClientID(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_CLIENT_ID)));
        }
        cursor.close();
        db.close();
        return sub;
    }

    //Deletes a row based on the id of a table.
    public boolean deleteSubmission(int ID) {
        boolean result = false;
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_ID + " = '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Submission sub = new Submission();
        if (cursor.moveToFirst()) {
            sub.setInternalID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_ID))));
            db.delete(Submission.TABLE_NAME, Submission.COLUMN_ID + "=?",
                    new String[] {
                            String.valueOf(sub.getInternalID())
                    });
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean deletePicutre(int ID, String imageName) {
        boolean result = false;
        String query = "Select * FROM " + imageName + " WHERE " + Picture.COLUMN_ID + " = '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Picture pic = new Picture();
        if (cursor.moveToFirst()) {
            pic.setImageID(Integer.parseInt(cursor.getString(0)));
            db.delete(Picture.TABLE_NAME, Picture.COLUMN_ID + "=?",
                    new String[] {
                            String.valueOf(pic.getImageID())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    //return number of drafts in submission table
    public int getNumberOfDrafts(){
        int len = 0;
        String query = "Select Count(" + Submission.COLUMN_ID + ") FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_STATUS_FLAG + " = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            len = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return len;
    }

    //return number of drafts in submission table
    public int getNumberOfPictures(){
        int len = 0;
        String query = "Select Count(" + Picture.COLUMN_LATITUDE + ") FROM " + Picture.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            len = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return len;
    }

    //return Submission array of drafts
    public ArrayList<Submission> getDrafts(){
        ArrayList<Submission> submissions = new ArrayList<Submission>();
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_STATUS_FLAG + " = 0 ORDER BY " + Submission.COLUMN_DATE_CREATION + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                Submission sub = new Submission();
                sub.setInternalID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_ID)));
                sub.setCaseID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_CASE_ID)));
                sub.setMasterID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_MASTER_ID)));
                sub.setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
                sub.setGroup(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_GROUP)));
                sub.setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
                sub.setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
                sub.setComment(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_COMMENT)));
                submissions.add(sub);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return submissions;
    }

    //return the number of rows in the submission table
    public int getNumberOfSubmissions(){
        int len = 0;
        String query = "Select Count(" + Submission.COLUMN_ID + ") FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_STATUS_FLAG + " != 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            len = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return len;
    }

    //returns array of Submissions that are sent and waiting to send
    public Submission[] getSubmissions(){
        Submission [] subs = new Submission[getNumberOfSubmissions()];
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_STATUS_FLAG + " != 0 ORDER BY " + Submission.COLUMN_DATE_CREATION + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        if(cursor.moveToFirst()){
            do{
                subs[i] = new Submission();
                subs[i].setInternalID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_ID)));
                subs[i].setCaseID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_CASE_ID)));
                subs[i].setMasterID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_MASTER_ID)));
                subs[i].setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
                subs[i].setGroup(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_GROUP)));
                subs[i].setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
                subs[i].setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
                subs[i].setComment(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_COMMENT)));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subs;
    }

    public boolean updateSubmission(Submission submission) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Submission.COLUMN_ID, submission.getInternalID());
        args.put(Submission.COLUMN_CASE_ID, submission.getCaseID());
        args.put(Submission.COLUMN_MASTER_ID, submission.getMasterID());
        args.put(Submission.COLUMN_TITLE, submission.getTitle());
        args.put(Submission.COLUMN_DATE_CREATION, submission.getDateOfCreation());
        args.put(Submission.COLUMN_STATUS_FLAG, submission.getStatusFlag());
        args.put(Submission.COLUMN_GROUP, submission.getGroup());
        args.put(Submission.COLUMN_COMMENT, submission.getComment());
        Log.d("SQLite Database", "Update: " + submission.getTitle());
        return db.update(Submission.TABLE_NAME, args, Submission.COLUMN_ID + "=" + submission.getInternalID(), null) > 0;
    }

    //returns sick element with arg specified id
    public SickElement findSickElementID(int id){
        String query = "Select * FROM " + SickElement.TABLE_NAME + " WHERE " + SickElement.COLUMN_INTERNAL + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        SickElement sickElement = new SickElement();
        if (cursor.moveToFirst()) {
            sickElement.setSickID(cursor.getInt(cursor.getColumnIndex(SickElement.COLUMN_ID)));
            sickElement.setInternalID(cursor.getInt(cursor.getColumnIndex(SickElement.COLUMN_INTERNAL)));
            sickElement.setName(cursor.getString(cursor.getColumnIndex(SickElement.COLUMN_SICKELEMENTNAME)));
            sickElement.setSpecies(cursor.getString(cursor.getColumnIndex(SickElement.COLUMN_SPECIES)));
            sickElement.setEuthanized(cursor.getInt(cursor.getColumnIndex(SickElement.COLUMN_EUTHANIZED)));
            sickElement.setSex(cursor.getString(cursor.getColumnIndex(SickElement.COLUMN_SEX)));
            sickElement.setDateOfBirth(cursor.getLong(cursor.getColumnIndex(SickElement.COLUMN_DATEOFBIRTH)));
            sickElement.setDateOfDeath(cursor.getLong(cursor.getColumnIndex(SickElement.COLUMN_DATEOFDEATH)));
        }
        cursor.close();
        db.close();
        return sickElement;
    }

    public boolean updateSickElement(SickElement sickElement){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(SickElement.COLUMN_ID, sickElement.getSickID());
        args.put(SickElement.COLUMN_INTERNAL, sickElement.getInternalID());
        args.put(SickElement.COLUMN_SICKELEMENTNAME, sickElement.getNameOfSickElement());
        args.put(SickElement.COLUMN_SPECIES, sickElement.getSpecies());
        args.put(SickElement.COLUMN_SEX, sickElement.getSex());
        args.put(SickElement.COLUMN_EUTHANIZED, sickElement.getEuthanized());
        args.put(SickElement.COLUMN_DATEOFBIRTH, sickElement.getDateOfBirth());
        args.put(SickElement.COLUMN_DATEOFDEATH, sickElement.getDateOfDeath());
        Log.d("SQLite Database", "Update: " + sickElement.getNameOfSickElement());
        return db.update(SickElement.TABLE_NAME, args, SickElement.COLUMN_ID + "=" + sickElement.getSickID(), null) > 0;
    }

    public boolean updateSample(Sample sample){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Sample.COLUMN_ID, sample.getInternalID());
        args.put(Sample.COLUMN_INTERNAL, sample.getInternalID());
        args.put(Sample.COLUMN_LOCATIONOFSAMPLE, sample.getLocation());
        args.put(Sample.COLUMN_NAMEOFSAMPLE, sample.getNameOfSample());
        args.put(Sample.COLUMN_NUMBEROFSAMPLE, sample.getNumberOfSamples());
        args.put(Sample.COLUMN_SAMPLECOLLECTIONDATE, sample.getSampleCollectionDate());
        Log.d("SQLite Datebase", "Update: " + sample.getNameOfSample());
        return db.update(Sample.TABLE_NAME, args, Sample.COLUMN_ID + "=" + sample.getInternalID(), null) > 0;
    }

    public boolean updatePicture(Picture picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Picture.COLUMN_ID, picture.getImageID());
        args.put(Picture.COLUMN_INTERNAL, picture.getInternalID());
        args.put(Picture.COLUMN_IMAGETITLE, picture.getImageTitle());
        args.put(Picture.COLUMN_DATETAKEN, picture.getDateTaken());
        args.put(Picture.COLUMN_LATITUDE, picture.getLatitude());
        args.put(Picture.COLUMN_LONGITUDE, picture.getLongitude());
        args.put(Picture.COLUMN_IMAGELINK, picture.getPicturePath());
        Log.d("SQLite Database", "Update: " + picture.getImageTitle());
        return db.update(Picture.TABLE_NAME, args, Picture.COLUMN_INTERNAL + "=" + picture.getInternalID(), null) > 0;
    }
}

