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
public class Departure_Stop implements Parcelable {
    private String name;

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
        dest.writeString(this.name);
    }

    public Departure_Stop() {
    }

    protected Departure_Stop(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Departure_Stop> CREATOR = new Parcelable.Creator<Departure_Stop>() {
        @Override
        public Departure_Stop createFromParcel(Parcel source) {
            return new Departure_Stop(source);
        }

        @Override
        public Departure_Stop[] newArray(int size) {
            return new Departure_Stop[size];
        }
    };
}
