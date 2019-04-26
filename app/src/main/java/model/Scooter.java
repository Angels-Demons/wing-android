package model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import masterpiece.wing.R;

/**
 * Created by User on 11/12/2018.
 */

public class Scooter {
    private int device_code;
    private LatLng latLng;
    private double latitude;
    private double longitude;
    private int battery;
    private int status;
//
//    public Scooter(int device_code, LatLng latLng, int battery, int status) {
//        this.device_code = device_code;
//        this.latLng = latLng;
//        this.battery = battery;
//        this.status = status;
//    }


    public Scooter(int device_code, double latitude, double longitude, int battery, int status) {
        this.device_code = device_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.battery = battery;
        this.status = status;
        
        this.latLng = new LatLng(latitude, longitude);
    }

    public BitmapDescriptor getIcon(){
        switch (status){
//            ready
            case 1:
                return BitmapDescriptorFactory.fromResource(R.drawable.free_scooter_marker);
//            occupied
            case 2:
                return BitmapDescriptorFactory.fromResource(R.drawable.occupied_scooter_marker);
//            low_battery
            case 3:
                return BitmapDescriptorFactory.fromResource(R.drawable.low_battery_scooter_marker);
//            unavailable
            case 4:
                return BitmapDescriptorFactory.fromResource(R.drawable.unavailable_scooter_marker);
//            should not happen
            default:
                return null;
        }
    }



    public int getDevice_code() {
        return device_code;
    }

    public int getBattery() {
        return battery;
    }

    public int getStatus() {
        return status;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDevice_code(int device_code) {
        this.device_code = device_code;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "Scooter{" +
                "device_code=" + device_code +
                ", latLng=" + latLng +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", battery=" + battery +
                ", status=" + status +
                '}';
    }
}
