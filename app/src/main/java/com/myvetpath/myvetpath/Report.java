package com.myvetpath.myvetpath;

import java.sql.Date;

public class Report {
    //Table Fields
    public static final String TABLE_NAME = "Report";
    public static final String COLUMN_ID = "PathologistID";
    public static final String COLUMN_INTERNALID = "InternalID";
    public static final String COLUMN_REPORTDATE = "ReportDate";
    public static final String COLUMN_FINALCOMMENTS = "FinalComments";
    public static final String COLUMN_Attachments = "Attachments";


    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    " INTEGER PRIMARY KEY, "
                    + COLUMN_INTERNALID + " INTEGER, "
                    + COLUMN_REPORTDATE + " BIGINT, "
                    + COLUMN_FINALCOMMENTS + " TEXT, "
                    + COLUMN_Attachments + " TEXT" + " )";

    //Object Fields
    private int pathoID;
    private int internalID;
    private long reportDate;
    private String finalComments;
    private String attachments; //not sure what we are doing with this field right now.


    //Constructors
    public Report() {}
    public Report(int id, int newInternal, long newDate, String newFinal, String newAttachments) {
        this.pathoID = id;
        this.internalID = newInternal;
        this.reportDate = newDate;
        this.finalComments = newFinal;
        this.attachments = newAttachments;
    }
    //Modifiers
    public void setID(int newID){
        this.pathoID = newID;
    }
    public void setInternalID(int newInternal){
        this.internalID = newInternal;
    }
    public void setReportDate(long newDate){
        this.reportDate = newDate;
    }
    public void setFinalComments(String newFinal){
        this.finalComments = newFinal;
    }
    public void setAttachments (String newAttachments){ this.attachments = newAttachments;}
    //Accessors
    public int getPathoID(){
        return this.pathoID;
    }
    public int getInternalID(){
        return this.internalID;
    }
    public long getReportDate(){ return this.reportDate; }
    public String getFinalComments(){return this.finalComments; }
    public String getAttachments(){return this.attachments;}

}




