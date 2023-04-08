package practice.hippo.logic;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import practice.hippo.HippoPractice;
import practice.hippo.playerdata.PlayerData;
import practice.hippo.util.Side;

public class InventoryLogic {

    public static final int DEFAULT_PICK_SLOT = 2;
    public static final int DEFAULT_BLOCKS1_SLOT = 3;
    public static final int DEFAULT_BLOCKS2_SLOT = 4;
    public static final int DEFAULT_RESET_ITEM_SLOT = 5;
    public static final int DEFAULT_SETTINGS_ITEM_SLOT = 8;

    public static void loadInventory(Player player, Side side, PlayerData playerData) {
        hardInventoryClear(player);
        player.getInventory().setItem(playerData.getPickSlot(), getPickaxeItemStack());
        player.getInventory().setItem(playerData.getBlocks1Slot(), getBlocksItemStack(side));
        player.getInventory().setItem(playerData.getBlocks2Slot(), getBlocksItemStack(side));
        player.getInventory().setItem(playerData.getResetItemSlot(), getResetItemItemStack());
        player.getInventory().setItem(playerData.getSettingsItemSlot(), getSettingsItemStack());
    }

    private static ItemStack getPickaxeItemStack() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMetaPick = pick.getItemMeta();
        itemMetaPick.spigot().setUnbreakable(true);
        itemMetaPick.addEnchant(Enchantment.DIG_SPEED, 2, false);
        pick.setItemMeta(itemMetaPick);
        return pick;
    }

    private static ItemStack getBlocksItemStack(Side side) {
        ItemStack block = new ItemStack(Material.STAINED_CLAY, 64);
        if (side == Side.red) {
            block.setDurability((short) 14);
        } else if (side == Side.blue) {
            block.setDurability((short) 11);
        }
        return(block);
    }

    private static ItemStack getResetItemItemStack() {
        ItemStack itemStack = new ItemStack(HippoPractice.RESET_MATERIAL, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lReset Map §7(Right Click)");
        itemStack.setItemMeta(itemMeta);
        return(itemStack);
    }

    private static ItemStack getSettingsItemStack() {
        ItemStack itemStack = new ItemStack(HippoPractice.SETTINGS_MATERIAL, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§aSettings §7(Right Click)");
        itemStack.setItemMeta(itemMeta);
        return(itemStack);
    }

    public static void hardInventoryClear(Player player) {
        player.getInventory().clear();
        ItemStack clear = new ItemStack(Material.AIR);
        player.getOpenInventory().setCursor(clear);
        player.getOpenInventory().getTopInventory().clear();
        player.getOpenInventory().getBottomInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void giveSettingsItem(Player player, PlayerData playerData) {
        player.getInventory().setItem(playerData.getSettingsItemSlot(), getSettingsItemStack());
    }

}
