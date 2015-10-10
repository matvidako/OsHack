package hr.ms.oshack.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Traps {

    @SerializedName("traps")
    public final List<Trap> traps;

    public Traps(List<Trap> traps) {
        this.traps = traps;
    }
}
