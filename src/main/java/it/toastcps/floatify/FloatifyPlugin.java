package it.toastcps.floatify;

import it.toastcps.floatify.commands.FloatifyCommand;
import it.toastcps.floatify.config.PluginConfig;
import it.toastcps.floatify.listeners.PlayerConnectionListener;
import it.toastcps.floatify.listeners.RegionListener;
import it.toastcps.floatify.manager.FloatifyManager;
import it.toastcps.floatify.manager.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FloatifyPlugin extends JavaPlugin {

    private PluginConfig pluginConfig;
    private FloatifyManager floatifyManager;
    private RegionManager regionManager;

    @Override
    public void onLoad() {
        regionManager = new RegionManager(this);
        regionManager.tryRegisterFlag();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        pluginConfig = new PluginConfig(this);
        floatifyManager = new FloatifyManager(this, pluginConfig);
        floatifyManager.startHeightTask();

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(floatifyManager), this);

        if (regionManager.isAvailable()) {
            getServer().getPluginManager().registerEvents(new RegionListener(floatifyManager, regionManager), this);
        }

        FloatifyCommand command = new FloatifyCommand(this, floatifyManager, pluginConfig);
        getCommand("floatify").setExecutor(command);
        getCommand("floatify").setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        floatifyManager.disableAll();
    }
}
