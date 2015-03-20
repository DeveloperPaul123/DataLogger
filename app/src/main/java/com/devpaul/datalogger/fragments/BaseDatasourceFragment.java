package com.devpaul.datalogger.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.devpaul.datalogger.data.DataSource;

/**
 * Created by Pauly D on 3/19/2015.
 */
public abstract class BaseDatasourceFragment extends Fragment {

    private DataSource dataSource;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new DataSource(getActivity());
        dataSource.open();
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
