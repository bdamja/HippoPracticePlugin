package practice.hippo.logic;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ChatLogic {

    public static ChatColor strToChatColor(String color) {
        ChatColor chatColor = ChatColor.WHITE;
        switch (color) {
            case "black":
                chatColor = ChatColor.BLACK;
                break;
            case "dark_blue":
                chatColor = ChatColor.DARK_BLUE;
                break;
            case "dark_green":
                chatColor = ChatColor.DARK_GREEN;
                break;
            case "dark_aqua":
                chatColor = ChatColor.DARK_AQUA;
                break;
            case "dark_red":
                chatColor = ChatColor.DARK_RED;
                break;
            case "dark_purple":
                chatColor = ChatColor.DARK_PURPLE;
                break;
            case "gold":
                chatColor = ChatColor.GOLD;
                break;
            case "gray":
                chatColor = ChatColor.GRAY;
                break;
            case "dark_gray":
                chatColor = ChatColor.DARK_GRAY;
                break;
            case "blue":
                chatColor = ChatColor.BLUE;
                break;
            case "green":
                chatColor = ChatColor.GREEN;
                break;
            case "aqua":
                chatColor = ChatColor.AQUA;
                break;
            case "red":
                chatColor = ChatColor.RED;
                break;
            case "light_purple":
                chatColor = ChatColor.LIGHT_PURPLE;
                break;
            case "yellow":
                chatColor = ChatColor.YELLOW;
                break;
        }
        return chatColor;
    }

    private static final String PREFIX = "" + ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "HP" + ChatColor.RESET + ChatColor.DARK_GRAY + "] ";

    public static void sendMessageToPlayer(String msg, Player player) {
        player.sendMessage(PREFIX + ChatColor.RESET + msg);
    }

    public static void sendHippoCompletion(HippoPlayer hippoPlayer, long ms, Player player) {
        String msg = ChatColor.GRAY + "You completed the " + hippoPlayer.mapText() + ChatColor.GRAY + " structure in "
                + ChatColor.AQUA + Timer.computeTimeFormatted(ms) + ChatColor.GRAY + "!";
        sendMessageToPlayer(msg, player);
    }

    public static void sendWelcomeMessage(Player player) {
        String msg1 = ChatColor.GRAY + "Welcome to " + ChatColor.DARK_GREEN + "Hippo Practice";
        String msg2 = ChatColor.GRAY + "Do " + ChatColor.AQUA + "/hp loadmap <map>" + ChatColor.GRAY + " to select a map.";
        sendMessageToPlayer(msg1, player);
        sendMessageToPlayer(msg2, player);
    }
}
