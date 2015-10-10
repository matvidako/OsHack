package hr.ms.oshack.model;

import com.google.gson.annotations.SerializedName;

public class Cluster {
    @SerializedName("lat")
    public final double latitude;
    @SerializedName("long")
    public final double longitude;
    @SerializedName("radius")
    public final double radius;
    @SerializedName("number")
    public final double number;

    public Cluster(double latitude, double longitude, double radius, double number) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.number = number;
    }
}
