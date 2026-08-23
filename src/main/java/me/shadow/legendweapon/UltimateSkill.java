package me.shadow.legendweapon;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UltimateSkill implements Listener {
    private final LegendWeapon plugin;

    public UltimateSkill(LegendWeapon plugin) { this.plugin = plugin; }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (event.getAction() != Action.LEFT_CLICK_AIR &&
            event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!SkillManager.hasLegendWeapon(player)) return;

        PlayerData data = SkillManager.getData(player);
        if (!data.isUltimateUnlocked()) {
            player.sendMessage("§cUltimate belum terbuka. Butuh 10 kill.");
            return;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "ultimate")) {
            player.sendMessage("§cUltimate cooldown: " +
                    CooldownManager.remainingSeconds(player.getUniqueId(), "ultimate") + " detik.");
            return;
        }

        event.setCancelled(true);

        double damage = plugin.getConfig().getDouble("damage.ultimate", 25.0);
        for (LivingEntity target : player.getWorld().getNearbyLivingEntities(
                player.getLocation(), 8)) {
            if (target != player && !target.isDead()) target.damage(damage, player);
        }

        player.getWorld().spawnParticle(
                Particle.DRAGON_BREATH, player.getLocation().add(0,1,0),
                120, 3,1.2,3,.15);
        player.getWorld().spawnParticle(
                Particle.END_ROD, player.getLocation().add(0,1,0),
                80, 2.5,1,2.5,.1);
        player.getWorld().playSound(
                player.getLocation(), Sound.ENTITY_WITHER_SPAWN, .8f, 1.5f);

        CooldownManager.setCooldown(player.getUniqueId(), "ultimate",
                plugin.getConfig().getInt("cooldown.ultimate", 60));
    }
}
