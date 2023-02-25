package practice.hippo.logic;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.*;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class SchematicPaster {

    private final PluginMain parentPlugin;
    private final World world;

    public SchematicPaster(PluginMain parentPlugin, World world) {
        this.parentPlugin = parentPlugin;
        this.world = world;
    }

    public void loadMainBridge() {
        pasteSchematic("mainbridge", new Vector(0, 93, 0), false);
    }

    public void loadMap(String mapName) {
        pasteSchematic("air", new Vector(0, 93, 0), false);
        pasteSchematic(mapName, new Vector(0, 93, 0), false);
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematic(String schematicName, Vector locationVector, boolean noAir) {
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
        File file = new File(parentPlugin.getDataFolder() + File.separator + "schematics" + File.separator + schematicName + ".schematic");
        if (!file.exists()) {
            parentPlugin.getLogger().severe("Error when trying to paste schematic: Could not find file: " + file);
            return;
        }
        try {
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);
            clipboard.paste(editSession, locationVector, noAir);
        } catch (DataException | IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

}
