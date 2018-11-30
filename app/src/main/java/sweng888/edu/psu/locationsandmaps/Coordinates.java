package sweng888.edu.psu.locationsandmaps;

public class Coordinates {
    private double latitude;
    private double longitude;
    private String place;

    public Coordinates() {
    }

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(double latitude, double longitude, String place) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
