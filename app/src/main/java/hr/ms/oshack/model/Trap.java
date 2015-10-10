package hr.ms.oshack.model;

import com.google.gson.annotations.SerializedName;

public class Trap {

    @SerializedName("lat")
    public final double latitude;
    @SerializedName("long")
    public final double longitude;
    @SerializedName("creatorId")
    public final String creatorId;

    public Trap(double latitude, double longitude, String creatorId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.creatorId = creatorId;
    }
}
