package practice.hippo.playerdata;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.InventoryLogic;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class PlayerData {

    public static final long DEFAULT_PB_IN_MS = -1;

    private final HippoPractice parentPlugin;
    private final String playerName;
    private final String playerUUID;
    private final File playerDataFile;
    private PlayerDataFormat data;

    public PlayerData(HippoPractice parentPlugin, String playerName, String playerUUID) throws FileNotFoundException {
        this.parentPlugin = parentPlugin;
        this.playerName = playerName;
        this.playerUUID = playerUUID;
        this.playerDataFile = new File(parentPlugin.getPluginsDirSubdir("playerdata") + File.separator + playerUUID + ".json");
        createIfNonExistent(playerDataFile);
        readCurrentPBs(playerDataFile);
    }

    private void createIfNonExistent(File file) {
        try {
            if (file.createNewFile()){
                writeDefaultFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultFile(File file) {
        PlayerDataFormat playerData = setDefaults();
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            gson.toJson(playerData, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeNewPlayerData() {
        try (Writer writer = new FileWriter(playerDataFile)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PlayerDataFormat setDefaults() {
        PlayerDataFormat data = new PlayerDataFormat(playerName);
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

    private void readCurrentPBs(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        this.data = gson.fromJson(bufferedReader, PlayerDataFormat.class);
    }

    public String getPlayerName() {
        return this.playerName;
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
                potentialPB.setPersonalBestMs(ms);
            }
        }
        if (found) {
            writeNewPlayerData();
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

    public void save() {
        writeNewPlayerData();
    }
}