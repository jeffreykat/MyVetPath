package com.myvetpath.myvetpath;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
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
    }

    //Remove submission table
    public void dropTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS Submission");
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
    public void addSubmission(Submission submission) {
        ContentValues values = new ContentValues();
        values.put(Submission.COLUMN_TITLE, submission.getTitle());
        values.put(Submission.COLUMN_DATE_CREATION, submission.getDateOfCreation());
        values.put(Submission.COLUMN_STATUS_FLAG, submission.getStatusFlag());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Submission.TABLE_NAME, null, values);
        db.close();
    }

    //Searches the database to to find a row based on a title.
    public Submission findSubmissionTitle(String nTitle) {
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_TITLE + " = " + "'" + nTitle + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Submission sub = new Submission();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            sub.setInternalID(Integer.parseInt(cursor.getString(0)));
            sub.setTitle(cursor.getString(1));
            cursor.close();
        } else {
            sub = null;
        }
        db.close();
        return sub;
    }

    //Searches the database to find a row based on the id.
    public Submission findSubmissionID(int id) {
        String query = "Select * FROM " + Submission.TABLE_NAME + " WHERE " + Submission.COLUMN_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Submission sub = new Submission();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            sub.setInternalID(Integer.parseInt(cursor.getString(0)));
            sub.setTitle(cursor.getString(1));
            cursor.close();
        } else {
            sub = null;
        }
        db.close();
        return sub;
    }

    //Deletes a row based on the id of a table.
    public boolean deleteSubmission(int ID, String tableName) {
        boolean result = false;
        String query = "Select * FROM " + tableName + " WHERE " + Submission.COLUMN_ID + " = '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Submission sub = new Submission();
        if (cursor.moveToFirst()) {
            sub.setInternalID(Integer.parseInt(cursor.getString(0)));
            db.delete(Submission.TABLE_NAME, Submission.COLUMN_ID + "=?",
                    new String[] {
                            String.valueOf(sub.getInternalID())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    //return the number of rows in the submission table
    public int getNumberOfSubmissions(){
        int len = 0;
        String query = "Select Count(" + Submission.COLUMN_ID + ") FROM Submission";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            Log.d("table length", "len = " + cursor.getInt(0));
            len = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return len;
    }

    //return String array of all submission titles
    public String[] getSubmissionTitles(){
        int len = getNumberOfSubmissions();
        String [] titles = new String[len];
        int i = 0;
        String query = "Select " + Submission.COLUMN_TITLE + " FROM Submission";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            for(i = 0; i < len; i++){
                titles[i] = cursor.getString(cursor.getPosition());
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return titles;
    }

    //return formatted String array of submission dates
    public String[] getSubmissionDates(){
        int len = getNumberOfSubmissions();
        String [] dates = new String[len];
        int i = 0;
        String query = "Select " + Submission.COLUMN_DATE_CREATION + " FROM Submission";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            while(i < len) {
                long d = Long.parseLong(cursor.getString(cursor.getPosition()));
                calendar.setTimeInMillis(d);
                dates[i] = simpleDateFormat.format(calendar.getTime());
                i++;
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return dates;
    }

    //return true if there are drafts, return false if no drafts exist
    public boolean draftsExist() {
        String query = "Select " + Submission.COLUMN_STATUS_FLAG + " FROM Submission";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            for(int i = 0; i < getNumberOfSubmissions(); i++){
                if(cursor.getInt(0) == 0) {
                    cursor.close();
                    return true;
                }
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    public boolean updateHandler(int ID, String title, Long dateCreation, int statusFlag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Submission.COLUMN_ID, ID);
        args.put(Submission.COLUMN_TITLE, title);
        args.put(Submission.COLUMN_DATE_CREATION, dateCreation);
        args.put(Submission.COLUMN_STATUS_FLAG, statusFlag);
        return db.update(Submission.TABLE_NAME, args, Submission.COLUMN_ID + "=" + ID, null) > 0;
    }
}

