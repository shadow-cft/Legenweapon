package me.shadow.legendweapon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public final class PlayerDataManager {
    private static final Map<UUID, PlayerData> DATA = new HashMap<>();

    private PlayerDataManager() {}

    public static PlayerData getData(Player player) {
        return DATA.computeIfAbsent(player.getUniqueId(), PlayerData::new);
    }

    public static void clear() { DATA.clear(); }
    public static void clear(Player player) { DATA.remove(player.getUniqueId()); }
}
