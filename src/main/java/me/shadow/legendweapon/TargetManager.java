package me.shadow.legendweapon;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public final class TargetManager {
    private TargetManager() {}

    public static LivingEntity getTarget(Player player, double range) {
        RayTraceResult result = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                range,
                0.35,
                entity -> entity instanceof LivingEntity && entity != player
        );
        if (result == null) return null;
        Entity hit = result.getHitEntity();
        return hit instanceof LivingEntity living ? living : null;
    }
}
