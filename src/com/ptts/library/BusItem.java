package com.ptts.library;

public class BusItem {
    private String busId;
    private String latitude;
    private String longitude;
    
    public BusItem(String busId, String latitude, String longitude) {
        this.busId = busId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String toString() {
        return busId+" "+latitude;
    }
}
