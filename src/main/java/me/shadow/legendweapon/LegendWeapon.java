package me.shadow.legendweapon;

import org.bukkit.plugin.java.JavaPlugin;

public class LegendWeapon extends JavaPlugin {
    private static LegendWeapon instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new BasicAttack(this), this);
        getServer().getPluginManager().registerEvents(new DashSkill(this), this);
        getServer().getPluginManager().registerEvents(new PhoenixSkill(this), this);
        getServer().getPluginManager().registerEvents(new ChainBindSkill(this), this);
        getServer().getPluginManager().registerEvents(new UltimateSkill(this), this);
        getServer().getPluginManager().registerEvents(new KillManager(), this);

        if (getCommand("legendweapon") != null) {
            getCommand("legendweapon").setExecutor(new LegendWeaponCommand());
        }

        getLogger().info("LegendWeapon Enabled! Developer: Shadow Craft");
    }

    @Override
    public void onDisable() {
        CooldownManager.clear();
        PlayerDataManager.clear();
        getLogger().info("LegendWeapon Disabled!");
    }

    public static LegendWeapon getInstance() {
        return instance;
    }
}
