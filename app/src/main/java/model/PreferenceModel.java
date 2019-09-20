package model;

import enums.StateMainActivity;
import helper.PreferenceManager;

/**
 * Created by User on 11/26/2018.
 */

public class PreferenceModel {
    private StateMainActivity state;
    private boolean isFirstTimeLaunch;
    private boolean isLoggedIn;
    private String phone;
    private String token;
    private String rideId;

    public PreferenceModel() {
        this.state = StateMainActivity.NOTHING_IS_SELECTED;
        this.isFirstTimeLaunch = true;
        this.isLoggedIn = false;
        this.phone = "";
        this.token = "dummy token";
        this.rideId = "dummy ride id";
    }

    public StateMainActivity getState() {
        return state;
    }

    public void setState(StateMainActivity state, PreferenceManager preferenceManager) {
        this.state = state;
        preferenceManager.setPreferenceModel(this);
//        System.out.println(this.state);
    }

    public boolean isFirstTimeLaunch() {
        return isFirstTimeLaunch;
    }

    public void setFirstTimeLaunch(boolean firstTimeLaunch, PreferenceManager preferenceManager) {
        isFirstTimeLaunch = firstTimeLaunch;
        preferenceManager.setPreferenceModel(this);

    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn, PreferenceManager preferenceManager) {
        isLoggedIn = loggedIn;
        preferenceManager.setPreferenceModel(this);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone, PreferenceManager preferenceManager) {
        this.phone = phone;
        preferenceManager.setPreferenceModel(this);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token, PreferenceManager preferenceManager) {
        this.token = token;
        preferenceManager.setPreferenceModel(this);
    }

//    public String getRideId() {
//        return rideId;
//    }

//    public void setRideId(String rideId, PreferenceManager preferenceManager) {
//        this.rideId = rideId;
//        preferenceManager.setPreferenceModel(this);
//    }
}
