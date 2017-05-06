package com.example.android.bustracker.directions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcusgao on 2017/5/5.
 */

public class Time implements Parcelable {
    private String text;
    private String time_zone;
    private Long value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.time_zone);
        dest.writeValue(this.value);
    }

    public Time() {
    }

    protected Time(Parcel in) {
        this.text = in.readString();
        this.time_zone = in.readString();
        this.value = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Time> CREATOR = new Parcelable.Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel source) {
            return new Time(source);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };
}
