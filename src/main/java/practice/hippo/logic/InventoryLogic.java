package practice.hippo.logic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import practice.hippo.util.Side;

public class InventoryLogic {

    public static void setDefaultInventory(Player player, Side side) {
        hardInventoryClear(player);
        player.getInventory().setItem(2, getPickaxeItemStack());
        player.getInventory().setItem(3, getBlocksItemStack(side));
        player.getInventory().setItem(4, getBlocksItemStack(side));
        player.getInventory().setItem(5, getSnowballItemStack());
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

    private static ItemStack getSnowballItemStack() {
        ItemStack snowball = new ItemStack(Material.SNOW_BALL, 1);
        ItemMeta itemMetaSnowball = snowball.getItemMeta();
        itemMetaSnowball.setDisplayName("" + ChatColor.RED + ChatColor.BOLD + "Reset Map");
        return(snowball);
    }

    public static void hardInventoryClear(Player player){
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

}
