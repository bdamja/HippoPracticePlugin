package practice.hippo.events.misc;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import practice.hippo.logic.HippoPractice;

import java.io.IOException;

public class ProjectileLaunchHandler implements Listener {

    private final HippoPractice parentPlugin;

    public ProjectileLaunchHandler(HippoPractice parentPlugin) {
        this.parentPlugin = parentPlugin;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity projectile = event.getEntity();
        if (projectile instanceof Snowball) {
            Player shooter = (Player) ((Snowball) projectile).getShooter();
            if (parentPlugin.getHippoPlayer(shooter).isEditingKit) {
                event.setCancelled(true);
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        killProjectiles();
                        try {
                            parentPlugin.resetPlayerAndSendToSpawn(shooter);
                            parentPlugin.resetMap(shooter);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.runTaskLater(parentPlugin, 1);
            }
        }
    }

    private void killProjectiles() {
        for (Entity entity : parentPlugin.world.getEntities()) {
            if (entity instanceof Projectile) {
                entity.remove();
            }
        }
    }

}
