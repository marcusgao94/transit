package com.example.android.bustracker;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.bustracker.ArrivingBus.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by yishuyan on 5/1/17.
 */

public class LatestRouteAdapter extends ArrayAdapter<Bus> {
    public static final String LOG_TAG = LatestRouteAdapter.class.getSimpleName();

    private List<Bus> busList = new ArrayList<>();

    AssetManager am = getContext().getApplicationContext().getAssets();
    Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/MontserratAlternates-Black.otf"));

    public LatestRouteAdapter(Activity context, List<Bus> buses) {
        super(context, 0, buses);
        busList = buses;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.latestbusitem, parent, false);
        }
        Log.i(LOG_TAG, "until here + get position :" + position);
        Bus bus = getItem(position);

        TextView routeNum = (TextView) listView.findViewById(R.id.bus_Num);
        routeNum.setText(bus.getRt());
        routeNum.setTypeface(typeface);

        TextView departStop = (TextView) listView.findViewById(R.id.destination);
        departStop.setText(bus.getRtdir() + " to " + bus.getDes());
        departStop.setTypeface(typeface);

        TextView arrTime = (TextView) listView.findViewById(R.id.arrivalTime);
        arrTime.setText(bus.getPrdctdn());
        arrTime.setTypeface(typeface);
        return listView;
    }
}
