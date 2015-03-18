package com.devpaul.datalogger;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import com.devpaul.bluetoothutillib.utils.BaseBluetoothActivity;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.datalogger.data.DataSource;
import com.devpaul.datalogger.data.Subject;

/**
 * Created by Pauly D on 3/16/2015.
 */
public class DataLoggingActivity extends BaseBluetoothActivity {

    private DataSource dataSource;
    private Subject curSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("SUBJECT_ID", 0);

        dataSource = new DataSource(this);
        dataSource.open();
        if(id != 0) {
            curSubject = dataSource.getSubjectById(id);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSimpleBluetooth().scan(BluetoothUtility.REQUEST_BLUETOOTH_SCAN);
    }

    @Override
    public void onBluetoothEnabled() {
        requestScan(BluetoothUtility.REQUEST_BLUETOOTH_SCAN);
    }


    @Override
    public void onBluetoothDataReceived(byte[] bytes, String s) {

    }

    @Override
    public void onDeviceConnected(BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void onDiscoveryFinished() {

    }

    @Override
    public void onDiscoveryStarted() {

    }

    @Override
    public void onDeviceFound(BluetoothDevice bluetoothDevice) {

    }
}
