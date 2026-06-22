package it.toastcps.floatify.listeners;

import it.toastcps.floatify.manager.FloatifyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final FloatifyManager floatifyManager;

    public PlayerConnectionListener(FloatifyManager floatifyManager) {
        this.floatifyManager = floatifyManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        floatifyManager.cleanup(e.getPlayer());
    }
}
