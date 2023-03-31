package practice.hippo.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import practice.hippo.hippodata.HippoData;
import practice.hippo.logic.ChatLogic;
import practice.hippo.logic.HippoPlayer;
import practice.hippo.HippoPractice;
import practice.hippo.util.Offset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

@CommandAlias("hp|hippo")
public class HippoPracticeCommand extends BaseCommand {

    private final HippoPractice parentPlugin;

    public HippoPracticeCommand(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @Default
    public void onDefault(CommandSender sender) {
        ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "List of Hippo Practice Commands\n- hp\n- hp loadmap <map>\n- hp maps\n- hp showhippo", (Player) sender);
    }

    @Subcommand("maps")
    @Description("Displays a list of all the maps")
    public void onMaps(CommandSender sender) {
        String msg = ChatColor.GRAY + "List of available maps: ";
        for (Map.Entry<String, String> mapElement : HippoPractice.maps.entrySet()) {
            msg = msg.concat("\n" + ChatColor.GRAY + "      - " + mapElement.getValue());
        }
        ChatLogic.sendMessageToPlayer(msg, (Player) sender);
    }

    @Subcommand("info")
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

    @Subcommand("loadmap")
    @Description("Loads a bridge map")
    @Syntax("<map>")
    @CommandCompletion("@mapNames")
    public void onLoadMap(CommandSender sender, String[] args) throws IOException {
        if (args.length > 0) {
            String mapName = args[0];
            if (HippoPractice.maps.containsKey(mapName)) {
                ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "Loading map " + mapName + "...", (Player) sender);
                parentPlugin.changeMap(mapName, sender);
            } else {
                ChatLogic.sendMessageToPlayer(ChatColor.RED + "Map not found. Do /hp maps to see a list of all maps", (Player) sender);
            }
        } else {
            ChatLogic.sendMessageToPlayer(ChatColor.RED + "Usage: /hp loadmap <map>", (Player) sender);
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

    @Subcommand("kit")
    @Syntax("<player>")
    @Description("Modify the current kit layout")
    @CommandCompletion("@kitActions")
    public void onKit(CommandSender sender, String[] args) {
        String msg = ChatColor.RED + "Usage: /hp kit <edit or save>";
        if (args.length > 0) {
            if (args[0].equals("edit")) {
                msg = ChatColor.GRAY + "Edit your layout to your liking, then do " + ChatColor.AQUA + "/hp kit save";
                parentPlugin.beginEditingKit((Player) sender);
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

}
