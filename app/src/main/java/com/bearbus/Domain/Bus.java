package com.bearbus.Domain;

/**
 * Created by timothymiko on 3/20/14.
 */
public class Bus {

    public String id;
    public String name;
    public double latitude;
    public double longitude;
    public String currentStop;
    public String nextStop;

    public Bus() {

    }

    public Bus(String id, String name, double latitude, double longitude, String currentStop, String nextStop) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentStop = currentStop;
        this.nextStop = nextStop;
    }

    @Override
    public String toString() {
        return "ID: " + id + " Name: " + name + " Latitude: " + latitude + " Longitude: " + longitude;
    }
}
