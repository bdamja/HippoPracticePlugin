package practice.hippo.logic;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.World;
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

    public void loadAir(Plot plot) {
        pasteSchematic("air_" + plot.getSide(), Offset.worldeditVector(plot, 0, 93, 0, true), false);
    }

    public void loadMainBridge(Plot plot, int length) {
        String schematicName = "bridge_" + plot.getSide() + "_" + length;
        pasteSchematic(schematicName, Offset.worldeditVector(plot, 0, 93, 0, true), false);
    }

    public void loadViewBox(Plot plot) {
        loadAir(plot);
        pasteSchematic("viewbox", Offset.worldeditVector(plot, 2, 93, 0, true), false);
    }

    public void loadMap(Plot plot, String mapName) {
        loadAir(plot);
        String schematicName = mapName + "_" + plot.getSide();
        pasteSchematic(schematicName, Offset.worldeditVector(plot, 0, 93, 0, true), false);
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematic(String schematicName, Vector locationVector, boolean noAir) {
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
        schematicName = schematicName.concat(".schematic");
        File file = new File(parentPlugin.getPluginsDirSubdir("schematics") + File.separator + schematicName);
        if (!file.exists()) {
            parentPlugin.getLogger().severe("Error when trying to paste schematic: Could not find file: " + file);
        } else {
            try {
                CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                clipboard.paste(editSession, locationVector, noAir);
            } catch (DataException | IOException | MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        }
    }
}
