package com.myvetpath.myvetpath;

import java.sql.Date;

public class Client {
    //Table Fields
    public static final String TABLE_NAME = "Client";
    public static final String COLUMN_ID = "ClientID";
    public static final String COLUMN_FIRSTNAME = "FirstName";
    public static final String COLUMN_LASTNAME = "LastName";
    public static final String COLUMN_PHONENUMBER = "PhoneNumber";
    public static final String Column_EMAIL = "Email";

    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID +
                    " INTEGER PRIMARY KEY, "
                    + COLUMN_FIRSTNAME + " TEXT, "
                    + COLUMN_LASTNAME + " TEXT, "
                    + COLUMN_PHONENUMBER + " TEXT, "
                    + Column_EMAIL + " TEXT" + " )";


    //Object Fields
    private int clientID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;


    //Constructors
    public Client() {}
    public Client(int id, String newFirst, String newLast, String newPhone, String newEmail) {
        this.clientID = id;
        this.firstName = newFirst;
        this.lastName = newLast;
        this.phoneNumber = newPhone;
        this.email = newEmail;
    }
    //Modifiers
    public void setID(int newID){
        this.clientID = newID;
    }
    public void setFirstName(String newFirst){
        this.firstName = newFirst;
    }
    public void setLastName(String newLast){
        this.lastName = newLast;
    }
    public void setPhonenumber(String newPhone) {
        this.phoneNumber = newPhone;
    }
    public void setEmail(String newEmail){
        this.email = newEmail;
    }
    //Accessors
    public int getClientID(){
        return this.clientID;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public String getEmail(){
        return this.email;
    }

}




