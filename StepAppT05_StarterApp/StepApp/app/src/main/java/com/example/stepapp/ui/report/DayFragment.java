package com.example.stepapp.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepapp.R;
import com.example.stepapp.StepAppOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DayFragment extends Fragment {

    public int everSteps = 0;
    TextView numStepsTextView;
    AnyChartView anyChartView;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    public Map<String, Integer> stepsByDay = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_day, container, false);

        anyChartView = root.findViewById(R.id.dayBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar));

        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

        // Add the number of steps in text view
        numStepsTextView = root.findViewById(R.id.numStepsTextView);
        everSteps = StepAppOpenHelper.loadSingleRecord_ever(getContext(), current_time);
        numStepsTextView.setText(String.valueOf(everSteps));


        return root;
    }

    public Cartesian createColumnChart() {
        //***** Read data from SQLiteDatabase *********/
        // TODO 1: Get the map with hours and number of steps for today
        //  from the database and initialize it to variable stepsByHour

        stepsByDay= StepAppOpenHelper.loadStepsByDay(getContext(),current_time);



        // TODO 2: Creating a new map that contains hours of the day from 0 to 23 and
        //  number of steps during each hour set to 0
        Map<String,Integer> graph_map = new TreeMap<>();
        for (int i = 0; i<15; i++){
            graph_map.put("",0);
        }


        // TODO 3: Replace the number of steps for each hour in graph_map
        //  with the number of steps read from the database
        graph_map.putAll(stepsByDay);


        //***** Create column chart using AnyChart library *********/
        // 1. Create and get the cartesian coordinate system for column chart
        Cartesian cartesian = AnyChart.column();

        // 2. Create data entries for x and y axis of the graph
        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        // 3. Add the data to column chart and get the columns
        Column column = cartesian.column(data);

        //***** Modify the UI of the chart *********/
        // TODO 4. Change the color of column chart and its border

        column.fill("#1EB980");
        column.stroke( "#1EB980");


        //Modifying properties of tooltip
        column.tooltip()
                .titleFormat("By day: {%X}")
                .format("{%Value}{groupsSeparator: } Steps")
                .anchor(Anchor.RIGHT_TOP);

        // TODO 5: Modify column chart tooltip properties

        column.tooltip()
                .position(Position.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);


        // Modifying properties of cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);


        // TODO 6: Modify the UI of the cartesian
        cartesian.background().fill("#00000000");
        cartesian.xAxis(0).title("Day");
        cartesian.yAxis(0).title("Number of Steps");
        cartesian.animation(Boolean.TRUE);



        return cartesian;
    }

}