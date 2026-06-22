package it.toastcps.floatify.listeners;

import it.toastcps.floatify.manager.FloatifyManager;
import it.toastcps.floatify.manager.RegionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionListener implements Listener {

    private final FloatifyManager floatifyManager;
    private final RegionManager regionManager;
    private final Map<UUID, Boolean> regionState = new HashMap<>();

    public RegionListener(FloatifyManager floatifyManager, RegionManager regionManager) {
        this.floatifyManager = floatifyManager;
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        boolean inRegion = regionManager.isInFloatifyRegion(player);
        regionState.put(player.getUniqueId(), inRegion);
        if (inRegion) floatifyManager.enableFloat(player, false);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        if (!e.hasChangedBlock()) return;

        Player player = e.getPlayer();
        UUID id = player.getUniqueId();

        boolean nowInRegion = regionManager.isInFloatifyRegion(player);
        boolean wasInRegion = regionState.getOrDefault(id, false);

        if (nowInRegion == wasInRegion) return;
        regionState.put(id, nowInRegion);

        if (nowInRegion && !floatifyManager.isFloating(player)) {
            floatifyManager.enableFloat(player, false);
        } else if (!nowInRegion) {
            floatifyManager.disableFloatIfNotManual(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        regionState.remove(e.getPlayer().getUniqueId());
    }
}
