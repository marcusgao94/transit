package com.example.android.bustracker;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bustracker.ArrivingBus.Bus;
import com.example.android.bustracker.bus_station.BusStationResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by yishuyan on 5/7/17.
 */

public class LatestRouteActivity extends AppCompatActivity {
    private List<Bus> busList;
    private MyAPIClient myAPIClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAPIClient = new MyAPIClient();
        String[] stpids = new String[]{"4407", "4408", "7116"};
        new ArrivingBusAsyncTask().execute(stpids);
    }

    private class ArrivingBusAsyncTask extends AsyncTask<String, Void, List<Bus>> {
        @Override
        protected List<Bus> doInBackground(String... stpid) {
            if (stpid.length < 1 || stpid[0] == null) {
                return null;
            }
            List<Bus> res = new ArrayList<Bus>();
            for (String stp : stpid) {
                List<Bus> busList = myAPIClient.getArrivingBus(stpid[0]);
                res.addAll(busList);
            }
            res.sort(new Comparator<Bus>() {
                @Override
                public int compare(Bus bus, Bus t1) {
                    return Integer.valueOf(bus.getPrdctdn()) - Integer.valueOf(t1.getPrdctdn());
                }
            });
            return res;
        }

        @Override
        protected void onPostExecute(List<Bus> buses) {
            busList = buses;
        }
    }
}
