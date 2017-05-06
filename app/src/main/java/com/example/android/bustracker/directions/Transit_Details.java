package com.example.android.bustracker.directions;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * Created by yishuyan on 5/6/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Transit_Details implements Parcelable {
    private Arrival_Stop arrival_stop;
    private Arrival_Time arrival_time;
    private Departure_Stop departure_stop;
    private Departure_Time departure_time;
    private String headSign;
    private Line line;

    public Arrival_Stop getArrival_stop() {
        return arrival_stop;
    }

    public void setArrival_stop(Arrival_Stop arrival_stop) {
        this.arrival_stop = arrival_stop;
    }

    public Arrival_Time getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Arrival_Time arrival_time) {
        this.arrival_time = arrival_time;
    }

    public Departure_Stop getDeparture_stop() {
        return departure_stop;
    }

    public void setDeparture_stop(Departure_Stop departure_stop) {
        this.departure_stop = departure_stop;
    }

    public Departure_Time getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Departure_Time departure_time) {
        this.departure_time = departure_time;
    }

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getNum_stops() {
        return num_stops;
    }

    public void setNum_stops(int num_stops) {
        this.num_stops = num_stops;
    }

    private int num_stops;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.arrival_stop, flags);
        dest.writeParcelable(this.arrival_time, flags);
        dest.writeParcelable(this.departure_stop, flags);
        dest.writeParcelable(this.departure_time, flags);
        dest.writeString(this.headSign);
        dest.writeParcelable(this.line, flags);
        dest.writeInt(this.num_stops);
    }

    public Transit_Details() {
    }

    protected Transit_Details(Parcel in) {
        this.arrival_stop = in.readParcelable(Arrival_Stop.class.getClassLoader());
        this.arrival_time = in.readParcelable(Arrival_Time.class.getClassLoader());
        this.departure_stop = in.readParcelable(Departure_Stop.class.getClassLoader());
        this.departure_time = in.readParcelable(Departure_Time.class.getClassLoader());
        this.headSign = in.readString();
        this.line = in.readParcelable(Line.class.getClassLoader());
        this.num_stops = in.readInt();
    }

    public static final Parcelable.Creator<Transit_Details> CREATOR = new Parcelable.Creator<Transit_Details>() {
        @Override
        public Transit_Details createFromParcel(Parcel source) {
            return new Transit_Details(source);
        }

        @Override
        public Transit_Details[] newArray(int size) {
            return new Transit_Details[size];
        }
    };
}
