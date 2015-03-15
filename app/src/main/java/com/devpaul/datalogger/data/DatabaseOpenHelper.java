package com.devpaul.datalogger.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pauly D on 3/14/2015.
 *
 * This is a wrapper class for creating an SQLiteDatabase for this app.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    /**
     * Name of the database.
     */
    private static final String DATABASE_NAME = "dataLoggerSubject.db";
    /**
     * Version of the database.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table name of the database table.
     */
    public static final String TABLE_NAME = "Subjects";

    /**
     * Column titles in the database.
     */
    public static final String COLUMN_ID = "subjectId";
    public static final String COLUMN_NUMBER = "subjectNumber";
    public static final String COLUMN_HEIGHT = "subjectHeight";
    public static final String COLUMN_WEIGHT = "subjectWeight";
    public static final String COLUMN_AGE = "subjectAge";
    public static final String COLUMN_CATEGORY = "subjectCategory";
    public static final String COLUMN_GENDER = "subjectGender";

    /**
     * SQL Statement to create a new table.
     */
    private static final String CREATE_MAIN_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER NOT NULL, " +
                    COLUMN_NUMBER + " INTEGER " +
                    COLUMN_HEIGHT + " INTEGER, " +
                    COLUMN_WEIGHT + " INTEGER, " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT " + ")";


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MAIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
