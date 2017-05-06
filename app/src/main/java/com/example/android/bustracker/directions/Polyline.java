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
public class Polyline implements Parcelable {
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.points);
    }

    public Polyline() {
    }

    protected Polyline(Parcel in) {
        this.points = in.readString();
    }

    public static final Parcelable.Creator<Polyline> CREATOR = new Parcelable.Creator<Polyline>() {
        @Override
        public Polyline createFromParcel(Parcel source) {
            return new Polyline(source);
        }

        @Override
        public Polyline[] newArray(int size) {
            return new Polyline[size];
        }
    };
}
