package me.shadow.legendweapon;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BasicAttack implements Listener {
    private final LegendWeapon plugin;

    public BasicAttack(LegendWeapon plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        if (!SkillManager.hasLegendWeapon(player)) return;

        PlayerData data = SkillManager.getData(player);
        double damage = plugin.getConfig().getDouble("damage.basic", 7.0)
                + data.getBonusDamage();

        event.setCancelled(true);
        target.damage(damage, player);

        target.getWorld().spawnParticle(
                Particle.SWEEP_ATTACK, target.getLocation().add(0, 1, 0), 1);
        target.getWorld().playSound(
                target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }
                       }
            
