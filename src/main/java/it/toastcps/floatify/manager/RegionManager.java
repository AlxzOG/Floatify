package it.toastcps.floatify.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import it.toastcps.floatify.FloatifyPlugin;
import org.bukkit.entity.Player;

public class RegionManager {

    private final FloatifyPlugin plugin;
    private StateFlag floatifyFlag;
    private boolean available = false;

    public RegionManager(FloatifyPlugin plugin) {
        this.plugin = plugin;
    }

    public void tryRegisterFlag() {
        if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") == null) return;

        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            StateFlag flag = new StateFlag("floatify", false);
            registry.register(flag);
            floatifyFlag = flag;
            available = true;
        } catch (FlagConflictException e) {
            Object existing = WorldGuard.getInstance().getFlagRegistry().get("floatify");
            if (existing instanceof StateFlag sf) {
                floatifyFlag = sf;
                available = true;
            } else {
                plugin.getLogger().warning("Il flag 'floatify' esiste già con un tipo incompatibile. L'integrazione WorldGuard è disabilitata.");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Errore durante la registrazione del flag WorldGuard: " + e.getMessage());
        }
    }

    public boolean isInFloatifyRegion(Player player) {
        if (!available || floatifyFlag == null) return false;

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldguard.LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        return StateFlag.test(regions.queryState(localPlayer, floatifyFlag));
    }

    public boolean isAvailable() {
        return available;
    }
}
