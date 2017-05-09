package com.example.android.bustracker;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.bustracker.directions.Leg;
import com.example.android.bustracker.directions.Route;
import com.example.android.bustracker.directions.Step;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by yishuyan on 5/1/17.
 */

public class BusInfoAdapter extends ArrayAdapter<Route> {
    public static final String LOG_TAG = BusInfoAdapter.class.getSimpleName();
    private List<Route> busList = new ArrayList<>();
    public BusInfoAdapter(Activity context, List<Route> routes) {
        super(context, 0, routes);
        busList = routes;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        Log.i(LOG_TAG, "until here + get position :" + position);
        Route currentRoute = getItem(position);
        Leg leg = currentRoute.getLegs().get(0);
        String arrivalTime = leg.getArrival_time().getText();
        Log.i(LOG_TAG, "Arrival Time : " + arrivalTime);
        String departureTime = leg.getDeparture_time().getText();
        Log.i(LOG_TAG, "Depart Time : " + departureTime);
        List<Step> steps = leg.getSteps();
        String route_name = "";
        String depart_stop = "";
        for (Step step : steps) {
            if (step.getTravel_mode().equals("TRANSIT")) {
                route_name = step.getTransit_details().getLine().getShort_name();
                depart_stop = step.getTransit_details().getDeparture_stop().getName();
                Log.i(LOG_TAG, "Route Name : " + route_name);
                break;
            }
        }
//        Log.i(LOG_TAG, "until here + after position");
        String departTime = leg.getDeparture_time().getText();
        TextView routeName = (TextView) listView.findViewById(R.id.departTime);
        routeName.setText(departTime);

        TextView routes = (TextView) listView.findViewById(R.id.arrivalTime);
        routes.setText(leg.getArrival_time().getText());

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String[] timeSplit = formattedDate.split(":");
        String[] departSplit = departTime.split(":");
        int currMinute = Integer.valueOf(timeSplit[1]);
        Log.i(LOG_TAG, "Current Minute : " + currMinute);
        int leftMinute = Integer.valueOf(departSplit[1].substring(0,2));
        Log.i(LOG_TAG, "Left Minute : " + leftMinute);
        int resMinute = leftMinute - currMinute;
        if (resMinute < 0) {
            resMinute = resMinute + 60;
        }
        Log.i(LOG_TAG, "time left" + resMinute);
        TextView leftTime = (TextView) listView.findViewById(R.id.left_time);
        leftTime.setText(resMinute + "");



        TextView routeNum = (TextView) listView.findViewById(R.id.bus_Num);
        routeNum.setText(route_name);

        TextView departStop = (TextView) listView.findViewById(R.id.destination);
        departStop.setText(depart_stop);


        return listView;
    }
}
