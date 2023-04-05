package practice.hippo.leaderboard;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import practice.hippo.logic.Timer;
import practice.hippo.util.MongoDB;

import java.util.*;

public class LeaderboardLogic {

    private static final int MAX_PLAYERS_PER_LINE = 10;

    public static TextComponent getLeaderboardForMap(String playerName, String mapName, String mapNameFormatted, int page) {
        HashMap<String, Long> sortedLB = sortPlayerTimeMap(MongoDB.getTimeLeaderboardFromMap(mapName));
        int maxPage = 1 + sortedLB.size() / MAX_PLAYERS_PER_LINE;
        if (page > maxPage) {
            page = maxPage;
        } else if (page < 1) {
            page = 1;
        }
        int startingIndex = (page - 1) * MAX_PLAYERS_PER_LINE;
        int endingIndex = startingIndex + MAX_PLAYERS_PER_LINE;
        TextComponent response = new TextComponent("§8§m-----------------------------------------------------");
        TextComponent previous = new TextComponent(" §7<< ");
        TextComponent next = new TextComponent(" §7>> ");
        previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hp lb " + mapName + " " + (page - 1)));
        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hp lb " + mapName + " " + (page + 1)));
        previous.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§bPrevious")}));
        next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§aNext")}));
        if (page == 1) {
            previous = new TextComponent(" §8<< ");
        }
        if (page == maxPage) {
            next = new TextComponent(" §8>> ");
        }
        response.addExtra("\n§7Leaderboard for " + mapNameFormatted);
        response.addExtra(previous);
        response.addExtra("§7(Page " + page + ")");
        response.addExtra(next);
        for (int i = startingIndex; i < endingIndex; i++) {
            if (sortedLB.size() > i) {
                String position = String.valueOf(i + 1);
                String name = (String) sortedLB.keySet().toArray()[i];
                String time = Timer.computeTimeFormatted((Long) (sortedLB.values().toArray()[i]));
                if (i == 0) {
                    name = "§b" + name;
                    time = "§f" + time + " §e✴";
                }
                if ((sortedLB.keySet().toArray()[i]).equals(playerName)) {
                    name = "§a" + sortedLB.keySet().toArray()[i];
                }
                response.addExtra("\n§7" + position + ". §3" + name + " §7- §f" + time);
            } else {
                response.addExtra("\n");
            }
        }
        response.addExtra("§8§m-----------------------------------------------------");
        return response;
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
