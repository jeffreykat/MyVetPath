package com.myvetpath.myvetpath;

public class Submission {
    //Fields
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
        //this.masterID = 0;
        this.title = titlename;
        //this.dateOfCreation = new Date();
        //this.caseID = first
    }
    //Modifiers
    public void setInternalID(int newInternal){
        this.internalID = newInternal;
    }
    public void setCaseID(){}
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



