package practice.hippo.playerdata;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayerDataFormat {

    @SerializedName("player_name")
    private String playerName;

    @SerializedName("personal_bests")
    private ArrayList<MapPB> personalBests;

    public PlayerDataFormat(String playerName) {
        this.playerName = playerName;
        this.personalBests = new ArrayList<>();
    }

    public void addPB(MapPB mapPB) {
        personalBests.add(mapPB);
    }

    public ArrayList<MapPB> getPersonalBests() {
        return personalBests;
    }
}
