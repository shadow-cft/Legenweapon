package me.shadow.legendweapon;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class WeaponManager {
    private static final NamespacedKey KEY =
            new NamespacedKey(LegendWeapon.getInstance(), "death_note");

    private WeaponManager() {}

    public static ItemStack createDeathNote() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§5§lDeath Note");
        meta.setLore(List.of(
                "§7Legendary Weapon",
                "§dRight Click §f: Dash",
                "§dShift + Right Click §f: Phoenix",
                "§dShift + Left Click §f: Chain Bind",
                "§dUltimate §f: Shift + Left Click"
        ));
        meta.addEnchant(Enchantment.SHARPNESS, 5, true);
        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isDeathNote(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return false;
        Byte value = item.getItemMeta().getPersistentDataContainer()
                .get(KEY, PersistentDataType.BYTE);
        return value != null && value == (byte) 1;
    }
}
