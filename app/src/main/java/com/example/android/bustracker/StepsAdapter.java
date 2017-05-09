package com.example.android.bustracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView imageView = (ImageView) listView.findViewById(R.id.mode_icon);
        TextView infoView = (TextView) listView.findViewById(R.id.transit_info);
        TextView stopNum = (TextView) listView.findViewById(R.id.stops_num);

//        TextView transit_distance = (TextView) listView.findViewById(R.id.transit_distance);
//        transit_distance.setText(currentStep.getDistance().getText());

//        TextView transit_duration = (TextView) listView.findViewById(R.id.transit_duration);
//        transit_duration.setText(currentStep.getDuration().getText());

        String mode = currentStep.getTravel_mode();

        if (mode.equals("TRANSIT")) {
            imageView.setImageResource(R.drawable.bus);

            String transitMode = mode + "    " + currentStep.getTransit_details().getLine().getShort_name() + " ";

            travel_mode.setText(transitMode);

            String departAt = currentStep.getTransit_details().getDeparture_time().getText();
            String departFrom = currentStep.getTransit_details().getDeparture_stop().getName();
            String transitInfo = departAt + " from " + departFrom;

            infoView.setText(transitInfo);


//            stopNum.setText(currentStep.getTransit_details().getNum_stops() + "");
//            stopNum.setVisibility(View.VISIBLE);



//            TextView arrivalStop = (TextView) listView.findViewById(R.id.arrive_stop);
//            arrivalStop.setText(currentStep.getTransit_details().getArrival_stop().getName());
//
//            TextView arriveTime = (TextView) listView.findViewById(R.id.arrive_at);
//            arriveTime.setText(currentStep.getTransit_details().getArrival_time().getText());

        } else {

            imageView.setImageResource(R.drawable.walk);

            travel_mode.setText(mode);
            String duration = currentStep.getDuration().getText();
            infoView.setText(duration);
//            stopNum.setVisibility(View.INVISIBLE);
        }

        return listView;
    }
}
