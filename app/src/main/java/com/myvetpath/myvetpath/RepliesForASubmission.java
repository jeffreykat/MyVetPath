package com.myvetpath.myvetpath;

import java.sql.Date;

public class RepliesForASubmission {
    //Table Fields
    public static final String TABLE_NAME = "RepliesForASubmission";
    public static final String COLUMN_ID = "ReplyID";
    public static final String COLUMN_INTERNALID = "internalID";
    public static final String COLUMN_DATEOFMESSAGE = "DateOfMessage";


    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    " INTEGER PRIMARY KEY, "
                    + COLUMN_INTERNALID + " INTEGER, "
                    + COLUMN_DATEOFMESSAGE + " BIGINT" + " )";

    //Object Fields
    private int replyID;
    private int internalID;
    private long dateOfMessage;


    //Constructors
    public RepliesForASubmission() {}
    public RepliesForASubmission(int id, int newInternal, long newDate) {
        this.replyID = id;
        this.internalID = newInternal;
        this.dateOfMessage = newDate;
    }
    //Modifiers
    public void setID(int newID){
        this.replyID = newID;
    }
    public void setInternalID(int newInternal){
        this.internalID = newInternal;
    }
    public void setDateofmessage(int newDate){
        this.dateOfMessage = newDate;}
    //Accessors
    public int getReplyID(){
        return this.replyID;
    }
    public int getInternalID(){
        return this.internalID;
    }
    public long getDateOfMessage(){ return this.dateOfMessage; }


}




