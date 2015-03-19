package com.devpaul.datalogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devpaul.datalogger.R;

/**
 * Created by Pauly D on 3/18/2015.
 */
public class SubjectDetailFragment extends Fragment {

    private static final String[] titles = new String[] {
            "Normal Squats",
            "Varied Speed",
            "Block Right Foot",
            "Block Left Foot",
            "Block Toes",
            "Block Heels"
    };

    public static SubjectDetailFragment newInstance() {
        return new SubjectDetailFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subject_detail, container, false);
        return v;
    }
}
