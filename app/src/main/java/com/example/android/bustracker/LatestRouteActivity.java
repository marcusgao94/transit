package com.example.android.bustracker;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.android.bustracker.ArrivingBus.Bus;
import com.example.android.bustracker.bus_station.BusStationResponse;
import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by yishuyan on 5/7/17.
 */

public class LatestRouteActivity extends AppCompatActivity {
    private List<Bus> busList;
    private MyAPIClient myAPIClient;
    private ListView listView;
    private LatestRouteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_list);
        listView = (ListView) findViewById(R.id.upcoming);
        myAPIClient = new MyAPIClient();
        String[] stpids = new String[]{"4407", "4408", "7116"};
        new ArrivingBusAsyncTask().execute(stpids);
    }

    public void setAdapter() {
        adapter = new LatestRouteAdapter(this, busList);
        listView.setAdapter(adapter);
    }

    private class ArrivingBusAsyncTask extends AsyncTask<String, Void, List<Bus>> {
        @Override
        protected List<Bus> doInBackground(String... stpid) {
            if (stpid.length < 1 || stpid[0] == null) {
                return null;
            }
            List<Bus> res = new ArrayList<Bus>();
            for (String stp : stpid) {
                List<Bus> busList = myAPIClient.getArrivingBus(stp);
                res.addAll(busList);
            }
            Collections.sort(res, new Comparator<Bus>() {
                @Override
                public int compare(Bus bus, Bus t1) {
                    int i1 = 0, i2 = 0;
                    try {
                        if (!bus.getPrdctdn().equals("DUE"))
                            i1 = Integer.valueOf(bus.getPrdctdn());
                        if (!t1.getPrdctdn().equals("DUE"))
                            i2 = Integer.valueOf(t1.getPrdctdn());
                        return i1 - i2;
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
            return res;
        }

        @Override
        protected void onPostExecute(List<Bus> buses) {
            busList = buses;
            setAdapter();
        }
    }
}
