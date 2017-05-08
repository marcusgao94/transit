package com.example.android.bustracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.bustracker.directions.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yishuyan on 5/6/17.
 */

public class StepsAdapter extends ArrayAdapter<Step> {
    public static final String LOG_TAG = BusInfoAdapter.class.getSimpleName();
    private List<Step> stepList = new ArrayList<>();
    public StepsAdapter(Activity context, List<Step> steps) {
        super(context, 0, steps);
        stepList = steps;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.step_list, parent, false);
        }
        Step currentStep = getItem(position);

        TextView travel_mode = (TextView) listView.findViewById(R.id.travel_mode);
        travel_mode.setText(currentStep.getTravel_mode());

//        TextView transit_distance = (TextView) listView.findViewById(R.id.transit_distance);
//        transit_distance.setText(currentStep.getDistance().getText());

//        TextView transit_duration = (TextView) listView.findViewById(R.id.transit_duration);
//        transit_duration.setText(currentStep.getDuration().getText());

        if (currentStep.getTravel_mode().equals("TRANSIT")) {
            TextView lineNum = (TextView) listView.findViewById(R.id.line_num);
            lineNum.setText(currentStep.getTransit_details().getNum_stops() + "");

            TextView departStop = (TextView) listView.findViewById(R.id.depart_stop);
            departStop.setText(currentStep.getTransit_details().getDeparture_stop().getName());

            TextView departTime = (TextView) listView.findViewById(R.id.depart_at);
            departTime.setText(currentStep.getTransit_details().getDeparture_time().getText());

//            TextView arrivalStop = (TextView) listView.findViewById(R.id.arrive_stop);
//            arrivalStop.setText(currentStep.getTransit_details().getArrival_stop().getName());
//
//            TextView arriveTime = (TextView) listView.findViewById(R.id.arrive_at);
//            arriveTime.setText(currentStep.getTransit_details().getArrival_time().getText());

        }


        return listView;
    }
}
