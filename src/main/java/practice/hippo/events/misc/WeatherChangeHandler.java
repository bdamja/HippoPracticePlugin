package practice.hippo.events.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import practice.hippo.HippoPractice;

public class WeatherChangeHandler implements Listener {

    public WeatherChangeHandler() { }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!event.getWorld().getName().equals(HippoPractice.INSTANCE.worldName)) return;
        event.setCancelled(true);
    }

}