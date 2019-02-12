package com.myvetpath.myvetpath;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;


public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myVetPath.db";

    //initialize the database
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates a database with the table Submission.
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Submission.CREATE_TABLE);
        //Added the below tables to the new database.
        db.execSQL(Picture.CREATE_TABLE);
        db.execSQL(SickElement.CREATE_TABLE);
        db.execSQL(Sample.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

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
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Submission.TABLE_NAME, null, values);
        db.close();
    }
    // added adders for the tables: sample, sick element, and image
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
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(picture.TABLE_NAME, null, values);
        db.close();
    }

    //Searches the database to to find a row based on a title.
    public Submission findSubmissionTitle(String nTitle) {
        String query = "Select * FROM " + Submission.TABLE_NAME + "WHERE" + Submission.COLUMN_TITLE + " = " + "'" + nTitle + "'";
        SQLiteDatabase db = this.getWritableDatabase();
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
        String query = "Select * FROM " + Submission.TABLE_NAME + "WHERE" + Submission.COLUMN_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
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
    //added a deletion for the picture for now. If more is needed just send notify me.
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

    public boolean updateSubmission(int ID, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(Submission.COLUMN_ID, ID);
        args.put(Submission.COLUMN_TITLE, title);
        return db.update(Submission.TABLE_NAME, args, Submission.COLUMN_ID + "=" + ID, null) > 0;
    }
}

