package com.example.android.bustracker.bus_station;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by marcusgao on 2017/5/9.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusStation implements Parcelable {
    private Geometry geometry;
    private String name;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.geometry, flags);
        dest.writeString(this.name);
    }

    public BusStation() {
    }

    protected BusStation(Parcel in) {
        this.geometry = in.readParcelable(Geometry.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<BusStation> CREATOR = new Parcelable.Creator<BusStation>() {
        @Override
        public BusStation createFromParcel(Parcel source) {
            return new BusStation(source);
        }

        @Override
        public BusStation[] newArray(int size) {
            return new BusStation[size];
        }
    };
}
