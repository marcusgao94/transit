package com.example.android.bustracker.directions;

/**
 * Created by marcusgao on 2017/5/5.
 */

public class Time {
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
}
