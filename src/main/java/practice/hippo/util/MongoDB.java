package practice.hippo.util;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import practice.hippo.hippodata.HippoData;
import practice.hippo.mapdata.MapData;

import java.io.File;
import java.util.ArrayList;

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
            System.err.println("Error when trying to load hippo data: Could not find document in db with the map name: " + mapName);
        }
        return hippoData;
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

    public static void upsertPlayerData(String mapName, String json) {
        Document document = Document.parse(json);
        Bson filter = eq("map_name", mapName);
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        playerDataCollection.replaceOne(filter, document, options);
    }

    public static void close() {
        mongo.close();
    }
}
