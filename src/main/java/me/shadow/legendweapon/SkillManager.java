package me.shadow.legendweapon;

import org.bukkit.entity.Player;

public final class SkillManager {
    private SkillManager() {}

    public static boolean hasLegendWeapon(Player player) {
        return WeaponManager.isDeathNote(player.getInventory().getItemInMainHand());
    }

    public static PlayerData getData(Player player) {
        return PlayerDataManager.getData(player);
    }
}
