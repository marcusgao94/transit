package com.example.android.bustracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bustracker.directions.Leg;
import com.example.android.bustracker.directions.Step;

import java.util.List;

/**
 * Created by yishuyan on 5/4/17.
 */

public class RouteDetail extends AppCompatActivity {

    public static final String LOG_TAG = RouteDetail.class.getSimpleName();
    ListView listView;

    StepsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_detail);
        Intent intent = getIntent();
        Leg leg = (Leg) intent.getParcelableExtra("legDetail");

        TextView departView = (TextView) findViewById(R.id.estimated_depart);
        departView.setText(leg.getDeparture_time().getText());

        TextView arrivalView = (TextView) findViewById(R.id.estimated_arrive);
        arrivalView.setText(leg.getArrival_time().getText());
        TextView distanceView = (TextView) findViewById(R.id.total_distance);
        distanceView.setText(leg.getDistance().getText());

        TextView durationView = (TextView) findViewById(R.id.total_time);
        durationView.setText(leg.getDuration().getText());

        listView = (ListView) findViewById(R.id.step_view);
        List<Step> stepList = leg.getSteps();
        adapter = new StepsAdapter(this, stepList);
        listView.setAdapter(adapter);
    }
}
