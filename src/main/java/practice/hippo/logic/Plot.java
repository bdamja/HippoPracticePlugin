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
        this.parentPlugin = parentPlugin;
        ArrayList<Plot> plotList = parentPlugin.getPlotList();
        Plot randomPlot = selectAvailablePlot(plotList);
        this.side = randomPlot.side;
        this.location = randomPlot.location;
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

    private Plot selectAvailablePlot(ArrayList<Plot> plotList) {
        boolean found = false;
        int i = 0;
        Plot plot = plotList.get(0);
        while (!found && i < plotList.size()) {
            plot = plotList.get(i);
            if (!parentPlugin.isPlotOccupied(plot)) {
                found = true;
            }
            i++;
        }
        if (!found) {
            parentPlugin.getLogger().severe("No plots were available, so multiple players may have the same plot");
        }
        return plot;
    }

}
