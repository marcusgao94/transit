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
public class Departure_Time implements Parcelable {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
    }

    public Departure_Time() {
    }

    protected Departure_Time(Parcel in) {
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Departure_Time> CREATOR = new Parcelable.Creator<Departure_Time>() {
        @Override
        public Departure_Time createFromParcel(Parcel source) {
            return new Departure_Time(source);
        }

        @Override
        public Departure_Time[] newArray(int size) {
            return new Departure_Time[size];
        }
    };
}
