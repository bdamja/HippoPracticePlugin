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

    public void loadMainBridge(Plot plot) {
        String schematicName = "bridge" + "_" + plot.getSide();
        pasteSchematic(schematicName, Offset.worldeditVector(plot, 0, 93, 0, true), false);
    }

    public void loadViewBox(Plot plot) {
        pasteSchematic("air", Offset.worldeditVector(plot, 2, 93, 0, true), false);
        pasteSchematic("viewbox", Offset.worldeditVector(plot, 2, 93, 0, true), false);
    }

    public void loadMap(Plot plot, String mapName) {
        String schematicName = mapName + "_" + plot.getSide();
        pasteSchematic("air", Offset.worldeditVector(plot, 0, 93, 0, true), false);
        pasteSchematic(schematicName, Offset.worldeditVector(plot, 0, 93, 0, true), false);
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematic(String schematicName, Vector locationVector, boolean noAir) {
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
        File file = new File(parentPlugin.getDataFolder() + File.separator + "schematics" + File.separator + schematicName + ".schematic");
        if (!file.exists()) {
            parentPlugin.getLogger().severe("Error when trying to paste schematic: Could not find file: " + file);
        } else {
            try {
                CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
                clipboard.paste(editSession, locationVector, noAir);
                System.out.println("pasted at "+ locationVector);
            } catch (DataException | IOException | MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        }
    }
}
