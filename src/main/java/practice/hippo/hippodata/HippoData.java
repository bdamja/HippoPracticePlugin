package practice.hippo.hippodata;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class HippoData {

    @SerializedName("map_name")
    private String mapName;

    @SerializedName("blocks")
    private Queue<Vector> blocks;

    public HippoData() {
        this.blocks = new LinkedList<>();
        this.mapName = "null";
    }

    public String getMapName() {
        return this.mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public Queue<Vector> getBlocks() {
        return blocks;
    }

    public void addBlock(Location location) {
        Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        blocks.offer(vector);
    }

    public void setBlocksViaBlockQueue(Queue<Block> blockQueue) {
        for (Block block : blockQueue) {
            Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
            blocks.add(vector);
        }
    }

    public void setBlocksViaLocationQueue(Queue<Location> locationQueue) {
        for (Location location : locationQueue) {
            Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            blocks.add(vector);
        }
    }
}
