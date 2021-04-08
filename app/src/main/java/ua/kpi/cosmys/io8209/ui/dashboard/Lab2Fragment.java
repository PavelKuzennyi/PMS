package ua.kpi.cosmys.io8209.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;

import java.util.ArrayList;
import java.util.List;

import ua.kpi.cosmys.io8209.R;


public class Lab2Fragment extends Fragment {
    private GraphView coordPlot;
    private PieChart pieChart;
    private static short togglePosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lab2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coordPlot = (GraphView) view.findViewById(R.id.coordPlot);
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        ToggleSwitch toggleSwitch = (ToggleSwitch) view.findViewById(R.id.toggleGraphs);
        pieChart.setUsePercentValues(true);

        toggleSwitch.setOnChangeListener(new ToggleSwitch.OnChangeListener() {
            @Override
            public void onToggleSwitchChanged(int position) {
                if (position == 0) {
                    drawPlot();
                    togglePosition = 0;
                } else {
                    drawPieDiagram();
                    togglePosition = 1;
                }
            }
        });

        toggleSwitch.setCheckedPosition(togglePosition);
        if (togglePosition == 0) {
            drawPlot();
        } else {
            drawPieDiagram();
        }
    }

    public void drawPlot() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        pieChart.setVisibility(View.INVISIBLE);
        coordPlot.setVisibility(View.VISIBLE);
        double x, y;
        x = -Math.PI;
        int points = 600;

        for (int i = 0; i < points; i++) {
            x += 0.1;
            y = Math.cos(x);
            series.appendData(new DataPoint(x, y), true, 600);
        }
        series.setAnimated(true);
        series.setColor(Color.BLUE);
        coordPlot.removeAllSeries();
        coordPlot.addSeries(series);

        coordPlot.getViewport().setXAxisBoundsManual(true);
        coordPlot.getViewport().setMinX(-Math.PI);
        coordPlot.getViewport().setMaxX(Math.PI);

        coordPlot.getViewport().setYAxisBoundsManual(true);
        coordPlot.getViewport().setMinY(-2);
        coordPlot.getViewport().setMaxY(2);

        coordPlot.getViewport().setScalable(true);
        coordPlot.getViewport().setScalableY(true);

        coordPlot.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        coordPlot.getGridLabelRenderer().setVerticalLabelsVisible(false);
    }

    public void drawPieDiagram() {
        coordPlot.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.VISIBLE);

        List<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(25f));
        values.add(new PieEntry(45f));
        values.add(new PieEntry(5f));
        values.add(new PieEntry(25f));

        PieDataSet pieDataSet = new PieDataSet(values, "Pie Chart");

        final int[] PIE_COLORS = {
                Color.rgb(133,133,133),
                Color.rgb(0,191,255),
                Color.rgb(90,0,157),
                Color.rgb(255,255,0)};

        ArrayList<Integer> colors = new ArrayList<>();

        for(int i: PIE_COLORS) {
            colors.add(i);
        }

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieData.setValueTextSize(0);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.animateY(500, Easing.EaseInOutExpo);
    }
}