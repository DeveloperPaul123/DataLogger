package com.devpaul.datalogger.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.devpaul.datalogger.R;

/**
 * Created by Pauly D on 3/18/2015.
 */
public class SerialMonitorFragment extends Fragment {

    private EditText serialView;
    private EditText message;
    private ImageButton send;
    private Callback callback;

    public static interface Callback {
        public void onSendData(String data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (Callback) activity;
    }

    public static SerialMonitorFragment newInstance() {
        return new SerialMonitorFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_serial_monitor, container, false);

        serialView = (EditText) v.findViewById(R.id.serial_text_view);
        message = (EditText) v.findViewById(R.id.serial_send_edit_text);
        send = (ImageButton) v.findViewById(R.id.serial_send_data_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                   String data =message.getText().toString();
                    callback.onSendData(data);
                }
            }
        });

        return v;
    }

    public void appendData(String data) {
        serialView.getText().append(data);
    }
}
