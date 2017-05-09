package com.example.android.bustracker.directions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcusgao on 2017/5/8.
 */

public class Bound implements Parcelable {
    private MyLocation northeast;
    private MyLocation southwest;

    public MyLocation getNortheast() {
        return northeast;
    }

    public void setNortheast(MyLocation northeast) {
        this.northeast = northeast;
    }

    public MyLocation getSouthwest() {
        return southwest;
    }

    public void setSouthwest(MyLocation southwest) {
        this.southwest = southwest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.northeast, flags);
        dest.writeParcelable(this.southwest, flags);
    }

    public Bound() {
    }

    protected Bound(Parcel in) {
        this.northeast = in.readParcelable(MyLocation.class.getClassLoader());
        this.southwest = in.readParcelable(MyLocation.class.getClassLoader());
    }

    public static final Parcelable.Creator<Bound> CREATOR = new Parcelable.Creator<Bound>() {
        @Override
        public Bound createFromParcel(Parcel source) {
            return new Bound(source);
        }

        @Override
        public Bound[] newArray(int size) {
            return new Bound[size];
        }
    };
}
