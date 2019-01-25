package com.myvetpath.myvetpath;

public class Submission {
    //Table Fields
    public static final String TABLE_NAME = "Submission";
    public static final String COLUMN_ID = "InternalID";
    public static final String COLUMN_TITLE = "Title";

    //Create Table String
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
            "INTEGER PRIMARY KEY," + COLUMN_TITLE + "TEXT )";

    //Object Fields
    //Commented out variables in order to reduce the amount of testing needed.
    private int internalID; //Primary key for the internal database.
    //private int caseID; //An identifier currently used by Pathologist
    //private int masterID; //Primary key for the Server database
    //private string  statusFlag; // Represents to stage of the submission
    private String title; //user created title
    //private string comment; //Contains the comment of a submission.
    //private date dateOfCreation; // Date the submission was created
    //private date dateOfSubmission; // Date the submission was sent to the server
    //private date dateOfCompletion; //Date the submission is closed and complete

    //Constructors
    public Submission() {}
    public Submission(int id, String titlename) {
        this.internalID = id;
        this.title = titlename;
    }
    //Modifiers
    public void setInternalID(int newInternal){
        this.internalID = newInternal;
    }
    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    //Accessors
    public int getInternalID(){
        return this.internalID;
    }
    public String getTitle(){
        return this.title;
    }

}



