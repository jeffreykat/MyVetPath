package com.myvetpath.myvetpath;

import java.sql.Date;

public class Reply {
    //Table Fields
    public static final String TABLE_NAME = "Reply";
    public static final String COLUMN_ID = "ReplyID";
    public static final String COLUMN_SENDERID = "SenderID";
    public static final String COLUMN_RECIVERID = "ReciverID";
    public static final String COLUMN_MESSAGE = "Message"; //shortened the column name for faster typing.


    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    " INTEGER PRIMARY KEY, "
                    + COLUMN_SENDERID + " INTEGER, "
                    + COLUMN_RECIVERID + " INTEGER, "
                    + COLUMN_MESSAGE + " TEXT" + " )";

    //Object Fields
    private int replyID;
    private int senderID;
    private int recieverID;
    private String message;


    //Constructors
    public Reply() {}
    public Reply(int id, int newSender, int newReciever, String newMessage) {
        this.replyID = id;
        this.senderID = newSender;
        this.recieverID = newReciever;
        this.message = newMessage;
    }
    //Modifiers
    public void setID(int newID){
        this.replyID = newID;
    }
    public void setSenderID(int newSender){
        this.senderID = newSender;
    }
    public void setRecieverID(int newReciever){
        this.recieverID = newReciever;
    }
    public void setMessage(String newMessage){
        this.message = newMessage;
    }
    //Accessors
    public int getReplyID(){
        return this.replyID;
    }
    public int getSenderid(){
        return this.senderID;
    }
    public int getRecieverID(){ return this.recieverID; }
    public String getMessage(){return this.message; }

}




