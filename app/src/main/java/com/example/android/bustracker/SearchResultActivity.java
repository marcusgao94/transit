package com.example.android.bustracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Route;

import java.util.List;

/**
 * Created by yishuyan on 5/7/17.
 */

public class SearchResultActivity extends AppCompatActivity {
    ListView listView;
    BusInfoAdapter adapter;
    Direction direction;
    List<Route> lists;

    public static final String LOG_TAG = SearchResultActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        Log.w(LOG_TAG, "here");
        Intent intent = getIntent();
        direction = (Direction) intent.getParcelableExtra("search_result");
        Log.w(LOG_TAG, "here2");
        lists = direction.getRoutes();

        listView = (ListView) findViewById(R.id.list_view);

        adapter = new BusInfoAdapter(this,lists);
        listView.setAdapter(adapter);
        // Check one route, and check the detail route information in another screen.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SearchResultActivity.this, RouteDetail.class);
                if (lists == null) {
                    Log.i(LOG_TAG, "buses is null");
                }
                Log.i(LOG_TAG, "buses size: " + lists.size());
                Route clickedRoute = lists.get(position);
//                Leg currentLeg = clickedRoute.getLegs().get(0);
                intent.putExtra("currentRoute", clickedRoute);
                startActivity(intent);
            }
        });

    }

}
