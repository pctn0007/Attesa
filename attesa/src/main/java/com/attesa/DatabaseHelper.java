package com.attesa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context) {
        super(context, "Attesa.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables of our database for new app user.
        db.execSQL("Create table user(id integer primary key autoincrement unique, email text unique, password text, HC integer unique)");
        db.execSQL("Create table clinics(id integer primary key autoincrement unique, " +
                "email text, " +
                "password text, " +
                "cname text, " +
                "phone text, " +
                "ctype text," +
                "caddr text)");

        db.execSQL("Create table visits(id integer primary key autoincrement unique, uHC integer, cname text, status integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists clinics");
        db.execSQL("drop table if exists visits");
    }

    // Get table size.
    public int getTableSize(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return (int) count;

    }


    // Inserting new visit into database.
    public boolean insertVisit(String HC, String cname, Integer status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uHC", HC);
        contentValues.put("cname", cname);
        contentValues.put("status", status);

        long ins = db.insert("visits", null, contentValues);
        if (ins == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    // Inserting new user into database.
    public boolean insert(String email, String password, String HC){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("HC", HC);

        long ins = db.insert("user", null, contentValues);
        if (ins == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // Inserting new clinic into database.
    public boolean insertClinic(String cname, String ctype, String phone, String[] email, String password, String caddr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", String.valueOf(email));
        contentValues.put("password", password);
        contentValues.put("cname", String.valueOf(cname));
        contentValues.put("ctype", String.valueOf(ctype));
        contentValues.put("phone", phone);
        contentValues.put("caddr", caddr);


        long ins = db.insert("clinics", null, contentValues);
        if (ins == -1)
        {
            return false;
        }
        else
        {
            System.out.println("INSERTED NEW CLINIC RECORD !!!!!!!!!");
            return true;
        }
    }


    // Checking if clinic name exists
    public String getcname(String cname)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select cname from clinics where cname=?", new String[]{cname});

        if (cursor.getCount() > 0)
        {
            return cname;
        }
        else
        {
            return "clinic name N/A";
        }
    }
    // Checking if clinic address exists
    public String getcaddr(String caddr)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select caddr from clinics where caddr=?", new String[]{caddr});

        if (cursor.getCount() > 0)
        {
            return caddr;
        }
        else
        {
            return "clinic address N/A";
        }
    }
    // Checking if User health card number exists
    public String getHC(String HC)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select HC from user where HC=?", new String[]{HC});

        if (cursor.getCount() > 0)
        {
            return HC;
        }
        else
        {
            return "user health card N/A";
        }
    }

    // Checking if User health card number exists
    public String getHCviaEmail(String email, String pass)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select HC from user where email=? and password=?", new String[]{email, pass});
        cursor.moveToFirst();

        if (cursor.getCount() > 0)
        {
            return cursor.getString(cursor.getColumnIndex("HC"));
        }
        else
        {
            return "user health card N/A";
        }
    }
    // Checking if email exists (true - NO; false - YES)
    public Boolean chkemail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email=?", new String[]{email});

        if (cursor.getCount() > 0)
        {

            return false;
        }
        else
        {
            return true;
        }
    }


    // Checking if healthcard number exists (true - NO; false - YES)
    public Boolean chkHC(String HC)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email=? and password=?", new String[]{HC});

        if (cursor.getCount() > 0)
        {

            return false;
        }
        else
        {
            return true;
        }
    }

    // Checking the email and password (login).
    public Boolean loginSubmit(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email=? and password=?", new String[]{email, password});
        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    // Return user logged-in email.
    public String uemail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email=?", new String[]{email});

        if (cursor.getCount() > 0)
        {
            return email;
        }
        else
        {
            email = null;
            System.out.println("No email found!");
            return email;
        }
    }

}


