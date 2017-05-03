package com.example.android.bustracker;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yishuyan on 5/1/17.
 */

public class BusInfoAdapter extends ArrayAdapter<BusInfo>{
    public static final String LOG_TAG = BusInfoAdapter.class.getSimpleName();
    private List<BusInfo> busList = new ArrayList<>();
    public BusInfoAdapter(Activity context, List<BusInfo> buses) {
        super(context, 0, buses);
        busList = buses;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        Log.i(LOG_TAG, "until here + get position :" + position);
        BusInfo currentBus = getItem(position);
        Log.i(LOG_TAG, "until here + after position");
        TextView routeName = (TextView) listView.findViewById(R.id.departTime);
        routeName.setText(currentBus.getStartTime() + "");

        TextView routes = (TextView) listView.findViewById(R.id.arrivalTime);
        routes.setText(currentBus.getEndTime() + "");

        TextView routeNum = (TextView) listView.findViewById(R.id.bus_Num);
        routeNum.setText(currentBus.getRouteNum() + "");
//        TextView busName = (TextView) listView.findViewById(R.id.bus_name);
//        busName.setText(currentBus.getLatitude() + "");


        return listView;
    }
}
