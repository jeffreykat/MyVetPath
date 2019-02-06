package com.myvetpath.myvetpath;

import java.util.Date;

public class Submission {
    //Table Fields
    public static final String TABLE_NAME = "Submission";
    public static final String COLUMN_ID = "internalID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE_CREATION = "dateOfCreation";
    public static final String COLUMN_STATUS_FLAG = "statusFlag";

    //Create Table String

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_DATE_CREATION + " BIGINT, " +
            COLUMN_STATUS_FLAG + " INT" + " )";

    //Object Fields
    //Commented out variables in order to reduce the amount of testing needed.
    private int internalID; //Primary key for the internal database.
    //private int caseID; //An identifier currently used by Pathologist
    //private int masterID; //Primary key for the Server database
    private int statusFlag; // Stage of the submission 0 = draft, 1 = submitted, 2 = received by server
    private String title; //user created title
    //private string comment; //Contains the comment of a submission.
    /*TODO: check if Date is correct SQLite date type*/
    private long dateOfCreation; // Date the submission was created
    //private date dateOfSubmission; // Date the submission was sent to the server
    //private date dateOfCompletion; //Date the submission is closed and complete

    //Constructors
    public Submission() {}
    public Submission( int internalID, String titlename, long dateOfCreation, int statusFlag) {
        this.internalID = internalID;
        this.title = titlename;
        this.dateOfCreation = dateOfCreation;
        this.statusFlag = statusFlag;
    }

    //Modifiers
    public void setInternalID(int newInternal){
        this.internalID = newInternal;
    }
    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public void setDateOfCreation(long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    //Accessors
    public int getInternalID(){
        return this.internalID;
    }

    public String getTitle(){
        return this.title;
    }

    public long getDateOfCreation() {
        return dateOfCreation;
    }

    public int getStatusFlag() {
        return statusFlag;
    }
}



