package cc.funkemunky.funkephase.commands;

import cc.funkemunky.funkephase.FunkePhase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PhaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (FunkePhase.INSTANCE.hasPermission(sender, "help")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cUsage: /" + label.toLowerCase() + " [toggle/reload/alerts]"));
            } else {
                if (FunkePhase.INSTANCE.hasPermission(sender, "toggle")
                        && args[0].equalsIgnoreCase("toggle")) {
                    FunkePhase.INSTANCE.phaseEnabled = !FunkePhase.INSTANCE.phaseEnabled;
                    sender.sendMessage(ChatColor.GRAY + "AntiPhase has been set to: "
                            + (FunkePhase.INSTANCE.phaseEnabled ? ChatColor.GREEN : ChatColor.RED)
                            + FunkePhase.INSTANCE.phaseEnabled);
                    return true;
                }
                if (FunkePhase.INSTANCE.hasPermission(sender, "reload")
                        && args[0].equalsIgnoreCase("reload")) {
                    FunkePhase.INSTANCE.reloadPhase();
                    sender.sendMessage(ChatColor
                            .translateAlternateColorCodes('&', "&aReloaded FunkePhase!"));
                    return true;
                }
                if (FunkePhase.INSTANCE.hasPermission(sender, "alerts")
                        && args[0].equalsIgnoreCase("alerts") && sender instanceof Player) {
                    Player player = (Player) sender;

                    if (FunkePhase.INSTANCE.getPlayersWithAlerts().contains(player.getUniqueId())) {
                        FunkePhase.INSTANCE.getPlayersWithAlerts().remove(player.getUniqueId());
                    } else {
                        FunkePhase.INSTANCE.getPlayersWithAlerts().add(player.getUniqueId());
                    }
                    sender.sendMessage(ChatColor.GRAY + "Toggled alerts: "
                            + (!FunkePhase.INSTANCE.getPlayersWithAlerts().contains(player.getUniqueId())
                            ? ChatColor.RED + "false" : ChatColor.GREEN + "true"));
                    return true;
                }
                sender.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',
                                "&cInvalid argument '" + args[0] + "'!"));
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "No permission.");
        return true;
    }


}
