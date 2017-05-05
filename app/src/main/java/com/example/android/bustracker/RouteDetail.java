package com.example.android.bustracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by yishuyan on 5/4/17.
 */

public class RouteDetail extends AppCompatActivity {

    public static final String LOG_TAG = RouteDetail.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_detail);
        Intent intent = getIntent();
        String detailJson = (String) intent.getExtras().get("routeDetail");
        Log.i(LOG_TAG, detailJson);
    }
}
