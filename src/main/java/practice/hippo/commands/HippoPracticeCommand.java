package practice.hippo.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import practice.hippo.HippoPractice;
import practice.hippo.hippodata.HippoData;
import practice.hippo.logic.LeaderboardLogic;
import practice.hippo.logic.ChatLogic;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.util.Offset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

@CommandAlias("hp|hippo")
public class HippoPracticeCommand extends BaseCommand {

    public static HippoPracticeCommand INSTANCE;
    private final HippoPractice parentPlugin;

    public HippoPracticeCommand(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
        INSTANCE = this;
    }

    @Default
    @Description("Displays a list of all available commands")
    public void onDefault(CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        ChatLogic.sendHelpMessage(player);    }

    @Subcommand("help")
    @Description("Displays a list of all available commands")
    public void onHelp(CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        ChatLogic.sendHelpMessage(player);
    }

    @Subcommand("maps|map")
    @Description("Displays a list of all the maps")
    public void onMaps(CommandSender sender) {
        TextComponent message = new TextComponent(ChatLogic.PREFIX + "§7List of available maps: ");
        for (Map.Entry<String, String> mapElement : HippoPractice.maps.entrySet()) {
            TextComponent mapMessage = new TextComponent("\n" + ChatColor.GRAY + "  §3» §b" + mapElement.getValue());
            mapMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hp loadmap " + mapElement.getKey()));
            mapMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click to go to this map")}));
            message.addExtra(mapMessage);
        }
        ((Player) sender).spigot().sendMessage(message);
    }

    @Subcommand("info|stat|stats")
    @Description("Displays information regarding a player")
    @Syntax("<player>")
    @CommandCompletion("@players")
    public void onInfo(CommandSender sender, String[] args) throws Exception {
        String playerName;
        if (args.length > 0) {
            playerName = args[0];
        } else {
            playerName = sender.getName();
        }
        ChatLogic.sendMessageToPlayer(parentPlugin.getPlayerInfo(playerName), (Player) sender);
    }

