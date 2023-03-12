package practice.hippo.playerdata;

import com.google.gson.Gson;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.InventoryLogic;
import practice.hippo.logic.Timer;
import practice.hippo.util.MongoDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

public class PlayerData {

    public static final long DEFAULT_PB_IN_MS = -1;

    private final HippoPractice parentPlugin;
    private final String playerName;
    private final String playerUUID;
    private PlayerDataFormat data;

    public PlayerData(HippoPractice parentPlugin, String playerName, String playerUUID) throws FileNotFoundException {
        this.parentPlugin = parentPlugin;
        this.playerName = playerName;
        this.playerUUID = playerUUID;
        this.data = readData();
        HippoPractice.uploadPlayerData(this);
    }

    private PlayerDataFormat readData() throws FileNotFoundException {
        PlayerDataFormat data = null;
        if (HippoPractice.USE_DATABASE) {
            data = MongoDB.getPlayerDataFromDocument(playerUUID);
        } else {
            File playerDataFile = new File(parentPlugin.getPluginsDirSubdir("playerdata") + File.separator + playerUUID + ".json");
            if (playerDataFile.exists()) {
                Gson gson = new Gson();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(playerDataFile));
                data = gson.fromJson(bufferedReader, PlayerDataFormat.class);
            } else {
                parentPlugin.getLogger().severe("Error when trying to load player data: Could not find file: " + playerDataFile);
            }
        }
        if (data == null) {
            data = setDefaults();
        }
        return data;
    }

    private PlayerDataFormat setDefaults() {
        PlayerDataFormat data = new PlayerDataFormat(playerName);
        data.setPlayerUUID(playerUUID);
        data.setPickSlot(InventoryLogic.DEFAULT_PICK_SLOT);
        data.setBlocks1Slot(InventoryLogic.DEFAULT_BLOCKS1_SLOT);
        data.setBlocks2Slot(InventoryLogic.DEFAULT_BLOCKS2_SLOT);
        data.setSnowballSlot(InventoryLogic.DEFAULT_SNOWBALL_SLOT);
        for (Map.Entry<String, String> mapElement : HippoPractice.maps.entrySet()) {
            MapPB mapPB = new MapPB(mapElement.getKey(), DEFAULT_PB_IN_MS);
            data.addPB(mapPB);
        }
        return data;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getPlayerUUID() {
        return this.playerUUID;
    }

    public PlayerDataFormat getData() {
        return this.data;
    }

    public ArrayList<MapPB> getPBs() {
        return data.getPersonalBests();
    }

    public long getPB(String mapName) {
        for (MapPB potentialPB : data.getPersonalBests()) {
            if (potentialPB.getMapName().equals(mapName)) {
                return potentialPB.getPersonalBestMs();
            }
        }
        return DEFAULT_PB_IN_MS;
    }

    public long getTotalTime() {
        long total = 0;
        for (MapPB mapPB : data.getPersonalBests()) {
            long time = mapPB.getPersonalBestMs();
            if (time == -1) {
                time = 0;
            }
            total += time;
        }
        return total;
    }

    public void setPB(String mapName, long ms) {
        boolean found = false;
        for (MapPB potentialPB : data.getPersonalBests()) {
            if (potentialPB.getMapName().equals(mapName)) {
                found = true;
                potentialPB.setPersonalBestMs(Timer.computeFloor50(ms));
            }
        }
        if (found) {
            HippoPractice.uploadPlayerData(this);;
        }
    }

    public int getPickSlot() {
        return data.getPickSlot();
    }

    public void setPickSlot(int slot) {
        data.setPickSlot(slot);
    }

    public int getBlocks1Slot() {
        return data.getBlocks1Slot();
    }

    public void setBlocks1Slot(int slot) {
        data.setBlocks1Slot(slot);
    }

    public int getBlocks2Slot() {
        return data.getBlocks2Slot();
    }

    public void setBlocks2Slot(int slot) {
        data.setBlocks2Slot(slot);
    }

    public int getSnowballSlot() {
        return data.getSnowballSlot();
    }

    public void setSnowballSlot(int slot) {
        data.setSnowballSlot(slot);
    }
}