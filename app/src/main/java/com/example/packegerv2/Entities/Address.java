package com.example.packegerv2.Entities;

public class Address {
    double lat;
    double longt;
    String address;

    public Address() {
    }

    public Address(double lat, double longt, String address) {
        this.lat = lat;
        this.longt = longt;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
