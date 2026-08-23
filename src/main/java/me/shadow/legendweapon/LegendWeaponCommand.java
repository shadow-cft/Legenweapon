package me.shadow.legendweapon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LegendWeaponCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command ini hanya untuk player.");
            return true;
        }

        if (!player.hasPermission("legendweapon.admin")) {
            player.sendMessage("§cKamu tidak memiliki permission.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            player.getInventory().addItem(WeaponManager.createDeathNote());
            player.sendMessage("§5§lDeath Note §7berhasil diberikan!");
            return true;
        }

        player.sendMessage("§d/legendweapon give");
        return true;
    }
}
