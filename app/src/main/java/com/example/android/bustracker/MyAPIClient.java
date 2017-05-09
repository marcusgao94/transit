package com.example.android.bustracker;

import android.util.Log;

import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by marcusgao on 2017/5/4.
 */

public class MyAPIClient {

    public static final String LOG_TAG = MyAPIClient.class.getSimpleName();
    String url;
    public MyAPIClient (String url) {
        this.url = url;
    }
    public Direction getDirections() {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("mode", "transit")
                .queryParam("transit_mode", "bus")
                .queryParam("alternatives", "true")
                .build().encode().toUri();
        Log.w(LOG_TAG, uri+"");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Direction> response = restTemplate.getForEntity(
                uri, Direction.class);
        Direction dr = response.getBody();
        Route route = dr.getRoutes().get(0);
        Log.w(MainActivity.LOG_TAG, "status " + dr.getStatus());
        return dr;
    }
}

