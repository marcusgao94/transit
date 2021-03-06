package com.example.android.bustracker.directions;

/**
 * Created by marcusgao on 2017/5/5.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fare implements Parcelable {
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

    public Fare() {
    }

    protected Fare(Parcel in) {
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Fare> CREATOR = new Parcelable.Creator<Fare>() {
        @Override
        public Fare createFromParcel(Parcel source) {
            return new Fare(source);
        }

        @Override
        public Fare[] newArray(int size) {
            return new Fare[size];
        }
    };
}
