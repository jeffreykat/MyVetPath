package com.myvetpath.myvetpath;

import java.sql.Date;

public class User { //remaned to picture so we don't get it confused with Image object from android
    //Table Fields
    public static final String TABLE_NAME = "User";
    public static final String COLUMN_ID = "UserID";
    public static final String COLUMN_USERNAME = "UserName";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_AUTHORIZED = "Authorized";

    //Create Table String
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    " INTEGER PRIMARY KEY, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
                    + COLUMN_AUTHORIZED + " INTEGER" + " )";


    //Object Fields
    private int userID;
    private String userName;
    private String password;
    private int authorized; //flag variable 0 means no 1 means yes.


    //Constructors
    public User() {}
    public User(int id, String newName, String newPass, int Author) {
        this.userID = id;
        this.userName = newName;
        this.password = newPass;
        this.authorized = Author;
    }
    //Modifiers
    public void setID(int newID){
        this.userID = newID;
    }
    public void setUserName(String newName){
        this.userName = newName;
    }
    public void setPassword(String newPass){
        this.password = newPass;
    }
    public void setAuthorized(int Authorization) {
        this.authorized = Authorization;
    }
    //Accessors
    public int getUserID(){
        return this.userID;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public int getAuthorized(){
        return this.authorized;
    }

}




