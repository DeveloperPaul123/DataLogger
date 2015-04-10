package com.devpaul.datalogger.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.devpaul.datalogger.data.DataSource;
import com.devpaul.datalogger.data.Subject;

import java.util.List;

/**
 * Created by Pauly D on 3/17/2015.
 *
 * Handles the loading of a list of subjects.
 */
public class LoadSubjectsTask extends AsyncTask<Void, Void, List<Subject>> {

    public static interface AsyncCallback {
        public void onSubjectsLoaded(List<Subject> subjects);
    }

    private DataSource dataSource;
    private Context context;
    private AsyncCallback callback;

    /**
     * Handles the loading of subjects from the database.
     * @param context, the Activity or fragment context.
     * @param callback, a callback to notify the activity or fragment.
     */
    public LoadSubjectsTask(Context context, AsyncCallback callback) {
        dataSource = new DataSource(context);
        dataSource.open();
        this.context = context;
        this.callback = callback;
    }
    @Override
    protected List<Subject> doInBackground(Void... params) {
        return dataSource.getSubjects();
    }

    @Override
    protected void onPostExecute(List<Subject> subjects) {
        if(callback != null) {
            callback.onSubjectsLoaded(subjects);
        }
    }
}