package me.shadow.legendweapon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillManager implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        PlayerDataManager.getData(victim).resetAll();
        victim.sendMessage("§cKill streak kamu reset.");

        if (killer == null || killer == victim) return;

        PlayerData data = PlayerDataManager.getData(killer);
        int before = data.getKillStreak();
        data.addKill();
        int after = data.getKillStreak();

        killer.sendMessage("§aKill! §7Streak: §d" + after +
                " §7| Bonus damage: §c+" + data.getBonusDamage());

        if (before < 3 && after >= 3) killer.sendMessage("§d§lDASH UNLOCKED!");
        if (before < 5 && after >= 5) killer.sendMessage("§d§lPHOENIX UNLOCKED!");
        if (before < 7 && after >= 7) killer.sendMessage("§d§lCHAIN BIND UNLOCKED!");
        if (before < 10 && after >= 10) killer.sendMessage("§5§lULTIMATE UNLOCKED!");
    }
}
