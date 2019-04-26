package model;

import helper.PreferenceManager;

/**
 * Created by User on 11/28/2018.
 */

public class Profile {
    boolean is_riding;
    int credit;
    int ride_id;
    String timer;

    public Profile(boolean is_riding, int credit, int ride_id, String timer) {
        this.is_riding = is_riding;
        this.credit = credit;
        this.ride_id = ride_id;
        this.timer = timer;
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

    @Override
    public String toString() {
        return "Profile{" +
                "is_riding=" + is_riding +
                ", credit=" + credit +
                ", ride_id=" + ride_id +
                ", timer='" + timer + '\'' +
                '}';
    }
}
