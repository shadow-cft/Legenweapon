package me.shadow.legendweapon;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class DashSkill implements Listener {
    private final LegendWeapon plugin;

    public DashSkill(LegendWeapon plugin) { this.plugin = plugin; }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR || player.isSneaking()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!SkillManager.hasLegendWeapon(player)) return;

        PlayerData data = SkillManager.getData(player);
        if (!data.isDashUnlocked()) {
            player.sendMessage("§cDash belum terbuka. Butuh 3 kill.");
            return;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "dash")) {
            player.sendMessage("§cDash cooldown: " +
                    CooldownManager.remainingSeconds(player.getUniqueId(), "dash") + " detik.");
            return;
        }

        event.setCancelled(true);
        Vector direction = player.getLocation().getDirection().normalize();
        Location location = player.getLocation().clone();

        for (int i = 0; i < 10; i++) {
            Location next = location.clone().add(direction);
            if (next.getBlock().getType().isSolid() ||
                next.clone().add(0, 1, 0).getBlock().getType().isSolid()) break;
            location = next;
        }

        player.teleport(location);
        player.getWorld().spawnParticle(Particle.CLOUD, location, 30, .35, .35, .35, .05);
        player.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f);
        CooldownManager.setCooldown(player.getUniqueId(), "dash",
                plugin.getConfig().getInt("cooldown.dash", 15));
    }
}
