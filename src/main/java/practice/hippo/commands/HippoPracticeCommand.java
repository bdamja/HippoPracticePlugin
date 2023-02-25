package practice.hippo.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import practice.hippo.logic.PluginMain;

import java.io.IOException;

@CommandAlias("hp|hippo")
public class HippoPracticeCommand extends BaseCommand {

    private final PluginMain parentPlugin;

    public HippoPracticeCommand(PluginMain parentPlugin) {
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
            if (PluginMain.maps.contains(mapName)) {
                sender.sendMessage(ChatColor.GREEN + "Loading map " + mapName + "...");
                parentPlugin.changeMap(mapName, sender);
            } else {
                sender.sendMessage(ChatColor.RED + "Map not found. Do /hp maps to see a list of all maps");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /hp loadmap <map>");
        }
    }

}
