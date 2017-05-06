package com.example.android.bustracker;

/**
 * Created by yishuyan on 5/1/17.
 */

public class BusInfo {
    //store all the detail information into detailJson, if user clicked the item, then parse the json in detail page.
    private String detailJson;
    private String routeNum;
    private String startTime, endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getDetailJson() {
        return detailJson;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }

    public BusInfo(String startTime, String endTime, String routeNum, String detailJson){
        this.routeNum = routeNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.detailJson = detailJson;
    }

}
