package nure.ua.mediaclient.model.domain;

public class Point {

    private float latitude;
    private float longitude;

    public Point() {
        this.latitude = 0.0f;
        this.longitude = 0.0f;
    }

    public Point(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}

