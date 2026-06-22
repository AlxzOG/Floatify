package it.toastcps.floatify.manager;

import it.toastcps.floatify.FloatifyPlugin;
import it.toastcps.floatify.config.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FloatifyManager {

    private final FloatifyPlugin plugin;
    private final PluginConfig config;

    private final Set<UUID> activeFloaters = new HashSet<>();
    private final Set<UUID> manualFloaters = new HashSet<>();

    public FloatifyManager(FloatifyPlugin plugin, PluginConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void enableFloat(Player player, boolean manual) {
        UUID id = player.getUniqueId();
        activeFloaters.add(id);
        if (manual) manualFloaters.add(id);
        applyLevitation(player);
    }

    public void disableFloat(Player player) {
        UUID id = player.getUniqueId();
        activeFloaters.remove(id);
        manualFloaters.remove(id);
        player.removePotionEffect(PotionEffectType.LEVITATION);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
    }

    public void disableFloatIfNotManual(Player player) {
        if (!manualFloaters.contains(player.getUniqueId())) {
            disableFloat(player);
        }
    }

    public boolean isFloating(Player player) {
        return activeFloaters.contains(player.getUniqueId());
    }

    public boolean isManualFloat(Player player) {
        return manualFloaters.contains(player.getUniqueId());
    }

    public void cleanup(Player player) {
        UUID id = player.getUniqueId();
        activeFloaters.remove(id);
        manualFloaters.remove(id);
    }

    public void disableAll() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (isFloating(player)) {
                player.removePotionEffect(PotionEffectType.LEVITATION);
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
        }
        activeFloaters.clear();
        manualFloaters.clear();
    }

    public void startHeightTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                double maxY = config.getMaxHeight();

                for (UUID id : activeFloaters) {
                    Player player = plugin.getServer().getPlayer(id);
                    if (player == null || !player.isOnline()) continue;

                    double y = player.getLocation().getY();

                    if (y >= maxY) {
                        player.removePotionEffect(PotionEffectType.LEVITATION);
                        if (!player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                            player.addPotionEffect(new PotionEffect(
                                    PotionEffectType.SLOW_FALLING, PotionEffect.INFINITE_DURATION, 0, false, false));
                        }
                    } else if (y < maxY - 2.0) {
                        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                        if (!player.hasPotionEffect(PotionEffectType.LEVITATION)) {
                            applyLevitation(player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 10L, 10L);
    }

    private void applyLevitation(Player player) {
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.LEVITATION,
                PotionEffect.INFINITE_DURATION,
                config.getLevitationAmplifier(),
                false,
                false));
    }
}
