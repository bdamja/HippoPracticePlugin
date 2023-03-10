package practice.hippo.playerdata;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;
import practice.hippo.logic.HippoPractice;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    public static final long DEFAULT_PB_IN_MS = -1;

    private final HippoPractice parentPlugin;
    private final Player player;
    private final UUID playerUUID;
    private final File playerDataFile;
    private PlayerDataFormat data;

    public PlayerData(HippoPractice parentPlugin, Player player) throws FileNotFoundException {
        this.parentPlugin = parentPlugin;
        this.player = player;
        this.playerUUID = player.getUniqueId();
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
        PlayerDataFormat mapPBs = setDefaultPBs();
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            gson.toJson(mapPBs, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PlayerDataFormat setDefaultPBs() {
        PlayerDataFormat data = new PlayerDataFormat(this.player.getName());
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

    public long getPB(String mapName) {
        for (MapPB potentialPB : data.getPersonalBests()) {
            if (potentialPB.getMapName().equals(mapName)) {
                return potentialPB.getPersonalBestMs();
            }
        }
        return DEFAULT_PB_IN_MS;
    }
}

class PlayerDataFormat {

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

class MapPB {

    @SerializedName("map_name")
    private final String mapName;

    @SerializedName("personal_best_ms")
    private final long personalBestMs;

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
}
