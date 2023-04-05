package practice.hippo.playerdata;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayerDataFormat {

    @SerializedName("player_name")
    private String playerName;

    @SerializedName("player_uuid")
    private String playerUUID;

    @SerializedName("personal_bests")
    private ArrayList<MapPB> personalBests;

    @SerializedName("pick_slot")
    private int pickSlot;

    @SerializedName("blocks1_slot")
    private int blocks1Slot;

    @SerializedName("blocks2_slot")
    private int blocks2Slot;

    @SerializedName("reset_item_slot")
    private int resetItemSlot;

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

    public int getPickSlot() {
        return this.pickSlot;
    }

    public void setPickSlot(int slot) {
        this.pickSlot = slot;
    }

    public int getBlocks1Slot() {
        return this.blocks1Slot;
    }

    public void setBlocks1Slot(int slot) {
        this.blocks1Slot = slot;
    }

    public int getBlocks2Slot() {
        return this.blocks2Slot;
    }

    public void setBlocks2Slot(int slot) {
        this.blocks2Slot = slot;
    }

    public int getResetItemSlot() {
        return this.resetItemSlot;
    }

    public void setResetItemSlot(int slot) {
        this.resetItemSlot = slot;
    }

    public String getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public MapPB getMapPB(String mapName) {
        for (MapPB potentialPB : personalBests) {
            if (potentialPB.getMapName().equals(mapName)) {
                return potentialPB;
            }
        }
        return null;
    }
}
