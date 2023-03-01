package practice.hippo.logic;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ChatLogic {

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
