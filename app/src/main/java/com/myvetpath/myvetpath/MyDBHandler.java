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
        db.execSQL(Picture.CREATE_TABLE);
        db.execSQL(SickElement.CREATE_TABLE);
        db.execSQL(Sample.CREATE_TABLE);
        //added below tables
        db.execSQL(Client.CREATE_TABLE);
        db.execSQL(Group.CREATE_TABLE);
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
        db.execSQL(Group.CREATE_TABLE);
        Log.v("tableCreateGroup", Group.CREATE_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + Submission.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Picture.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Sample.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SickElement.TABLE_NAME);
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
        values.put(Submission.COLUMN_TITLE, submission.getTitle());
        values.put(Submission.COLUMN_DATE_CREATION, submission.getDateOfCreation());
        values.put(Submission.COLUMN_STATUS_FLAG, submission.getStatusFlag());
        SQLiteDatabase db = this.getWritableDatabase();
        long internlID = db.insert(Submission.TABLE_NAME, null, values);
        db.close();
        return internlID;
    }

    public void addSample(Sample sample) {
        ContentValues values = new ContentValues();
        values.put(Sample.COLUMN_NAMEOFSAMPLE, sample.getNameOfSample());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Sample.TABLE_NAME, null, values);
        db.close();
    }

    public void addSickElement(SickElement sickElement) {
        ContentValues values = new ContentValues();
        values.put(SickElement.COLUMN_SICKELEMENTNAME, sickElement.getNameOfSickElement());
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
            sub.setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
            sub.setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
            sub.setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
        } else {
            sub = null;
        }
        cursor.close();
        db.close();
        return sub;
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
            sub.setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
            sub.setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
            sub.setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
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
    public Submission[] getDrafts(){
        Submission [] subs = new Submission[getNumberOfSubmissions()];
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_STATUS_FLAG + " = 0 ORDER BY " + Submission.COLUMN_DATE_CREATION + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        if(cursor.moveToFirst()){
            do{
                subs[i] = new Submission();
                subs[i].setInternalID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_ID)));
                subs[i].setCaseID(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_CASE_ID)));
                subs[i].setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
                subs[i].setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
                subs[i].setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subs;
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
                subs[i].setTitle(cursor.getString(cursor.getColumnIndex(Submission.COLUMN_TITLE)));
                subs[i].setDateOfCreation(cursor.getLong(cursor.getColumnIndex(Submission.COLUMN_DATE_CREATION)));
                subs[i].setStatusFlag(cursor.getInt(cursor.getColumnIndex(Submission.COLUMN_STATUS_FLAG)));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subs;
    }

    public boolean updateHandler(int ID, int caseID, String title, Long dateCreation, int statusFlag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Submission.COLUMN_ID, ID);
        args.put(Submission.COLUMN_CASE_ID, caseID);
        args.put(Submission.COLUMN_TITLE, title);
        args.put(Submission.COLUMN_DATE_CREATION, dateCreation);
        args.put(Submission.COLUMN_STATUS_FLAG, statusFlag);
        return db.update(Submission.TABLE_NAME, args, Submission.COLUMN_ID + "=" + ID, null) > 0;
    }
}

