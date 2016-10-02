package com.acukanov.sibedge.events;


public class CoordinatesFetched {
    private double lat;
    private double lon;

    public CoordinatesFetched(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    // region getters and setters

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
    //endregion
}
