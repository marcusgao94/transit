package com.example.android.bustracker;

/**
 * Created by yishuyan on 5/1/17.
 */

public class BusInfo {
    //
    private int tmstmp;
    private String rt;
    private String rtdir;
    // route name
    private String routeNum;
    // route color
    private String rtclr;
    private int stpid;
    private int vid;

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

    private String startTime, endTime;

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getRtclr() {
        return rtclr;
    }

    public void setRtclr(String rtclr) {
        this.rtclr = rtclr;
    }

    private String error;
    private double longitude;
    private double latitude;
    private String dir;

    public int getTmstmp() {
        return tmstmp;
    }

    public void setTmstmp(int tmstmp) {
        this.tmstmp = tmstmp;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRtdir() {
        return rtdir;
    }

    public void setRtdir(String rtdir) {
        this.rtdir = rtdir;
    }

    public int getStpid() {
        return stpid;
    }

    public void setStpid(int stpid) {
        this.stpid = stpid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    private String des;
    public BusInfo(String startTime, String endTime, String routeNum){
        this.routeNum = routeNum;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
