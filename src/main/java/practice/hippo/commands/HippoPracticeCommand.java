package practice.hippo.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import practice.hippo.logic.ChatLogic;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.MapLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Queue;

@CommandAlias("hp|hippo")
public class HippoPracticeCommand extends BaseCommand {

    private final HippoPractice parentPlugin;

    public HippoPracticeCommand(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @Default
    public void onDefault(CommandSender sender) {
        ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "List of Hippo Practice Commands\n- hp\n- hp loadmap <map>", (Player) sender);
    }

    @Subcommand("loadmap")
    @Description("Loads a bridge map")
    @Syntax("<map>")
    @CommandCompletion("@mapNames")
    public void onLoadMap(CommandSender sender, String[] args) throws IOException {
        if (args.length > 0) {
            String mapName = args[0];
            if (HippoPractice.maps.contains(mapName)) {
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
        parentPlugin.playerMap.get(player.getUniqueId()).placeHippoBlocks(parentPlugin.world);
    }

    @Subcommand("export")
    @Description("exports the current map setup to a hippo file")
    public void onExport(CommandSender sender) throws FileNotFoundException {
        Player player = (Player) sender;
        MapLogic mapLogic = parentPlugin.playerMap.get(player.getUniqueId());
        Queue<Block> allBlocks = parentPlugin.getAllBlocksPlacedByPlayer(player);
        File file = new File("./plugins/HippoPractice/hippos/" + mapLogic.getMapName() + ".txt");
        PrintWriter output = new PrintWriter(file);
        for (Block block : allBlocks) {
            output.write(block.getX() + " " + block.getY() + " " + block.getZ() + "\n");
        }
        ChatLogic.sendMessageToPlayer(ChatColor.GRAY + "Exported new hippo for " + mapLogic.mapText(), (Player) sender);
        output.close();
        mapLogic.updateMapValues(mapLogic.getMapName());
        player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 0.8f);
    }

}
