package me.shadow.legendweapon;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PhoenixSkill implements Listener {
    private final LegendWeapon plugin;

    public PhoenixSkill(LegendWeapon plugin) { this.plugin = plugin; }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!SkillManager.hasLegendWeapon(player)) return;

        PlayerData data = SkillManager.getData(player);
        if (!data.isPhoenixUnlocked()) {
            player.sendMessage("§cPhoenix belum terbuka. Butuh 5 kill.");
            return;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "phoenix")) {
            player.sendMessage("§cPhoenix cooldown: " +
                    CooldownManager.remainingSeconds(player.getUniqueId(), "phoenix") + " detik.");
            return;
        }

        LivingEntity target = TargetManager.getTarget(player, 10);
        if (target == null) {
            player.sendMessage("§7Tidak ada target dalam jarak 10 blok.");
            return;
        }

        event.setCancelled(true);
        target.damage(plugin.getConfig().getDouble("damage.phoenix", 37.0), player);
        target.getWorld().spawnParticle(
                Particle.FLAME, target.getLocation().add(0,1,0), 60, .5,.7,.5,.08);
        target.getWorld().spawnParticle(
                Particle.SOUL_FIRE_FLAME, target.getLocation().add(0,1,0), 25, .4,.5,.4,.05);
        target.getWorld().playSound(
                target.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.2f, .8f);

        CooldownManager.setCooldown(player.getUniqueId(), "phoenix",
                plugin.getConfig().getInt("cooldown.phoenix", 32));
    }
}
