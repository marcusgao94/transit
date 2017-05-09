package com.example.android.bustracker.directions;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcusgao on 2017/5/5.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Leg implements Parcelable {
    private Arrival_Time arrival_time;
    private Departure_Time departure_time;
    private Distance distance;
    private Duration duration;
    private String start_address;
    private String end_address;
    private MyLocation start_location;
    private MyLocation end_location;
    private List<Step> steps;

    public Arrival_Time getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Arrival_Time arrival_time) {
        this.arrival_time = arrival_time;
    }

    public Departure_Time getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Departure_Time departure_time) {
        this.departure_time = departure_time;
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

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public MyLocation getStart_location() {
        return start_location;
    }

    public void setStart_location(MyLocation start_location) {
        this.start_location = start_location;
    }

    public MyLocation getEnd_location() {
        return end_location;
    }

    public void setEnd_location(MyLocation end_location) {
        this.end_location = end_location;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Leg() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.arrival_time, flags);
        dest.writeParcelable(this.departure_time, flags);
        dest.writeParcelable(this.distance, flags);
        dest.writeParcelable(this.duration, flags);
        dest.writeString(this.start_address);
        dest.writeString(this.end_address);
        dest.writeParcelable(this.start_location, flags);
        dest.writeParcelable(this.end_location, flags);
        dest.writeTypedList(this.steps);
    }

    protected Leg(Parcel in) {
        this.arrival_time = in.readParcelable(Arrival_Time.class.getClassLoader());
        this.departure_time = in.readParcelable(Departure_Time.class.getClassLoader());
        this.distance = in.readParcelable(Distance.class.getClassLoader());
        this.duration = in.readParcelable(Duration.class.getClassLoader());
        this.start_address = in.readString();
        this.end_address = in.readString();
        this.start_location = in.readParcelable(MyLocation.class.getClassLoader());
        this.end_location = in.readParcelable(MyLocation.class.getClassLoader());
        this.steps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Leg> CREATOR = new Creator<Leg>() {
        @Override
        public Leg createFromParcel(Parcel source) {
            return new Leg(source);
        }

        @Override
        public Leg[] newArray(int size) {
            return new Leg[size];
        }
    };
}
