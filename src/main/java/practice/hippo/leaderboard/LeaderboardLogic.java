package practice.hippo.leaderboard;

import practice.hippo.logic.Timer;
import practice.hippo.util.MongoDB;

import java.util.*;

public class LeaderboardLogic {

    private static final int MAX_PLAYERS_PER_LINE = 10;

    public static String getLeaderboardForMap(String mapName, String mapNameFormatted, int page) {
        HashMap<String, Long> sortedLB = sortPlayerTimeMap(MongoDB.getTimeLeaderboardFromMap(mapName));
        int maxPage = 1 + sortedLB.size() / MAX_PLAYERS_PER_LINE;
        if (page > maxPage) {
            page = maxPage;
        } else if (page < 1) {
            page = 1;
        }
        int startingIndex = (page - 1) * MAX_PLAYERS_PER_LINE;
        int endingIndex = startingIndex + MAX_PLAYERS_PER_LINE;
        StringBuilder response = new StringBuilder().append("§8§m-----------------------------------------------------");
        response.append("\n§7Leaderboard for ").append(mapNameFormatted).append("§7 - §7(Page ").append(page).append(")§7:");
        for (int i = startingIndex; i < endingIndex; i++) {
            if (sortedLB.size() > i) {
                String position = String.valueOf(i + 1);
                String name = (String) sortedLB.keySet().toArray()[i];
                String time = Timer.computeTimeFormatted((Long) (sortedLB.values().toArray()[i]));
                if (i == 0) {
                    name = "§l" + name;
                    time = "§f" + time + " §e✴";
                }
                response.append("\n§7")
                        .append(position)
                        .append(". §b")
                        .append(name)
                        .append(" §7- §f")
                        .append(time);
            }
        }
        response.append("§8§m-----------------------------------------------------");
        return response.toString();
    }

    public static HashMap<String, Long> sortPlayerTimeMap(HashMap<String, Long> unsorted) {
        LinkedHashMap<String, Long> sorted = new LinkedHashMap<>();
        ArrayList<Long> list = new ArrayList<>();
        for (Map.Entry<String, Long> entry : unsorted.entrySet()) {
            list.add(entry.getValue());
        }
        list.sort(Long::compareTo);
        for (Long time : list) {
            for (Map.Entry<String, Long> entry : unsorted.entrySet()) {
                if (entry.getValue().equals(time)) {
                    sorted.put(entry.getKey(), time);
                }
            }
        }
        return sorted;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
