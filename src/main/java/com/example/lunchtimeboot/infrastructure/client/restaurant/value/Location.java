package com.example.lunchtimeboot.infrastructure.client.restaurant.value;

public class Location {

    private Double latitude;

    private Double longitude;

    public Location(Double latitude, Double longitude)  {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("Location: lat=%s,lng=%s", latitude, longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
