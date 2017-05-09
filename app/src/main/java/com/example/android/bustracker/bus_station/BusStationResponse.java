package com.example.android.bustracker.bus_station;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by marcusgao on 2017/5/9.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusStationResponse implements Parcelable {
    private String status;
    private List<BusStation> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BusStation> getResults() {
        return results;
    }

    public void setResults(List<BusStation> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeTypedList(this.results);
    }

    public BusStationResponse() {
    }

    protected BusStationResponse(Parcel in) {
        this.status = in.readString();
        this.results = in.createTypedArrayList(BusStation.CREATOR);
    }

    public static final Parcelable.Creator<BusStationResponse> CREATOR = new Parcelable.Creator<BusStationResponse>() {
        @Override
        public BusStationResponse createFromParcel(Parcel source) {
            return new BusStationResponse(source);
        }

        @Override
        public BusStationResponse[] newArray(int size) {
            return new BusStationResponse[size];
        }
    };
}
