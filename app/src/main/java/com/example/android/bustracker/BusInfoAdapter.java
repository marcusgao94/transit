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

import java.util.ArrayList;
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
        for (Step step : steps) {
            if (step.getTravel_mode().equals("TRANSIT")) {
                route_name = step.getTransit_details().getLine().getShort_name();
                Log.i(LOG_TAG, "Route Name : " + route_name);
                break;
            }
        }
        Log.i(LOG_TAG, "until here + after position");
        TextView routeName = (TextView) listView.findViewById(R.id.departTime);
        routeName.setText(leg.getDeparture_time().getText());

        TextView routes = (TextView) listView.findViewById(R.id.arrivalTime);
        routes.setText(leg.getArrival_time().getText());

        TextView routeNum = (TextView) listView.findViewById(R.id.bus_Num);
        routeNum.setText(route_name);
//        TextView busName = (TextView) listView.findViewById(R.id.bus_name);
//        busName.setText(currentBus.getLatitude() + "");


        return listView;
    }
}
