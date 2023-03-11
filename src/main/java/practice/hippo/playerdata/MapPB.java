package practice.hippo.playerdata;

import com.google.gson.annotations.SerializedName;

public class MapPB {

    @SerializedName("map_name")
    private final String mapName;

    @SerializedName("personal_best_ms")
    private long personalBestMs;

    public MapPB(String mapName, long personalBestMs) {
        this.mapName = mapName;
        this.personalBestMs = personalBestMs;
    }

    public String getMapName() {
        return this.mapName;
    }

    public long getPersonalBestMs() {
        return this.personalBestMs;
    }

    public void setPersonalBestMs(long ms) {
        this.personalBestMs = ms;
    }
}
