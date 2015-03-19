package com.devpaul.datalogger;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.devpaul.bluetoothutillib.abstracts.BaseBluetoothActivity;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.datalogger.data.DataSource;
import com.devpaul.datalogger.data.Subject;
import com.devpaul.datalogger.fragments.SerialMonitorFragment;
import com.devpaul.datalogger.fragments.SubjectDetailFragment;

/**
 * Created by Pauly D on 3/16/2015.
 */
public class DataLoggingActivity extends BaseBluetoothActivity {

    private DataSource dataSource;
    private Subject curSubject;
    private MyPagerAdapter myPagerAdapter;

    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logging);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
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
        super.onBluetoothEnabled();
    }

    @Override
    public void onDeviceSelected(String macAddress) {
        super.onDeviceSelected(macAddress);
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

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Details", "Serial Monitor", "Graphs" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SubjectDetailFragment.newInstance();
                case 1:
                    return SerialMonitorFragment.newInstance();
                case 2:
                    return SerialMonitorFragment.newInstance();
                default:
                    return SerialMonitorFragment.newInstance();
            }
        }

    }

}
