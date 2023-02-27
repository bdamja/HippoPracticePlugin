package practice.hippo.util;

import org.bukkit.Location;
import practice.hippo.logic.Plot;

public class Offset {

    public static void offset(Plot plot, Location location) {
        double offsetX = location.getX();
        double offsetY = location.getY();
        double offsetZ = location.getZ();
        location.setX(offsetX += plot.getLocation().getX());
        location.setX(offsetY += plot.getLocation().getY());
        location.setX(offsetZ += plot.getLocation().getZ());
    }
}
