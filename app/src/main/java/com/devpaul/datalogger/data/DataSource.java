package com.devpaul.datalogger.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pauly D on 3/14/2015.
 */
public class DataSource {
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase database;

    private static final String[] allcolumns = {
            DatabaseOpenHelper.COLUMN_ID,
            DatabaseOpenHelper.COLUMN_AGE,
            DatabaseOpenHelper.COLUMN_NUMBER,
            DatabaseOpenHelper.COLUMN_WEIGHT,
            DatabaseOpenHelper.COLUMN_HEIGHT,
            DatabaseOpenHelper.COLUMN_GENDER,
            DatabaseOpenHelper.COLUMN_TESTS,
            DatabaseOpenHelper.COLUMN_CATEGORY};

    /**
     * Default constructor. This class handles all the database reading and writing.
     * @param context, the context of the activity or fragment this is opened in.
     */
    public DataSource(Context context) {
        databaseOpenHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Opens the database for writing and reading.
     */
    public void open() {
        database = databaseOpenHelper.getWritableDatabase();
    }

    /**
     * Closes the database and releases resources. Should be called in activity onDestroy method.
     */
    public void close() {
        databaseOpenHelper.close();
    }

    /**
     * Creates a new {@code Subject} object in the database.
     * @param subject, the new {@code Subject} to create.
     */
    public void createSubject(Subject subject) {
        ContentValues value = new ContentValues();
        value.put(DatabaseOpenHelper.COLUMN_ID, subject.getId());
        value.put(DatabaseOpenHelper.COLUMN_AGE, subject.getAge());
        value.put(DatabaseOpenHelper.COLUMN_NUMBER, subject.getNumber());
        value.put(DatabaseOpenHelper.COLUMN_WEIGHT, subject.getWeight());
        value.put(DatabaseOpenHelper.COLUMN_HEIGHT, subject.getHeight());
        value.put(DatabaseOpenHelper.COLUMN_GENDER, subject.getGender());
        value.put(DatabaseOpenHelper.COLUMN_CATEGORY, subject.getCategory());
        value.put(DatabaseOpenHelper.COLUMN_TESTS, subject.getDoneStudies());
        database.insert(DatabaseOpenHelper.TABLE_NAME, null, value);
    }

    public void updateSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_TESTS, subject.getDoneStudies());
        database.update(DatabaseOpenHelper.TABLE_NAME, values, null, null);
    }

    /**
     * Gets all the subjects present in the database.
     * @return {@code List} of {@code Subject} objects.
     */
    public List<Subject> getSubjects() {
        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME, allcolumns, null, null, null, null, null);
        return cursorToList(cursor);
    }

    public Subject getSubjectById(long id) {
        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME,
                allcolumns, DatabaseOpenHelper.COLUMN_ID+"=?", new String[]{"" + id}, null, null, null);
        List<Subject> subjects = cursorToList(cursor);
        if(subjects.size() == 1) {
            return subjects.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Converts a cursor given by a database query into a list of objects.
     * @param cursor The cursor from the query.
     * @return a {@code List} of {@code Subject} objects.
     */
    private List<Subject> cursorToList(Cursor cursor) {
        List<Subject> subjects = new ArrayList<>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_ID));
                    Subject subject = new Subject(id);

                    int age = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_AGE));
                    int height = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_HEIGHT));
                    int weight = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_WEIGHT));
                    int number = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_NUMBER));
                    String gender = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_GENDER));
                    String category = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_CATEGORY));
                    String tests = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TESTS));

                    subject.setAge(age);
                    subject.setHeight(height);
                    subject.setWeight(weight);
                    subject.setCategory(category);
                    subject.setGender(gender);
                    subject.setNumber(number);
                    subject.setDoneStudies(tests);

                    subjects.add(subject);
                }
            }

        }

        return subjects;
    }
}
