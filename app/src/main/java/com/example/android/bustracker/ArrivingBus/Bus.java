package com.example.android.bustracker.ArrivingBus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcusgao on 2017/5/10.
 */

public class Bus implements Parcelable {
    private String stpnm;
    private String stpid;
    private String vid;
    private String rt;
    private String rtdir;
    private String prdtm;
    private String prdctdn;
    private String des;

    public String getStpnm() {
        return stpnm;
    }

    public void setStpnm(String stpnm) {
        this.stpnm = stpnm;
    }

    public String getStpid() {
        return stpid;
    }

    public void setStpid(String stpid) {
        this.stpid = stpid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
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

    public String getPrdtm() {
        return prdtm;
    }

    public void setPrdtm(String prdtm) {
        this.prdtm = prdtm;
    }

    public String getPrdctdn() {
        return prdctdn;
    }

    public void setPrdctdn(String prdctdn) {
        this.prdctdn = prdctdn;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stpnm);
        dest.writeString(this.stpid);
        dest.writeString(this.vid);
        dest.writeString(this.rt);
        dest.writeString(this.rtdir);
        dest.writeString(this.prdtm);
        dest.writeString(this.prdctdn);
        dest.writeString(this.des);
    }

    public Bus() {
    }

    protected Bus(Parcel in) {
        this.stpnm = in.readString();
        this.stpid = in.readString();
        this.vid = in.readString();
        this.rt = in.readString();
        this.rtdir = in.readString();
        this.prdtm = in.readString();
        this.prdctdn = in.readString();
        this.des = in.readString();
    }

    public static final Parcelable.Creator<Bus> CREATOR = new Parcelable.Creator<Bus>() {
        @Override
        public Bus createFromParcel(Parcel source) {
            return new Bus(source);
        }

        @Override
        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };
}
