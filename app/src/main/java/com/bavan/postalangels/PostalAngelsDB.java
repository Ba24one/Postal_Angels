package com.bavan.postalangels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostalAngelsDB extends SQLiteOpenHelper {
    public PostalAngelsDB(Context context) {
        super(context, "PostalAngels.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE Employeedetails (username TEXT PRIMARY KEY, password TEXT, department TEXT)");
        DB.execSQL("CREATE TABLE Packagedetails (packageCode TEXT PRIMARY KEY, senderAddress TEXT, recieverAddress TEXT, recievedDate TEXT, status TEXT, deliveredDate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS Employeedetails");
        DB.execSQL("DROP TABLE IF EXISTS Packagedetails");
    }

    //These set of codes are for login purposes

    public Boolean insertemployeedata(String username, String password, String department){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("department", department);
        long result = DB.insert("Employeedetails", null, contentValues);
        if(result==-1){
            return false;
        } else{
            return true;
        }
    }

    public Boolean checkusernamepassword(String username, String password, String department){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Employeedetails WHERE username = ? AND password = ? AND department = ?", new String[] {username, password, department});
        if(cursor.getCount () > 0)
            return true;
        else
            return false;
    }

    //These codes are to check the existence of the package code

    public Boolean checkpackagecode(String packageCode){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Packagedetails WHERE packageCode = ?", new String[] {packageCode});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //These set of codes are for staff purposes

    public Boolean createpackagedetails(String packageCode, String senderAddress, String recieverAddress, String recievedDate, String status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("packageCode", packageCode);
        contentValues.put("senderAddress", senderAddress);
        contentValues.put("recieverAddress", recieverAddress);
        contentValues.put("recievedDate", recievedDate);
        contentValues.put("status", status);
        long result = DB.insert("Packagedetails", null, contentValues);
        if(result==-1){
            return false;
        } else{
            return true;
        }
    }

    public Cursor getStatus(String packagestatus){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Packagedetails WHERE status LIKE'%"+packagestatus+"%'", null);

        return cursor;
    }

    public Boolean updateontheway(String packageCode, String status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        Cursor cursor = DB.rawQuery("SELECT * FROM Packagedetails WHERE packageCode = ?", new String[] {packageCode});
        if(cursor.getCount() > 0) {

            long result = DB.update("Packagedetails", contentValues, "packageCode=?", new String[]{packageCode});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else{
            return false;
        }
    }

    //These set of codes are for postmen purposes

    public Cursor getpendingStatus(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Packagedetails WHERE status LIKE'%On the way%'", null);

        return cursor;
    }

    public Boolean updatedelivered(String packageCode, String deliveredDate, String status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deliveredDate", deliveredDate);
        contentValues.put("status", status);
        Cursor cursor = DB.rawQuery("SELECT * FROM Packagedetails WHERE packageCode = ? AND status = 'On the way'", new String[] {packageCode});
        if(cursor.getCount() > 0) {

            long result = DB.update("Packagedetails", contentValues, "packageCode=?", new String[]{packageCode});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else{
            return false;
        }
    }

    //These set of codes are for customers to track their packages

    public Cursor customerpackagestatus(String customerpackagecode){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Packagedetails WHERE packageCode LIKE '%"+customerpackagecode+"%'", null);
//        Cursor cursor = DB.rawQuery("SELECT * FROM Packagedetails WHERE packageCode = ?", null);

        return cursor;
    }


}
