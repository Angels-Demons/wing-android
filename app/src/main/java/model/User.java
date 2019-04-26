package model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import masterpiece.wing.R;

/**
 * Created by User on 11/19/2018.
 */

public class User {
    private String phone;
    private LatLng latLng;
    private int credit;
    private double latitude;
    private double longitude;

    public User(String phone, int credit, double latitude, double longitude) {
        this.phone = phone;
        this.credit = credit;
        this.latitude = latitude;
        this.longitude = longitude;
        latLng = new LatLng(latitude, longitude);
    }

    public BitmapDescriptor getIcon(){
        return BitmapDescriptorFactory.fromResource(R.drawable.person_marker);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
