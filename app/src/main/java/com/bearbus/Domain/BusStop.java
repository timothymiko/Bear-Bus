package com.bearbus.Domain;

/**
 * Created by timothymiko on 4/2/14.
 */
public class BusStop {

    public String name;
    public double latitude;
    public double longitude;

    public BusStop(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + " Location: " + latitude + ", " + longitude;
    }
}
