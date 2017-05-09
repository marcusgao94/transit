package com.example.android.bustracker;

import android.location.Location;
import android.util.Log;

import com.example.android.bustracker.bus_station.BusStation;
import com.example.android.bustracker.bus_station.BusStationResponse;
import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Route;
import com.google.android.gms.maps.model.LatLng;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by marcusgao on 2017/5/4.
 */

public class MyAPIClient {

    public static final String LOG_TAG = MyAPIClient.class.getSimpleName();
    private String key = "AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";
    private RestTemplate restTemplate = new RestTemplate();

    public MyAPIClient () { }

    public Direction getDirections(String url) {
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
        return dr;
    }

    public BusStationResponse getBusStops(Location location) {
        String loc = String.valueOf(location.getLatitude()) + "," + location.getLongitude();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("key", key)
                .queryParam("location", loc)
                .queryParam("type", "bus_station")
                .queryParam("rankby", "distance")
                .build().encode().toUri();
        Log.w(LOG_TAG, uri.toString());
        ResponseEntity<BusStationResponse> bsr = restTemplate.getForEntity(
                uri, BusStationResponse.class);
        return bsr.getBody();
    }



}

