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
public class Arrival_Time implements Parcelable {
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

    public Arrival_Time() {
    }

    protected Arrival_Time(Parcel in) {
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Arrival_Time> CREATOR = new Parcelable.Creator<Arrival_Time>() {
        @Override
        public Arrival_Time createFromParcel(Parcel source) {
            return new Arrival_Time(source);
        }

        @Override
        public Arrival_Time[] newArray(int size) {
            return new Arrival_Time[size];
        }
    };
}
