package practice.hippo.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import practice.hippo.HippoPractice;

import java.io.FileNotFoundException;

public class PlayerChangedWorldHandler implements Listener {

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) throws FileNotFoundException {
        if (event.getFrom().getName().equals(HippoPractice.INSTANCE.worldName)) {
            PlayerQuitHandler.handleQuit(event.getPlayer(), HippoPractice.INSTANCE);
        } else {
            PlayerJoinHandler.handleJoin(event.getPlayer(), HippoPractice.INSTANCE);
        }
    }

}
