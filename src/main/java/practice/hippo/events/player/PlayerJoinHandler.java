package practice.hippo.events.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import practice.hippo.HippoPractice;
import practice.hippo.logic.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayerJoinHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerJoinHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        if (!event.getPlayer().getWorld().getName().equals(HippoPractice.INSTANCE.worldName)) return;
        event.setJoinMessage("§b" + event.getPlayer().getName() + " §7has joined.");
        handleJoin(event.getPlayer(), parentPlugin);
    }

    public static void handleJoin(Player player, HippoPractice parentPlugin) throws FileNotFoundException {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.ADVENTURE);
        InventoryLogic.hardInventoryClear(player);
        Plot plot = new Plot(parentPlugin);
        parentPlugin.playerMap.put(player.getUniqueId(), new HippoPlayer(plot, parentPlugin.world, "no_map", player, parentPlugin));
        parentPlugin.lastExecutedHeftyCommand.put(player, (System.currentTimeMillis() - 3000));
        parentPlugin.getSchematicPaster().loadViewBox(parentPlugin.getHippoPlayer(player));
        parentPlugin.teleportToCenterLocation(player);
        parentPlugin.scoreboardLogic.makeBoard(player);
        ChatLogic.sendWelcomeMessage(player);
        InventoryLogic.giveSettingsItem(player, parentPlugin.getHippoPlayer(player).getPlayerData());
    }

}
