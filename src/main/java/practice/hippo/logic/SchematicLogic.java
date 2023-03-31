package practice.hippo.logic;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.biome.BaseBiome;
import org.bukkit.World;
import practice.hippo.HippoPractice;
import practice.hippo.util.BoundingBox;
import practice.hippo.util.Offset;

import java.io.File;
import java.io.IOException;

public class SchematicLogic {

    private final HippoPractice parentPlugin;
    private final World world;

    public SchematicLogic(HippoPractice parentPlugin, World world) {
        this.parentPlugin = parentPlugin;
        this.world = world;
    }

    public void loadAir(HippoPlayer hippoPlayer) {
        pasteSchematic("air_" + hippoPlayer.getPlot().getSide(), Offset.worldeditVector(hippoPlayer.getPlot(), 0, 93, 0, true), hippoPlayer, false);
    }

    public void loadMainBridge(HippoPlayer hippoPlayer) {
        String schematicName = "bridge_" + hippoPlayer.getPlot().getSide() + "_" + hippoPlayer.getMapData().getBridgeLength();
        pasteSchematic(schematicName, Offset.worldeditVector(hippoPlayer.getPlot(), 0, 93, 0, true), hippoPlayer, false);
    }

    public void loadViewBox(HippoPlayer hippoPlayer) {
        loadAir(hippoPlayer);
        pasteSchematic("viewbox", Offset.worldeditVector(hippoPlayer.getPlot(), 2, 93, 0, true), hippoPlayer, false);
    }

    public void loadMap(HippoPlayer hippoPlayer) {
        loadAir(hippoPlayer);
        String schematicName = hippoPlayer.getMapName() + "_" + hippoPlayer.getPlot().getSide();
        pasteSchematic(schematicName, Offset.worldeditVector(hippoPlayer.getPlot(), 0, 93, 0, true), hippoPlayer, true);
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematic(String schematicName, Vector locationVector, HippoPlayer hippoPlayer, boolean pasteBiome) {
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
        schematicName = schematicName.concat(".schematic");
        File file = new File(parentPlugin.getPluginsDirSubdir("schematics") + File.separator + schematicName);
        if (!file.exists()) {
            parentPlugin.getLogger().severe("Error when trying to paste schematic: Could not find file: " + file);
        } else {
            try {
                CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                if (pasteBiome) {
                    BoundingBox boundingBox = Offset.boundingBox(hippoPlayer.getPlot(), getDimensions(clipboard), true);
                    for (int x = (int) boundingBox.getMinX(); x <= boundingBox.getMaxX(); x++) {
                        for (int z = (int) boundingBox.getMinZ(); z <= boundingBox.getMaxZ(); z++) {
                            editSession.setBiome(new Vector2D(x, z), new BaseBiome(hippoPlayer.getBiomeId()));
                        }
                    }
                }
                clipboard.paste(editSession, locationVector, false);
            } catch (DataException | IOException | MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private BoundingBox getDimensions(CuboidClipboard clipboard) {
        int minBlockX = 0;
        int minBlockY = 0;
        int minBlockZ = -((HippoPractice.DISTANCE_BETWEEN_PLOTS - 1) / 2) + 1;
        int maxBlockX = clipboard.getWidth();
        int maxBlockY = 0;
        int maxBlockZ = ((HippoPractice.DISTANCE_BETWEEN_PLOTS - 1) / 2) - 1;
        return new BoundingBox(minBlockX, minBlockY, minBlockZ, maxBlockX, maxBlockY, maxBlockZ);
    }
}
