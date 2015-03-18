package com.devpaul.datalogger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.devpaul.datalogger.data.DataSource;
import com.devpaul.datalogger.data.Subject;
import com.devpaul.datalogger.fragments.SubjectListFragment;
import com.devpaul.datalogger.fragments.dialogs.NewSubjectDialog;
import com.github.mrengineer13.snackbar.SnackBar;


public class MainActivity extends ActionBarActivity implements NewSubjectDialog.Callback{

    private DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout for the view. This is just a container for the fragments.
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            //load in the first fragment.
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, SubjectListFragment.newInstance(), SubjectListFragment.TAG)
                    .commit();
        }

        dataSource = new DataSource(this);
        dataSource.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubjectCreated(Subject subject) {
        new CreateSubjectTask(subject, this).execute();
        SubjectListFragment fragment = (SubjectListFragment) getSupportFragmentManager()
                .findFragmentByTag(SubjectListFragment.TAG);

        if(fragment != null) {
            fragment.addSubjectToList(subject);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    /**
     * Class that handles creating the subject in the database.
     */
    private class CreateSubjectTask extends AsyncTask<Void, Void, Void> {
        private Subject subject;
        private Context context;
        public CreateSubjectTask(Subject s, Context c) {
            subject = s;
            context = c;
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataSource.open();
            dataSource.createSubject(subject);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SnackBar.Builder snack = new SnackBar.Builder((ActionBarActivity) context)
                    .withMessage("Subject created.").withDuration((short) 1000);
            snack.show();
        }
    }
}
