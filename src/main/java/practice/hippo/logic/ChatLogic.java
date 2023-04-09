package practice.hippo.logic;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        String msg = ChatColor.GRAY + "You completed the §a" + hippoPlayer.mapText() + ChatColor.GRAY + " structure in "
                + ChatColor.AQUA + Timer.computeTimeFormatted(ms) + ChatColor.GRAY + "!";
        sendMessageToPlayer(msg, player);
    }

    public static void sendWelcomeMessage(Player player) {
        TextComponent msg1 = new TextComponent(ChatLogic.PREFIX + "§7Welcome to §2Hippo Practice");
        msg1.addExtra("\n" + ChatLogic.PREFIX + "§7This plugin was created by §abdamja");
        msg1.addExtra("\n" + ChatLogic.PREFIX + "§7The source can be found ");
        TextComponent link = new TextComponent("§3§nhere");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/bdamja/HippoPracticePlugin"));
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click here to view source on github")}));
        msg1.addExtra(link);
        player.spigot().sendMessage(msg1);
        TextComponent msg2 = new TextComponent(ChatLogic.PREFIX + "§7Do ");
        TextComponent command = new TextComponent("§b/hp maps");
        command.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hp maps"));
        command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click here to select a map")}));
        msg2.addExtra(command);
        msg2.addExtra(new TextComponent(" §7and click on a map to play"));
        player.spigot().sendMessage(msg2);
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
        message = message + "\n§2» §a/hp spectate §8- §7enter spectator mode";
        message = message + "\n§2» §a/hp return §8- §7return to your plot";
        message = message + "\n§2§m-----------------------------------------------------";
        player.sendMessage(message);
    }
}
