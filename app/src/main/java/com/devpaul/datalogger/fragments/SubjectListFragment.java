package com.devpaul.datalogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.devpaul.datalogger.R;
import com.devpaul.datalogger.data.DataSource;
import com.devpaul.materialfabmenu.MaterialFloatingActionButton;

/**
 * Created by Pauly D on 3/14/2015.
 * First view that the user will see.
 */
public class SubjectListFragment extends Fragment{

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
        View v = inflater.inflate(R.layout.fragment_main, container);

        //bind the list view to the item in the layout.
        listView = (ListView) v.findViewById(android.R.id.list);

        //connect the button.
        fab = (MaterialFloatingActionButton) v.findViewById(R.id.fab);
        //set the action that will occur when the button is clicked.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewSubject();
            }
        });

        return v;
    }

    /**
     * Creates a new subject
     */
    private void createNewSubject() {
        //TODO
    }
}
