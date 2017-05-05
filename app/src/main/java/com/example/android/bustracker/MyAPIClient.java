package com.example.android.bustracker;

import android.util.Log;

import com.example.android.bustracker.directions.Direction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by marcusgao on 2017/5/4.
 */

public class MyAPIClient {

    String key = "AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";

    public Direction getDirections() {
        String base = "https://maps.googleapis.com/maps/api/directions/json";
        String origin = "lotus food company pittsburgh";
        String destination = "carnegie mellon university pittsburgh";
        URI uri = UriComponentsBuilder.fromHttpUrl(base)
                .queryParam("key", key)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("mode", "transit")
                .queryParam("transit_mode", "bus")
                .build().encode().toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Direction> response = restTemplate.getForEntity(
                uri, Direction.class);
        Direction dr = response.getBody();
        Log.w(MainActivity.LOG_TAG, "status " + dr.getStatus());
        return dr;
    }
}

