package practice.hippo.events.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import practice.hippo.logic.*;
import practice.hippo.playerdata.PlayerData;

import java.io.IOException;

public class PlayerJoinHandler implements Listener {

    private final HippoPractice parentPlugin;

    public PlayerJoinHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        InventoryLogic.hardInventoryClear(player);
        PlayerData playerData = new PlayerData(parentPlugin, player);
        player.sendMessage(String.valueOf(playerData.getPB("aquatica")));
        Plot plot = new Plot(parentPlugin);
        parentPlugin.playerMap.put(player.getUniqueId(), new HippoPlayer(plot, parentPlugin.world, "no_map", player, parentPlugin));
        parentPlugin.getSchematicPaster().loadViewBox(parentPlugin.getHippoPlayer(player));
        parentPlugin.teleportToCenterLocation(player);
        parentPlugin.scoreboardLogic.makeBoard(player);
        ChatLogic.sendWelcomeMessage(player);
    }

}
