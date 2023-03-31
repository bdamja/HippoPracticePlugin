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

    public static final String PREFIX = "" + ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "HP" + ChatColor.RESET + ChatColor.DARK_GRAY + "] ";

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
        String msg2 = ChatColor.GRAY + "Do " + ChatColor.AQUA + "/hp maps" + ChatColor.GRAY + " and click on a map to play.";
        sendMessageToPlayer(msg1, player);
        sendMessageToPlayer(msg2, player);
    }

    public static void sendHelpMessage(Player player) {
        String message = "§2§m-----------------------------------------------------";
        message = message + "\n§aList of Hippo Practice commands:";
        message = message + "\n§2» §a/hp help §8- §7list all available commands";
        message = message + "\n§2» §a/hp maps §8- §7list all available maps";
        message = message + "\n§2» §a/hp kit §8- §7Modify your kit layout";
        message = message + "\n§2» §a/hp loadmap <map> §8- §7change the map you are on";
        message = message + "\n§2» §a/hp showhippo §8- §7show how to build the hippo";
        message = message + "\n§2» §a/hp showmissing §8- §7show hippo blocks that are missing";
        message = message + "\n§2» §a/hp stats <player> §8- §7show stats of a particular player";
        message = message + "\n§2§m-----------------------------------------------------";
        player.sendMessage(message);
    }
}
