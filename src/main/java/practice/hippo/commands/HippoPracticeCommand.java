package practice.hippo.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import practice.hippo.logic.HippoPractice;
import practice.hippo.logic.MapLogic;
import practice.hippo.util.BoundingBox;

import java.io.IOException;

@CommandAlias("hp|hippo")
public class HippoPracticeCommand extends BaseCommand {

    private final HippoPractice parentPlugin;

    public HippoPracticeCommand(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage("List of Hippo Practice Commands\n- hp\n- hp loadmap <map>");
    }

    @Subcommand("loadmap")
    @Description("Loads a bridge map")
    @Syntax("<map>")
    @CommandCompletion("@mapNames")
    public void onLoadMap(CommandSender sender, String[] args) throws IOException {
        if (args.length > 0) {
            String mapName = args[0];
            if (HippoPractice.maps.contains(mapName)) {
                sender.sendMessage(ChatColor.GREEN + "Loading map " + mapName + "...");
                parentPlugin.changeMap(mapName, sender);
            } else {
                sender.sendMessage(ChatColor.RED + "Map not found. Do /hp maps to see a list of all maps");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /hp loadmap <map>");
        }
    }

    @Subcommand("export")
    @Description("exports the current map setup to a hippo file")
    public void onExport(CommandSender sender) {
        Player player = (Player) sender;
        MapLogic mapLogic = parentPlugin.playerMap.get(player.getUniqueId());
        BoundingBox buildLimits = mapLogic.getBuildLimits();
        for (double x = buildLimits.getMinX(); x <= buildLimits.getMaxX(); x++) {
            for (double y = buildLimits.getMinY(); y <= buildLimits.getMaxY(); y++) {
                for (double z = buildLimits.getMinZ(); z <= buildLimits.getMaxZ(); z++) {
                    if (parentPlugin.world.getBlockAt(new Location(parentPlugin.world, x, y, z)).hasMetadata("placed by player")) {
                        System.out.println("x,y,z");
                    }
                }
            }
        }
    }

}
