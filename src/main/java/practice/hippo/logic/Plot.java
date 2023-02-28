package practice.hippo.logic;

import org.bukkit.Location;
import practice.hippo.util.Side;

import java.util.ArrayList;
import java.util.Collections;

public class Plot {

    private final HippoPractice parentPlugin;
    private final Location location;
    private final Side side;
    public final double x;
    public final double y;
    public final double z;

    public Plot(HippoPractice parentPlugin) {
        ArrayList<Plot> plotList = parentPlugin.getPlotList();
        Collections.shuffle(plotList);
        Plot randomPlot = plotList.get(0);
        this.side = randomPlot.side;
        this.location = randomPlot.location;
        this.parentPlugin = parentPlugin;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Plot(HippoPractice parentPlugin, Location location, Side side) {
        this.parentPlugin = parentPlugin;
        this.location = location;
        this.side = side;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Location getLocation() {
        return location;
    }

    public Side getSide() {
        return side;
    }

}