    @Subcommand("loadmap|choosemap|changemap")
    @Description("Loads a bridge map")
    @Syntax("<map>")
    @CommandCompletion("@mapNames")
    public void onLoadMap(CommandSender sender, String[] args) throws IOException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        long timeSinceLastExecute = System.currentTimeMillis() - parentPlugin.lastExecutedHeftyCommand.get(player);
        if (timeSinceLastExecute > parentPlugin.commandCooldownMilliseconds) {
            if (args.length > 0) {
                String mapName = args[0];
                if (HippoPractice.maps.containsKey(mapName)) {
                    String mapNameFormatted = HippoPractice.maps.get(mapName);
                    ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "Loading map " + mapNameFormatted + "§7...", player);
                    parentPlugin.changeMap(mapName, player);
                    parentPlugin.lastExecutedHeftyCommand.put(player, System.currentTimeMillis());
                } else {
                    ChatLogic.sendMessageToPlayer(ChatColor.RED + "Map not found. Do /hp maps to see a list of all maps", player);
                }
            } else {
                ChatLogic.sendMessageToPlayer(ChatColor.RED + "Usage: /hp loadmap <map>", player);
            }
        } else {
            ChatLogic.sendMessageToPlayer(ChatColor.RED + "You must wait before executing this command again!", player);
        }
    }

    @Subcommand("showhippo")
    @Description("Shows the current hippo to build")
    public void onShowHippo(CommandSender sender) throws FileNotFoundException {
        Player player = (Player) sender;
        ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "Showing the current structure to build", player);
        parentPlugin.getHippoPlayer(player).placeHippoBlocks(parentPlugin.world);
    }

    @Subcommand("showmissing")
    @Description("Shows what blocks are missing for the current hippo")
    public void onShowMissing(CommandSender sender) throws FileNotFoundException {
        Player player = (Player) sender;
        ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "Showing what blocks are missing", player);
        parentPlugin.showMissingBlocks(parentPlugin.getHippoPlayer(player));
    }

    @CommandPermission("op")
    @Subcommand("export")
    @Description("exports the current map setup to a hippo file")
    public void onExport(CommandSender sender) throws IOException {
        Player player = (Player) sender;
        HippoPlayer hippoPlayer = parentPlugin.getHippoPlayer(player);
        if (!hippoPlayer.getMapData().getMapName().equals("null")) {
            Queue<Block> allBlocks = parentPlugin.getAllBlocksPlacedByPlayer(player);
            Queue<Location> allBlocksOffset = Offset.blockQueueToOriginal(hippoPlayer.getPlot(), allBlocks);
            HippoData hippoData = new HippoData();
            hippoData.setBlocksViaLocationQueue(allBlocksOffset);
            hippoData.setMapName(hippoPlayer.getMapName());
            HippoPractice.uploadHippoData(hippoData);
            ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "Exported new hippo for " + hippoPlayer.mapText(), (Player) sender);
            hippoPlayer.updateMapValues(hippoPlayer.getMapName());
            player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 0.8f);
        } else {
            ChatLogic.sendMessageToPlayer(ChatColor.RED + "You must be in a map to use this command.", (Player) sender);
        }
    }

    @Subcommand("kit|layout|hotbar")
    @Syntax("<player>")
    @Description("Modify the current kit layout")
    @CommandCompletion("@kitActions")
    public void onKit(CommandSender sender, String[] args) {
        String msg = ChatColor.RED + "Usage: /hp kit <edit or save>";
        if (args.length > 0) {
            if (args[0].equals("edit")) {
                TextComponent message = new TextComponent(ChatLogic.PREFIX + "§7Edit your layout to your liking, then do ");
                TextComponent command = new TextComponent("§b/hp kit save");
                command.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hp kit save"));
                command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click here to save your kit")}));
                message.addExtra(command);
                parentPlugin.beginEditingKit((Player) sender);
                ((Player) sender).spigot().sendMessage(message);
                return;
            } else if (args[0].equals("save")) {
                if (parentPlugin.getHippoPlayer((Player) sender).isEditingKit) {
                    msg = ChatColor.GRAY + "Saving your layout.";
                    parentPlugin.saveKit((Player) sender);
                } else {
                    msg = ChatColor.RED + "You must do " + ChatColor.AQUA + "/hp kit edit " + ChatColor.RED + "before saving your layout.";
                }
            }
        }
        ChatLogic.sendMessageToPlayer(msg, (Player) sender);
    }

    @Subcommand("spec|spectate|watch")
    @Description("Enter spectator mode")
    public void onSpectate(CommandSender sender) {
        Player player = (Player) sender;
        player.setGameMode(GameMode.SPECTATOR);
        TextComponent message = new TextComponent(ChatLogic.PREFIX + "§7You are now spectating. Do ");
        TextComponent command = new TextComponent("§b/hp return");
        command.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hp return"));
        command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click here to return")}));
        message.addExtra(command);
        message.addExtra(new TextComponent(" §7to continue playing"));
        player.spigot().sendMessage(message);
    }

    @Subcommand("reset|return")
    @Description("Return to your plot after spectating")
    public void onReturn(CommandSender sender) throws IOException {
        Player player = (Player) sender;
        parentPlugin.resetPlayerAndSendToSpawn(player);
        parentPlugin.resetMap(player);
    }

    @Subcommand("lb|lbs|leaderboard|leaderboards")
    @Syntax("<map> <page>")
    @CommandCompletion("@mapNames")
    @Description("Display the leaderboard for a certain map")
    public void onLeaderboard(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length > 0) {
            String mapName = args[0];
            if (HippoPractice.maps.containsKey(mapName) || mapName.equals("total") || mapName.equals("overall")) {
                String mapNameFormatted = HippoPractice.maps.get(mapName);
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ignored) { }
                }
                player.spigot().sendMessage(new TextComponent(LeaderboardLogic.getLeaderboardForMap(player.getName(), mapName, mapNameFormatted, page)));
            } else {
                ChatLogic.sendMessageToPlayer(ChatColor.RED + "Map not found. Do /hp maps to see a list of all maps", player);
            }
        } else {
            ChatLogic.sendMessageToPlayer(ChatColor.RED + "Usage: /leaderboard <map> <page>", player);
        }
    }
}
