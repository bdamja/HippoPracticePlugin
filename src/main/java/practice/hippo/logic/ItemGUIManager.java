package practice.hippo.logic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import practice.hippo.commands.HippoPracticeCommand;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ItemGUIManager {

    public static final Material FILLER = Material.STAINED_GLASS_PANE;
    public static final Material KIT_EDITOR = Material.ANVIL;
    public static final Material SHOW_HIPPO = Material.EYE_OF_ENDER;
    public static final Material SHOW_MISSING = Material.COMPASS;
    public static final Material ENTER_SPEC = Material.FEATHER;

    public static Inventory createSettingsGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, "Hippo Practice");
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(null); items.add(getKitEditorItem()); items.add(null); items.add(getShowHippoItem()); items.add(null); items.add(getShowMissingItem()); items.add(null); items.add(getEnterSpecItem()); items.add(null);
        setFillerRow(items);
        items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null);
        items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null);
        items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null); items.add(null);
        setFillerRow(items);
        inventory.setContents(items.toArray(new ItemStack[0]));
        return inventory;
    }

    public static ItemStack getItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItemStack(Material material, String displayName, short durability) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setDurability(durability);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getFillerItem() {
        return getItemStack(FILLER, "§r", (short) 15);
    }

    public static ItemStack getKitEditorItem() {
        return getItemStack(KIT_EDITOR, "§aKit Editor");
    }

    public static ItemStack getShowHippoItem() {
        return getItemStack(SHOW_HIPPO, "§aShow the Hippo");
    }

    public static ItemStack getShowMissingItem() {
        return getItemStack(SHOW_MISSING, "§aShow Missing Blocks");
    }

    public static ItemStack getEnterSpecItem() {
        return getItemStack(ENTER_SPEC, "§aEnter Spectator Mode");
    }

    public static void setFillerRow(ArrayList<ItemStack> items) {
        items.add(getFillerItem()); items.add(getFillerItem()); items.add(getFillerItem());
        items.add(getFillerItem()); items.add(getFillerItem()); items.add(getFillerItem());
        items.add(getFillerItem()); items.add(getFillerItem()); items.add(getFillerItem());
    }

    public static void performItemClickAction(Player player, ItemStack itemStack) throws FileNotFoundException {
        if (itemStack == null) return;
        Material material = itemStack.getType();
        if (material.equals(KIT_EDITOR)) {
            HippoPracticeCommand.INSTANCE.onKit(player, new String[]{"edit"});
            player.closeInventory();
        } else if (material.equals(SHOW_HIPPO)) {
            HippoPracticeCommand.INSTANCE.onShowHippo(player);
            player.closeInventory();
        } else if (material.equals(SHOW_MISSING)) {
            HippoPracticeCommand.INSTANCE.onShowMissing(player);
            player.closeInventory();
        } else if (material.equals(ENTER_SPEC)) {
            HippoPracticeCommand.INSTANCE.onSpectate(player);
            player.closeInventory();
        }
    }

}
