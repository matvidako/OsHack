package hr.ms.oshack.model;

public class Bite {

    public final double latitude;
    public final double longitude;
    public final String userId;

    public Bite(double latitude, double longitude, String userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

}
