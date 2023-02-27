package practice.hippo.logic;

import org.bukkit.Location;
import practice.hippo.util.Side;

import java.util.ArrayList;
import java.util.Collections;

public class Plot {

    private final HippoPractice parentPlugin;
    private final Location location;
    private final Side side;

    public Plot(HippoPractice parentPlugin) {
        ArrayList<Plot> plotList = parentPlugin.getPlotList();
        Collections.shuffle(plotList);
        Plot randomPlot = plotList.get(0);
        this.side = randomPlot.side;
        this.location = randomPlot.location;
        this.parentPlugin = parentPlugin;
    }

    public Plot(HippoPractice parentPlugin, Location location, Side side) {
        this.parentPlugin = parentPlugin;
        this.location = location;
        this.side = side;
    }

    public Location getLocation() {
        return location;
    }

    public Side getSide() {
        return side;
    }

}
