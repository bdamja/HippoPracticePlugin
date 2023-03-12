package practice.hippo.util;

import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Test {


    public static void main(String[] args) throws FileNotFoundException {
        MongoClient mongo = MongoClients.create("mongodb://localhost:27017");

        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        MongoDatabase database = mongo.getDatabase("myDb");
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("hippos")) {
            database.createCollection("hippos");
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("mapdata")) {
            database.createCollection("mapdata");
        }
        if (!database.listCollectionNames().into(new ArrayList<>()).contains("playerdata")) {
            database.createCollection("playerdata");
        }

        File mapDataDirectory = new File("/home/bdamja/HippoPracticePlugin/plugins/HippoPractice/mapdata");

        for (File mapDataFile : mapDataDirectory.listFiles()) {
            System.out.println(mapDataFile.toPath());
        }

//        database.getCollection("mapdata").deleteOne(Filters.eq("map_name", "aquatica"));
//        Document map = new Document();
//        map.put("map_name", "aquatica");
//        map.put("map_color", "dark_aqua");
//        map.put("biome_red", "plains");
//        map.put("biome_blue", "plains");
//        Document spawnPoint = new Document();
//        spawnPoint.put("x", 29.5);
//        spawnPoint.put("y", 98.0);
//        spawnPoint.put("z", 0.5);
//        Document buildLimits = new Document();
//        buildLimits.put("minX", 1.0);
//        buildLimits.put("minY", 84.0);
//        buildLimits.put("minZ", -20.0);
//        buildLimits.put("maxX", 25.0);
//        buildLimits.put("maxY", 99.0);
//        buildLimits.put("maxZ", 20.0);
//        Document bridgeDimensions = new Document();
//        bridgeDimensions.put("minX", -20.0);
//        bridgeDimensions.put("minY", 84.0);
//        bridgeDimensions.put("minZ", -0.0);
//        bridgeDimensions.put("maxX", 20.0);
//        bridgeDimensions.put("maxY", 92.0);
//        bridgeDimensions.put("maxZ", 0.0);
//        map.put("spawn_point", spawnPoint);
//        map.put("build_limits", buildLimits);
//        map.put("bridge_dimensions", bridgeDimensions);
//        database.getCollection("mapdata").insertOne(map);

//        FindIterable<Document> mapdata =  database.getCollection("mapdata").find();
//        Iterator<Document> iterator = mapdata.iterator();
//
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }

        mongo.close();
    }
}
