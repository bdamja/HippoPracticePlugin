package practice.hippo.logic;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventory {

    public static void setDefaultInventory(Player player) {
        hardInventoryClear(player);
        player.getInventory().setItem(2, getPickaxeItemStack());
        player.getInventory().setItem(3, getBlocksItemStack());
        player.getInventory().setItem(4, getBlocksItemStack());
    }

    private static ItemStack getPickaxeItemStack() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMetaPick = pick.getItemMeta();
        itemMetaPick.spigot().setUnbreakable(true);
        itemMetaPick.addEnchant(Enchantment.DIG_SPEED, 2, false);
        pick.setItemMeta(itemMetaPick);
        return pick;
    }

    private static ItemStack getBlocksItemStack() {
        ItemStack block = new ItemStack(Material.STAINED_CLAY, 64);
        block.setDurability((short) 5);
        return(block);
    }

    private static void hardInventoryClear(Player player){
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
