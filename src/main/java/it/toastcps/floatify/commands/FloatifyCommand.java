package it.toastcps.floatify.commands;

import it.toastcps.floatify.FloatifyPlugin;
import it.toastcps.floatify.config.PluginConfig;
import it.toastcps.floatify.manager.FloatifyManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FloatifyCommand implements TabExecutor {

    private final FloatifyPlugin plugin;
    private final FloatifyManager floatifyManager;
    private final PluginConfig config;

    public FloatifyCommand(FloatifyPlugin plugin, FloatifyManager floatifyManager, PluginConfig config) {
        this.plugin = plugin;
        this.floatifyManager = floatifyManager;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(config.getMessage("usage"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on" -> handleToggle(sender, args, true);
            case "off" -> handleToggle(sender, args, false);
            case "reload" -> handleReload(sender);
            default -> sender.sendMessage(config.getMessage("usage"));
        }

        return true;
    }

    private void handleToggle(CommandSender sender, String[] args, boolean enable) {
        if (args.length >= 2) {
            if (!sender.hasPermission("floatify.others")) {
                sender.sendMessage(config.getMessage("no-permission"));
                return;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(config.getMessage("player-not-found"));
                return;
            }

            applyToggle(sender, target, enable, true);
            return;
        }

        if (!(sender instanceof Player self)) {
            sender.sendMessage(config.getMessage("not-player"));
            return;
        }

        if (!self.hasPermission("floatify.use")) {
            self.sendMessage(config.getMessage("no-permission"));
            return;
        }

        applyToggle(sender, self, enable, false);
    }

    private void applyToggle(CommandSender sender, Player target, boolean enable, boolean isOther) {
        if (enable) {
            if (floatifyManager.isFloating(target)) {
                sender.sendMessage(config.getMessage("already-on"));
                return;
            }
            floatifyManager.enableFloat(target, true);
            target.sendMessage(config.getMessage("float-on"));
            if (isOther) {
                sender.sendMessage(buildOtherMessage("float-on-other", target.getName()));
            }
        } else {
            if (!floatifyManager.isFloating(target)) {
                sender.sendMessage(config.getMessage("already-off"));
                return;
            }
            floatifyManager.disableFloat(target);
            target.sendMessage(config.getMessage("float-off"));
            if (isOther) {
                sender.sendMessage(buildOtherMessage("float-off-other", target.getName()));
            }
        }
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("floatify.admin")) {
            sender.sendMessage(config.getMessage("no-permission"));
            return;
        }
        config.reload();
        sender.sendMessage(config.getMessage("reload"));
    }

    private Component buildOtherMessage(String key, String playerName) {
        return config.getMessage(key)
                .replaceText(b -> b.matchLiteral("{player}").replacement(playerName));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if (sender.hasPermission("floatify.use")) {
                completions.add("on");
                completions.add("off");
            }
            if (sender.hasPermission("floatify.admin")) {
                completions.add("reload");
            }
            return filter(completions, args[0]);
        }

        if (args.length == 2 && sender.hasPermission("floatify.others")) {
            String sub = args[0].toLowerCase();
            if (sub.equals("on") || sub.equals("off")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }

    private List<String> filter(List<String> list, String input) {
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
