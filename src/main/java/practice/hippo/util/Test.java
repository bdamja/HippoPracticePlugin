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
        int DISTANCE_BETWEEN_PLOTS = 41;
        int maxPlayers = 20;
        int z = 0;
        for (int i = 0; i < maxPlayers/4; i++) {
            System.out.println("0 0 " + z + " red");
            System.out.println("0 0 " + z + " blue");
            z += DISTANCE_BETWEEN_PLOTS;
        }
    }
}
