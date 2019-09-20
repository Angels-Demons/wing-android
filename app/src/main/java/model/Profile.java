package model;

import android.support.annotation.NonNull;

import helper.PreferenceManager;

/**
 * Created by User on 11/28/2018.
 */

public class Profile {
    private boolean is_riding;
    private boolean ride_is_verified;
    private int credit;
    private int ride_id;
    private String timer;
    private int battery;

    public Profile(boolean is_riding, boolean ride_is_verified, int credit, int ride_id, String timer, int battery) {
        this.is_riding = is_riding;
        this.ride_is_verified = ride_is_verified;
        this.credit = credit;
        this.ride_id = ride_id;
        this.timer = timer;
        this.battery = battery;
    }


    public boolean isIs_riding() {
        return is_riding;
    }

    public String getCredit() {
        return String.valueOf(credit);
    }

    public int getRide_id() {
        return ride_id;
    }

    public String getTimer() {
        int seconds = (int) Float.parseFloat(this.timer);
//        int seconds = Integer.parseInt(this.timer);
        int minutes = seconds/60;
        seconds = seconds % 60;
        return minutes + ":" + seconds;
    }

    public String getTimer(int afterNSeconds){
        int seconds = (int) Float.parseFloat(this.timer) + afterNSeconds;
//        int seconds = Integer.parseInt(this.timer);
        int minutes = seconds/60;
        seconds = seconds % 60;
        String sec = String.valueOf(seconds);
        String min = String.valueOf(minutes);
        if (seconds < 10)
            sec = "0" + sec;
        if (minutes < 10)
            min = "0" + min;
//        min = "0" + min;

        return min + ":" + sec;
    }

    public void setIs_riding(boolean is_riding, PreferenceManager preferenceManager) {
        this.is_riding = is_riding;
        preferenceManager.setProfile(this);
    }

    public void setRide_id(int ride_id, PreferenceManager preferenceManager) {
        this.ride_id = ride_id;
        preferenceManager.setProfile(this);
    }

    public boolean isRide_is_verified() {
        return ride_is_verified;
    }

    public void setRide_is_verified(boolean ride_is_verified) {
        this.ride_is_verified = ride_is_verified;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    @NonNull
    @Override
    public String toString() {
        return "Profile{" +
                "is_riding=" + is_riding +
                ", ride_is_verified=" + ride_is_verified +
                ", credit=" + credit +
                ", ride_id=" + ride_id +
                ", timer='" + timer + '\'' +
                ", battery=" + battery +
                '}';
    }
}
