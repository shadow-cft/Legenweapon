package me.shadow.legendweapon;

import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private int killStreak;
    private double bonusDamage;
    private boolean ultimateActive;
    private boolean dashUnlocked;
    private boolean phoenixUnlocked;
    private boolean chainBindUnlocked;
    private boolean ultimateUnlocked;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        resetAll();
    }

    public UUID getUUID() { return uuid; }
    public int getKillStreak() { return killStreak; }
    public double getBonusDamage() { return bonusDamage; }

    public void addKill() {
        killStreak++;
        bonusDamage += 5.0;
        updateUnlocks();
    }

    public void resetKillStreak() { killStreak = 0; }
    public void resetBonusDamage() { bonusDamage = 0; }

    public boolean isUltimateActive() { return ultimateActive; }
    public void setUltimateActive(boolean active) { ultimateActive = active; }

    public boolean isDashUnlocked() { return dashUnlocked; }
    public void setDashUnlocked(boolean value) { dashUnlocked = value; }

    public boolean isPhoenixUnlocked() { return phoenixUnlocked; }
    public void setPhoenixUnlocked(boolean value) { phoenixUnlocked = value; }

    public boolean isChainBindUnlocked() { return chainBindUnlocked; }
    public void setChainBindUnlocked(boolean value) { chainBindUnlocked = value; }

    public boolean isUltimateUnlocked() { return ultimateUnlocked; }
    public void setUltimateUnlocked(boolean value) { ultimateUnlocked = value; }

    public void updateUnlocks() {
        LegendWeapon p = LegendWeapon.getInstance();
        dashUnlocked = killStreak >= p.getConfig().getInt("unlock.dash", 3);
        phoenixUnlocked = killStreak >= p.getConfig().getInt("unlock.phoenix", 5);
        chainBindUnlocked = killStreak >= p.getConfig().getInt("unlock.chainbind", 7);
        ultimateUnlocked = killStreak >= p.getConfig().getInt("unlock.ultimate", 10);
    }

    public void resetAll() {
        killStreak = 0;
        bonusDamage = 0;
        ultimateActive = false;
        dashUnlocked = false;
        phoenixUnlocked = false;
        chainBindUnlocked = false;
        ultimateUnlocked = false;
    }
}
