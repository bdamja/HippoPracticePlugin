package practice.hippo.logic;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ChatLogic {

    private static String PREFIX = "" + ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "HP" + ChatColor.RESET + ChatColor.DARK_GRAY + "] ";

    public static void sendMessageToPlayer(String msg, Player player) {
        player.sendMessage(PREFIX + ChatColor.RESET + msg);
    }

    public static void sendHippoCompletion(MapLogic mapLogic, long ms, Player player) {
        String msg = ChatColor.GRAY + "You completed the " + mapLogic.mapText() + ChatColor.GRAY + " structure in " + ChatColor.AQUA + ms + ChatColor.GRAY + " ms!";
        sendMessageToPlayer(msg, player);
    }
}
