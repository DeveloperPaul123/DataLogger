package com.devpaul.datalogger;

import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.devpaul.bluetoothutillib.abstracts.SupportBaseBluetoothActivity;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.datalogger.fragments.LivePlotFragment;
import com.devpaul.datalogger.fragments.SerialMonitorFragment;
import com.devpaul.datalogger.fragments.SubjectDetailFragment;
import com.devpaul.datalogger.utils.FileWriter;
import com.devpaul.datalogger.utils.TimerGenerator;
import com.devpaul.materialfabmenu.MaterialFloatingActionButton;

/**
 * Created by Pauly D on 3/16/2015.
 */
public class DataLoggingActivity extends SupportBaseBluetoothActivity
        implements ViewPager.OnPageChangeListener, SerialMonitorFragment.Callback{

    public static final String SUBJECT_ID = "SUBJECT_ID";

    private MyPagerAdapter myPagerAdapter;

    private Fragment curFragment;

    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager viewPager;

    private SubjectDetailFragment subjectDetailFragment;
    private SerialMonitorFragment serialMonitorFragment;

    private MaterialFloatingActionButton fab;

    private boolean isTablet;
    private boolean isRecording;

    private FileWriter fileWriter;

    TextView timerTextView;

    private TimerGenerator timerGenerator;

    private boolean isGraphShowing;

    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isGraphShowing = false;
        Resources res = getResources();
        isTablet = res.getBoolean(R.bool.isTablet);
        if(isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_data_logging);

        isRecording = false;

        long id = getIntent().getLongExtra(SUBJECT_ID, 0);
        Bundle bundle = new Bundle();
        bundle.putLong("SUBJECT_ID", id);

        timerTextView = (TextView) findViewById(R.id.timer_text_view);
        timerTextView.setScaleX(0.0f);
        timerTextView.setScaleY(0.0f);

        timerGenerator = new TimerGenerator(timerTextView);

        fab = (MaterialFloatingActionButton) findViewById(R.id.timer_fab);
        fab.setScaleX(0.0f);
        fab.setScaleY(0.0f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        container = (RelativeLayout) findViewById(R.id.recoding_container);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_data_logging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect_device:
                getSimpleBluetooth().scan(BluetoothUtility.REQUEST_BLUETOOTH_SCAN);
                break;
            case R.id.menu_show_graphs:
                if(isTablet) {
                    if(!isGraphShowing) {
                        showGraphFragment();
                        item.setTitle("Hide Graph");
                        isGraphShowing = true;
                    } else {
                        hideGraphFragment();
                        item.setTitle("Show Graph");
                        isGraphShowing = false;
                    }

                }
                break;
            default:
                break;
        }
        return true;
    }

    public void showGraphFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_left, LivePlotFragment.newInstance())
                .commit();
    }

    public void hideGraphFragment() {
        long id = getIntent().getLongExtra(SUBJECT_ID, 0);
        Bundle bundle = new Bundle();
        bundle.putLong("SUBJECT_ID", id);
        subjectDetailFragment = SubjectDetailFragment.newInstance();
        subjectDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_left, subjectDetailFragment)
                .commit();
    }
    public void startRecording(String filename) {
        fileWriter = new FileWriter(filename);
        isRecording = true;
        showStuff();
        timerGenerator.start();
        if(curFragment instanceof SubjectDetailFragment) {
            ((SubjectDetailFragment) curFragment).setIsRecording(true);
        }
    }

    /**
     * Shows the button and timer text view.
     */
    private void showStuff() {
        fab.animate().cancel();
        fab.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(300)
                .start();
        timerTextView.animate().cancel();
        timerTextView.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(200)
                .start();

        if(Build.VERSION.SDK_INT >= 16) {
            container.setBackground(getResources()
                    .getDrawable(R.drawable.rectangle));
        } else {
            container.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.rectangle));
        }
    }

    public void stopRecording() {
        if(fileWriter != null) {
            fileWriter.closeFile();
        }
        isRecording = false;
        timerGenerator.stop();
        hideStuff();
        if(curFragment instanceof SubjectDetailFragment) {
            ((SubjectDetailFragment)curFragment).setIsRecording(false);
        }

        if(Build.VERSION.SDK_INT >= 16) {
            container.setBackground(null);
        } else {
            container.setBackgroundDrawable(null);
        }
    }

    /**
     * Hides the button and timer text view.
     */
    private void hideStuff() {
        fab.animate().cancel();
        fab.animate()
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setDuration(300)
                .start();
        timerTextView.animate().cancel();
        timerTextView.animate()
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setDuration(200)
                .start();
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
        if(curFragment instanceof SerialMonitorFragment) {
            ((SerialMonitorFragment)curFragment).appendData(s);
        } else if(isTablet) {
            serialMonitorFragment.appendData(s);
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
        if(curFragment instanceof SubjectDetailFragment) {
            ((SubjectDetailFragment)curFragment).setIsRecording(isRecording);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSendData(String data) {
        getSimpleBluetooth().sendData(data);
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
                    return LivePlotFragment.newInstance();
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
                    return LivePlotFragment.newInstance();
                default:
                    return SerialMonitorFragment.newInstance();
            }
        }

    }

}
