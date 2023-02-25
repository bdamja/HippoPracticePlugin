package practice.hippo.events.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import practice.hippo.logic.PluginMain;

import java.io.IOException;

public class ProjectileLaunchHandler implements Listener {

    private final PluginMain parentPlugin;

    public ProjectileLaunchHandler(PluginMain parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) throws IOException {
        Entity projectile = event.getEntity();
        if (projectile instanceof Snowball) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player shooter = (Player) ((Snowball) projectile).getShooter();
                    killProjectiles();
                    try {
                        parentPlugin.resetPlayerAndSendToSpawn(shooter);
                        parentPlugin.resetMap();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.runTaskLater(parentPlugin, 1);
        }
    }

    private void killProjectiles() {
        for (Entity entity : parentPlugin.getWorld().getEntities()) {
            if (entity instanceof Projectile) {
                entity.remove();
            }
        }
    }

}
