package model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 12/13/2018.
 */

public class Ride {
    private int id;
    private Scooter scooter;
    private int price;
    private String start_time;
    private String end_time;
    private float start_point_latitude;
    private float start_point_longitude;
    private float end_point_latitude;
    private float end_point_longitude;
    private boolean is_finished;

    private LatLng starPoint;
    private LatLng endPoint;

    public Ride(int id, Scooter scooter, String start_time, float start_point_latitude, float start_point_longitude) {
        this.id = id;
        this.scooter = scooter;
        this.start_time = start_time;
        this.start_point_latitude = start_point_latitude;
        this.start_point_longitude = start_point_longitude;

        this.starPoint = new LatLng(start_point_latitude, start_point_longitude);
    }

    public Ride(int id, Scooter scooter, int price, String start_time, String end_time, float start_point_latitude, float start_point_longitude, float end_point_latitude, float end_point_longitude, boolean is_finished, LatLng starPoint, LatLng endPoint) {
        this.id = id;
        this.scooter = scooter;
        this.price = price;
        this.start_time = start_time;
        this.end_time = end_time;
        this.start_point_latitude = start_point_latitude;
        this.start_point_longitude = start_point_longitude;
        this.end_point_latitude = end_point_latitude;
        this.end_point_longitude = end_point_longitude;
        this.is_finished = is_finished;
        this.starPoint = starPoint;
        this.endPoint = endPoint;

        this.starPoint = new LatLng(start_point_latitude, start_point_longitude);
        this.endPoint = new LatLng(end_point_latitude, end_point_longitude);
    }


}
