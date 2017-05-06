package com.example.android.bustracker.directions;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * Created by marcusgao on 2017/5/5.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Step implements Parcelable {
    private String travel_mode;
    private String html_instructions;
    private Distance distance;
    private Duration duration;
    private Polyline polyline;
    private Transit_Details transit_details;

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Transit_Details getTransit_details() {
        return transit_details;
    }

    public void setTransit_details(Transit_Details transit_details) {
        this.transit_details = transit_details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.travel_mode);
        dest.writeString(this.html_instructions);
        dest.writeParcelable(this.distance, flags);
        dest.writeParcelable(this.duration, flags);
        dest.writeParcelable(this.polyline, flags);
        dest.writeParcelable(this.transit_details, flags);
    }

    public Step() {
    }

    protected Step(Parcel in) {
        this.travel_mode = in.readString();
        this.html_instructions = in.readString();
        this.distance = in.readParcelable(Distance.class.getClassLoader());
        this.duration = in.readParcelable(Duration.class.getClassLoader());
        this.polyline = in.readParcelable(Polyline.class.getClassLoader());
        this.transit_details = in.readParcelable(Transit_Details.class.getClassLoader());
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
