package hr.ms.oshack.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bites {

    @SerializedName("bites")
    public final List<Bite> bites;

    public Bites(List<Bite> bites) {
        this.bites = bites;
    }
}
