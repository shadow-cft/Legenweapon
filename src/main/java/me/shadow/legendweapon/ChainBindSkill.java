package me.shadow.legendweapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ChainBindSkill implements Listener {
    private final LegendWeapon plugin;
    private final Map<UUID, Long> bound = new HashMap<>();

    public ChainBindSkill(LegendWeapon plugin) { this.plugin = plugin; }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (event.getAction() != Action.LEFT_CLICK_AIR &&
            event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!SkillManager.hasLegendWeapon(player)) return;

        PlayerData data = SkillManager.getData(player);
        if (!data.isChainBindUnlocked()) {
            player.sendMessage("§cChain Bind belum terbuka. Butuh 7 kill.");
            return;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "chainbind")) {
            player.sendMessage("§cChain Bind cooldown: " +
                    CooldownManager.remainingSeconds(player.getUniqueId(), "chainbind") + " detik.");
            return;
        }

        List<LivingEntity> targets = findTargets(player, 32, 5);
        if (targets.isEmpty()) {
            player.sendMessage("§7Tidak ada target dalam jarak 32 blok.");
            return;
        }

        event.setCancelled(true);
        bind(player, targets);
        CooldownManager.setCooldown(player.getUniqueId(), "chainbind",
                plugin.getConfig().getInt("cooldown.chainbind", 20));
    }

    private List<LivingEntity> findTargets(Player player, double range, int max) {
        List<LivingEntity> result = new ArrayList<>();
        Vector direction = player.getEyeLocation().getDirection().normalize();

        for (LivingEntity entity : player.getWorld().getNearbyLivingEntities(
                player.getLocation(), range)) {
            if (result.size() >= max) break;
            if (entity == player || entity.isDead()) continue;

            Vector to = entity.getEyeLocation().toVector()
                    .subtract(player.getEyeLocation().toVector());

            if (to.lengthSquared() > range * range) continue;
            if (to.lengthSquared() == 0) continue;
            if (direction.dot(to.normalize()) < 0.35) continue;

            result.add(entity);
        }
        return result;
    }

    private void bind(Player caster, List<LivingEntity> targets) {
        long end = System.currentTimeMillis() + 5000L;

        for (LivingEntity target : targets) {
            bound.put(target.getUniqueId(), end);
            target.getWorld().spawnParticle(
                    Particle.ENCHANT, target.getLocation().add(0,1,0),
                    35, .4,.8,.4,.1);
            target.getWorld().playSound(
                    target.getLocation(), Sound.BLOCK_CHAIN_PLACE, 1f, .7f);
        }

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 100) {
                    for (LivingEntity target : targets)
                        bound.remove(target.getUniqueId());
                    cancel();
                    return;
                }

                for (LivingEntity target : targets) {
                    if (target.isDead() || !target.isValid()) continue;

                    target.setVelocity(new Vector(0, 0, 0));
                    target.getWorld().spawnParticle(
                            Particle.CRIT, target.getLocation().add(0,1,0),
                            4, .2,.4,.2,.02);

                    if (ticks % 4 == 0) {
                        target.damage(
                                plugin.getConfig().getDouble("damage.chainbind", 2.0),
                                caster);
                    }
                }
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Long end = bound.get(player.getUniqueId());
        if (end == null) return;

        if (System.currentTimeMillis() >= end) {
            bound.remove(player.getUniqueId());
            return;
        }

        if (event.getTo() != null) {
            event.setTo(event.getFrom().clone().setDirection(event.getTo().getDirection()));
        }
    }
}
