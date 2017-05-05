package com.example.android.bustracker.directions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by marcusgao on 2017/5/5.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Direction {
    private String status;
    private List<Route> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
