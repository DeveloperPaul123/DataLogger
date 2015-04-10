package com.devpaul.datalogger.utils;

import android.os.Handler;
import android.widget.TextView;

import java.lang.ref.SoftReference;

/**
 * Created by Pauly D on 4/7/2015.
 */
public class TimerGenerator {

    /**
     * Handler
     */
    private Handler timerHandler;

    /**
     * Runnable for timer.
     */
    private Runnable timerRunnable;

    /**
     * SoftReference to {@code TextView} widget.
     */
    private SoftReference<TextView> softReference;

    /**
     * Start time
     */
    private long startTime;

    /**
     * Callback
     */
    private TimerCallback callback;

    public static interface TimerCallback {
        public void onTimeUpdate(long millis, int seconds, int minutes);
    }

    /**
     * Constructor with text view.
     * @param textView a text view to update the time in.
     */
    public TimerGenerator(TextView textView) {
        softReference = new SoftReference<TextView>(textView);
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int leftMillis = (int) millis%1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                updateTextView(minutes, seconds, leftMillis);
                if(callback != null) {
                    callback.onTimeUpdate(leftMillis, seconds, minutes);
                }
                timerHandler.postDelayed(this, 50);
            }

            private void updateTextView(int minutes, int sec, int leftMillis) {
                if(softReference != null) {
                    TextView timerTextView = softReference.get();
                    if (timerTextView != null) {
                        timerTextView.setText(String.format("%d:%02d:%03d", minutes, sec, leftMillis));
                    }
                }
            }
        };
    }

    /**
     * Constructor with callback.
     * @param timerCallback
     */
    public TimerGenerator(TimerCallback timerCallback) {
        this.callback = timerCallback;
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int leftMillis = (int) millis%1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                updateTextView(minutes, seconds, leftMillis);
                if(callback != null) {
                    callback.onTimeUpdate(leftMillis, seconds, minutes);
                }
                timerHandler.postDelayed(this, 50);
            }

            private void updateTextView(int minutes, int sec, int leftMillis) {
                if(softReference != null) {
                    TextView timerTextView = softReference.get();
                    if (timerTextView != null) {
                        timerTextView.setText(String.format("%d:%02d:%03d", minutes, sec, leftMillis));
                    }
                }
            }
        };
    }

    /**
     * Starts the timer.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    /**
     * Stops and resets the timer.
     */
    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
        startTime = 0;
        TextView t = softReference.get();
        if(t!=null) {
            t.setText("");
        }
    }

    /**
     * Destroy references and callback. Call this when you're done with the timer.
     * Good place is in onDestroy().
     */
    public void destroy() {
        callback = null;
        softReference.clear();
    }
}
