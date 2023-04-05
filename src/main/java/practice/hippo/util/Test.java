package practice.hippo.util;

import com.github.javafaker.Faker;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import practice.hippo.HippoPractice;
import practice.hippo.logic.Timer;
import practice.hippo.playerdata.PlayerData;
import practice.hippo.playerdata.PlayerDataFormat;

import java.io.FileNotFoundException;
import java.util.UUID;

public class Test {

    public static void test() throws FileNotFoundException {
        Faker faker = new Faker();
        for (int i = 0; i < 63; i++) {
            String randomName = faker.name().fullName().replace(" ", "");
            PlayerData playerData = new PlayerData(randomName, UUID.randomUUID().toString());
            playerData.setPB("aquatica", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("boo", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("chronon", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("dojo", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("fortress", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("galaxy", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("hyperfrost", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("mister_cheesy", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("sorcery", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("stumped", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("sunstone", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("treehouse", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("tundra", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            playerData.setPB("urban", Timer.computeFloor50(3000L + (long) (Math.random() * (25000L - 3000L))));
            HippoPractice.uploadPlayerData(playerData);
        }
    }
}
