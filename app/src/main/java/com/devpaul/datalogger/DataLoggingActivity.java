package com.devpaul.datalogger;

import android.bluetooth.BluetoothDevice;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.devpaul.bluetoothutillib.abstracts.SupportBaseBluetoothActivity;
import com.devpaul.datalogger.fragments.SerialMonitorFragment;
import com.devpaul.datalogger.fragments.SubjectDetailFragment;
import com.devpaul.datalogger.utils.FileWriter;

/**
 * Created by Pauly D on 3/16/2015.
 */
public class DataLoggingActivity extends SupportBaseBluetoothActivity implements ViewPager.OnPageChangeListener{

    public static final String SUBJECT_ID = "SUBJECT_ID";

    private MyPagerAdapter myPagerAdapter;

    private Fragment curFragment;

    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager viewPager;

    private SubjectDetailFragment subjectDetailFragment;
    private SerialMonitorFragment serialMonitorFragment;

    private boolean isTablet;
    private boolean isRecording;

    private FileWriter fileWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logging);

        isRecording = false;

        long id = getIntent().getLongExtra(SUBJECT_ID, 0);
        Bundle bundle = new Bundle();
        bundle.putLong("SUBJECT_ID", id);

        Resources res = getResources();
        isTablet = res.getBoolean(R.bool.isTablet);
        if(isTablet) {
            subjectDetailFragment = SubjectDetailFragment.newInstance();
            subjectDetailFragment.setArguments(bundle);
            serialMonitorFragment = SerialMonitorFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_left, subjectDetailFragment)
                    .add(R.id.container_right, serialMonitorFragment)
                    .commit();

        } else {
            viewPager = (ViewPager) findViewById(R.id.pager);
            pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
            myPagerAdapter.setSubjectId(id);
            viewPager.setAdapter(myPagerAdapter);
            pagerSlidingTabStrip.setViewPager(viewPager);
            pagerSlidingTabStrip.setOnPageChangeListener(this);
        }


    }

    public void startRecording(String filename) {
        fileWriter = new FileWriter(filename);
        isRecording = true;
    }

    public void stopRecording() {
        if(fileWriter != null) {
            fileWriter.closeFile();
        }
        isRecording = false;
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
        if(isRecording) {
            if(fileWriter != null) {
                fileWriter.writeData(bytes);
            }
        }
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curFragment = myPagerAdapter.getCurFragment(position);
        setTitle(myPagerAdapter.getPageTitle(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Details", "Serial Monitor", "Graphs" };
        private Bundle bundle;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setSubjectId(long id) {
            bundle = new Bundle();
            bundle.putLong("SUBJECT_ID", id);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        public Fragment getCurFragment(int position) {
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

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment fragment = SubjectDetailFragment.newInstance();
                    fragment.setArguments(bundle);
                    return fragment;
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
