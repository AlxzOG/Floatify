package it.toastcps.floatify.config;

import it.toastcps.floatify.FloatifyPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PluginConfig {

    private final FloatifyPlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public PluginConfig(FloatifyPlugin plugin) {
        this.plugin = plugin;
    }

    public int getLevitationAmplifier() {
        return plugin.getConfig().getInt("levitation-amplifier", 0);
    }

    public double getMaxHeight() {
        return plugin.getConfig().getDouble("max-height", 50.0);
    }

    public Component getMessage(String key) {
        String raw = plugin.getConfig().getString("messages." + key, "<red>Messaggio mancante: " + key);
        return mm.deserialize(raw);
    }

    public void reload() {
        plugin.reloadConfig();
    }
}
