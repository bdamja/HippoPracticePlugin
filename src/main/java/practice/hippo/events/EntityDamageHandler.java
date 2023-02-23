package practice.hippo.events;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityDamageHandler implements Listener {

    public EntityDamageHandler() { }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

}