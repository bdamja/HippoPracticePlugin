package practice.hippo.logic;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import practice.hippo.events.PlayerJoinHandler;

public class PluginMain extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        System.out.println("Hippo Practice is loading...");
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new PlayerJoinHandler(), this);
    }
}
