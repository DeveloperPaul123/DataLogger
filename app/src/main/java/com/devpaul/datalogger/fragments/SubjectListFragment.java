package com.devpaul.datalogger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devpaul.datalogger.DataLoggingActivity;
import com.devpaul.datalogger.R;
import com.devpaul.datalogger.data.DataSource;
import com.devpaul.datalogger.data.Subject;
import com.devpaul.datalogger.fragments.dialogs.NewSubjectDialog;
import com.devpaul.datalogger.utils.LoadSubjectsTask;
import com.devpaul.materialfabmenu.MaterialFloatingActionButton;

import java.util.List;

/**
 * Created by Pauly D on 3/14/2015.
 * First view that the user will see.
 */
public class SubjectListFragment extends Fragment implements LoadSubjectsTask.AsyncCallback{

    public static final String TAG =  "SubjectListFragment";
    /**
     * The list of items in the view.
     */
    private ListView listView;

    /**
     * The button in the bottom right corner.
     */
    private MaterialFloatingActionButton fab;

    /**
     * The datasource for handling the data.
     */
    private DataSource dataSource;

    /**
     * List adapter for the list view.
     */
    private SubjectListAdapter subjectListAdapter;

    /**
     * Empty constructor.
     */
    public SubjectListFragment() {}

    /**
     * Creates a new instance of a fragment.
     * @return the new Fragment.
     */
    public static SubjectListFragment newInstance() {
        return new SubjectListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //bind the list view to the item in the layout.
        listView = (ListView) v.findViewById(android.R.id.list);

        //connect the button.
        fab = (MaterialFloatingActionButton) v.findViewById(R.id.fab);
        fab.setUseSelector(true);
        //set the action that will occur when the button is clicked.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FAB", "OnClick");
                createNewSubject();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent logData = new Intent(getActivity(), DataLoggingActivity.class);
                logData.putExtra("SUBJECT_ID", subjectListAdapter.getItem(position).getId());
                startActivity(logData);
            }
        });
        new LoadSubjectsTask(getActivity().getApplicationContext(), this).execute();

        return v;
    }

    /**
     * Creates a new subject
     */
    private void createNewSubject() {
        NewSubjectDialog dialog = NewSubjectDialog.newInstance();
        dialog.show(getActivity().getSupportFragmentManager(), "New Subject");
    }

    public void addSubjectToList(Subject s) {
        subjectListAdapter.addItem(s);
    }

    //callback from async task.
    @Override
    public void onSubjectsLoaded(List<Subject> subjects) {
        Log.i("Subjects", "SubjectsLoaded");
        subjectListAdapter = new SubjectListAdapter(getActivity().getApplicationContext(), subjects);
        listView.setAdapter(subjectListAdapter);
    }


}
