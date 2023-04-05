package practice.hippo.util;

import com.google.gson.Gson;
import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import practice.hippo.hippodata.HippoData;
import practice.hippo.mapdata.MapData;
import practice.hippo.playerdata.MapPB;
import practice.hippo.playerdata.PlayerData;
import practice.hippo.playerdata.PlayerDataFormat;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB {

    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Document> mapDataCollection;
    private static MongoCollection<Document> playerDataCollection;
    private static MongoCollection<Document> hippoDataCollection;

    public static void init() {
        mongo = MongoClients.create("mongodb://localhost:27017");
        database = mongo.getDatabase("myDb");

        if (!database.listCollectionNames().into(new ArrayList<>()).contains("hippodata")) {
            database.createCollection("hippodata");
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("mapdata")) {
            database.createCollection("mapdata");
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("playerdata")) {
            database.createCollection("playerdata");
        }

        hippoDataCollection = database.getCollection("hippodata");
        mapDataCollection = database.getCollection("mapdata");
        playerDataCollection = database.getCollection("playerdata");
    }

    public static MapData getMapDataFromDocument(String mapName) {
        MapData mapData = null;
        Document document  = database.getCollection("mapdata").find(eq("map_name", mapName)).first();
        if (document != null) {
            Gson gson = new Gson();
            mapData = gson.fromJson(document.toJson(), MapData.class);
        } else {
            System.err.println("Error when trying to load map data: Could not find document in db with the map name: " + mapName);
        }
        return mapData;
    }

    public static HippoData getHippoDataFromDocument(String mapName) {
        HippoData hippoData = null;
        Document document  = database.getCollection("hippodata").find(eq("map_name", mapName)).first();
        if (document != null) {
            Gson gson = new Gson();
            hippoData = gson.fromJson(document.toJson(), HippoData.class);
        } else {
            //System.err.println("Error when trying to load hippo data: Could not find document in db with the map name: " + mapName);
        }
        return hippoData;
    }

    public static PlayerDataFormat getPlayerDataFromDocument(String uuid) {
        PlayerDataFormat data = null;
        Document document  = database.getCollection("playerdata").find(eq("player_uuid", uuid)).first();
        if (document != null) {
            Gson gson = new Gson();
            data = gson.fromJson(document.toJson(), PlayerDataFormat.class);
        } else {
            System.err.println("Error when trying to load player data: Could not find document in db with the player uuid: " + uuid);
        }
        return data;
    }

    public static void upsertHippoData(String mapName, String json) {
        Document document = Document.parse(json);
        Bson filter = eq("map_name", mapName);
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        hippoDataCollection.replaceOne(filter, document, options);
    }

    public static void upsertMapData(String mapName, String json) {
        Document document = Document.parse(json);
        Bson filter = eq("map_name", mapName);
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        mapDataCollection.replaceOne(filter, document, options);
    }

    public static void upsertPlayerData(String uuid, String json) {
        Document document = Document.parse(json);
        Bson filter = eq("player_uuid", uuid);
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        playerDataCollection.replaceOne(filter, document, options);
    }

    public static void close() {
        mongo.close();
    }

    public static HashMap<String, Long> getTimeLeaderboardFromMap(String mapName) {
        HashMap<String, Long> result = new HashMap<>();
        FindIterable<Document> collection = database.getCollection("playerdata").find();
        Gson gson = new Gson();
        for (Document document : collection) {
            PlayerDataFormat data = gson.fromJson(document.toJson(), PlayerDataFormat.class);
            MapPB pb = data.getMapPB(mapName);
            if (pb != null && pb.getPersonalBestMs() != -1) {
                result.put(data.getPlayerName(), pb.getPersonalBestMs());
            }
        }
        return result;
    }
}
