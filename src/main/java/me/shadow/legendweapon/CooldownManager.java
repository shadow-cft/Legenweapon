package me.shadow.legendweapon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CooldownManager {
    private static final Map<String, Long> DATA = new HashMap<>();

    private CooldownManager() {}

    private static String key(UUID id, String skill) { return id + ":" + skill; }

    public static boolean isOnCooldown(UUID id, String skill) {
        Long end = DATA.get(key(id, skill));
        return end != null && end > System.currentTimeMillis();
    }

    public static long remainingSeconds(UUID id, String skill) {
        Long end = DATA.get(key(id, skill));
        if (end == null) return 0;
        return Math.max(0, (long)Math.ceil((end - System.currentTimeMillis()) / 1000.0));
    }

    public static void setCooldown(UUID id, String skill, int seconds) {
        DATA.put(key(id, skill), System.currentTimeMillis() + seconds * 1000L);
    }

    public static void clear() { DATA.clear(); }
}
