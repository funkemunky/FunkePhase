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
        if (FunkePhase.getInstance().hasPermission(sender, "help")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cUsage: /" + label.toLowerCase() + " [toggle/reload/alerts]"));
            } else {
                if (FunkePhase.getInstance().hasPermission(sender, "toggle")
                        && args[0].equalsIgnoreCase("toggle")) {
                    FunkePhase.getInstance().toggled = !FunkePhase.getInstance().toggled;
                    sender.sendMessage(ChatColor.GRAY + "AntiPhase has been set to: "
                            + (FunkePhase.getInstance().toggled ? ChatColor.GREEN : ChatColor.RED)
                            + FunkePhase.getInstance().toggled);
                    return true;
                }
                if (FunkePhase.getInstance().hasPermission(sender, "reload")
                        && args[0].equalsIgnoreCase("reload")) {
                    FunkePhase.getInstance().reloadPhase();
                    sender.sendMessage(ChatColor
                            .translateAlternateColorCodes('&', "&aReloaded FunkePhase!"));
                    return true;
                }
                if (FunkePhase.getInstance().hasPermission(sender, "alerts")
                        && args[0].equalsIgnoreCase("alerts") && sender instanceof Player) {
                    Player player = (Player) sender;

                    if (FunkePhase.getInstance().hasAlertsOn.contains(player.getUniqueId())) {
                        FunkePhase.getInstance().hasAlertsOn.remove(player.getUniqueId());
                    } else {
                        FunkePhase.getInstance().hasAlertsOn.add(player.getUniqueId());
                    }
                    sender.sendMessage(ChatColor.GRAY + "Toggled alerts: "
                            + (!FunkePhase.getInstance().hasAlertsOn.contains(player.getUniqueId())
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
