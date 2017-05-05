package com.example.android.bustracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by marcusgao on 2017/5/4.
 */

public class Directions {



    public void getDirections() {
        String base = "https://maps.googleapis.com/maps/api/directions/json";
        String key = "AIzaSyAEBBlM76YfcI7of-9Q5UkTM2O_NBjGaNw";
        String origin = "amberson plaza pittsburgh";
        String destination = "carnegie mellon university pittsburgh";
        URI uri = UriComponentsBuilder.fromHttpUrl(base)
                .queryParam("key", key)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .build().encode().toUri();
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("here again!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!111");
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println(response.getBody() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}

