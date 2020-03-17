package com.example.hp.tmastering.beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 07/03/2018.
 */

public class DatabaseHandler  extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "tmastering";
        private static final String TABLE_USERS = "user";

        private static final String KEY_ID = "id_user";
        private static final String KEY_ROLE = "role_user";

        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USER_TABLE ="CREATE TABLE " + TABLE_USERS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_ROLE + " TEXT" + ")";
            db.execSQL(CREATE_USER_TABLE);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

            // Create tables again
            onCreate(db);
        }


    public void addUser(user user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_USERS);

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_ROLE, user.getRole());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public String getRoleUser() {

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String res="";
        if(cursor.moveToFirst())
            res=cursor.getString(1);
        return res;
    }

    public int getIdUser() {

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int res=-1;
        if(cursor.moveToFirst())
            res=cursor.getInt(0);
        return res;
    }



}
