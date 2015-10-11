package hr.ms.oshack.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class Trap {

    public static final String ACTIVE = "active";
    public static final String INACTIVE = "inactive";

    @SerializedName("id")
    public final String id;
    @SerializedName("lat")
    public final double latitude;
    @SerializedName("long")
    public final double longitude;
    @SerializedName("creatorId")
    public final String creatorId;
    @SerializedName("state")
    public final String state;

    public Trap(String id, double latitude, double longitude, String creatorId, String state) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creatorId = creatorId;
        this.state = state;
    }

    public static Trap fromLocation(Location location) {
        return new Trap("", location.getLatitude(), location.getLongitude(), "android", "active");
    }

    public boolean isActive() {
        return ACTIVE.equals(state);
    }
}
