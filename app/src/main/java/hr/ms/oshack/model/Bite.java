package hr.ms.oshack.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Bite {

    @SerializedName("lat")
    public final double latitude;
    @SerializedName("long")
    public final double longitude;
    @SerializedName("userId")
    public final String userId;

    public Bite(double latitude, double longitude, String userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    public static Bite fromLocation(Location location) {
        return new Bite(location.getLatitude(), location.getLongitude(), "android");
    }

    public static Bite fromLatLng(LatLng latLng) {
        return new Bite(latLng.latitude, latLng.longitude, "android");
    }

}
