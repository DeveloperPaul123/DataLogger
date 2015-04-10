package com.devpaul.datalogger.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devpaul.datalogger.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by Pauly D on 4/9/2015.
 */
public class LivePlotFragment extends Fragment {

    private LineChart lineChartOne, lineChartTwo, lineChartThree;

    public static LivePlotFragment newInstance() {
        LivePlotFragment lpf = new LivePlotFragment();
        return lpf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_plot, container, false);

        lineChartOne = (LineChart) v.findViewById(R.id.lineChartOne);
        lineChartTwo = (LineChart) v.findViewById(R.id.lineChartTwo);
        lineChartThree = (LineChart) v.findViewById(R.id.lineChartThree);

        lineChartOne.setHighlightEnabled(false);
        lineChartOne.setTouchEnabled(false);
        lineChartOne.setDragEnabled(false);
        lineChartOne.setScaleEnabled(false);
        lineChartOne.setDrawGridBackground(true);
        lineChartOne.setPinchZoom(false);

        lineChartTwo.setHighlightEnabled(false);
        lineChartTwo.setTouchEnabled(false);
        lineChartTwo.setDragEnabled(false);
        lineChartTwo.setScaleEnabled(false);
        lineChartTwo.setDrawGridBackground(true);
        lineChartTwo.setPinchZoom(false);

        lineChartThree.setHighlightEnabled(false);
        lineChartThree.setTouchEnabled(false);
        lineChartThree.setDragEnabled(false);
        lineChartThree.setScaleEnabled(false);
        lineChartThree.setDrawGridBackground(true);
        lineChartThree.setPinchZoom(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLUE);

        lineChartOne.setData(data);
        lineChartTwo.setData(data);
        lineChartThree.setData(data);

        // limit the number of visible entries
        lineChartOne.setVisibleXRange(200);
        lineChartTwo.setVisibleXRange(200);
        lineChartThree.setVisibleXRange(200);

        return v;
    }

    private void addEntry() {

        LineData dataOne = lineChartOne.getData();
        LineData dataTwo = lineChartTwo.getData();
        LineData dataThree = lineChartThree.getData();

        if (dataOne != null) {

            LineDataSet set = dataOne.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            dataOne.addEntry(new Entry((float) (Math.random() * 40) + 40f, set.getEntryCount()), 0);

            // let the chart know it's data has changed
            lineChartOne.notifyDataSetChanged();

            // limit the number of visible entries
            lineChartOne.setVisibleXRange(200);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            lineChartOne.moveViewToX(dataOne.getXValCount() - 201);


        }
    }
}
