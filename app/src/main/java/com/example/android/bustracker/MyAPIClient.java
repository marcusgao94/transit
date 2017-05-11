package com.example.android.bustracker;

import android.location.Location;
import android.util.Log;

import com.example.android.bustracker.ArrivingBus.Bus;
import com.example.android.bustracker.bus_station.BusStation;
import com.example.android.bustracker.bus_station.BusStationResponse;
import com.example.android.bustracker.directions.Direction;
import com.example.android.bustracker.directions.Route;
import com.google.android.gms.maps.model.LatLng;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;

import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    public List<Bus> getArrivingBus(String stpid) {
        String url = "http://realtime.portauthority.org/bustime/api/v3/getpredictions?" +
                "key=BeDXd3nFDWGXAEH49nKTPv2LR&rtpidatafeed=Port Authority Bus";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("stpid", stpid)
                .build().encode().toUri();
        ResponseEntity<String> busstring = restTemplate.getForEntity(
                uri, String.class);
        InputSource is = new InputSource(new StringReader(busstring.getBody()));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder document = factory.newDocumentBuilder();
            Document doc = document.parse(is);
            NodeList nodeList = doc.getElementsByTagName("prd");
            List<Bus> busList = new ArrayList();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Bus bus = new Bus();
                    bus.setRt(element.getElementsByTagName("rt").item(0).getTextContent());
                    bus.setRtdir(element.getElementsByTagName("rtdir").item(0).getTextContent());
                    bus.setPrdctdn(element.getElementsByTagName("prdctdn").item(0).getTextContent());

                    int t = 0;
                    String time = bus.getPrdctdn();
                    if (!time.equals("DUE"))
                        t = Integer.valueOf(time);
                    if (t > 10)
                        continue;
                    busList.add(bus);
                }
            }
            return busList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(LOG_TAG, busstring.getBody());
        return null;
    }



}

