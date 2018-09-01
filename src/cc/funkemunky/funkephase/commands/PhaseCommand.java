/*
 * FunkePhase
 * Copyright (C) 2018 funkemunky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        if(FunkePhase.instance.hasPermission(sender, "help")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + label.toLowerCase() + " [toggle/reload/alerts]"));
            } else {
                if(FunkePhase.instance.hasPermission(sender, "toggle") && args[0].equalsIgnoreCase("toggle")) {
                    FunkePhase.instance.toggled = !FunkePhase.instance.toggled;
                    sender.sendMessage(ChatColor.GRAY + "AntiPhase has been set to: " + (FunkePhase.instance.toggled ? ChatColor.GREEN : ChatColor.RED) + FunkePhase.instance.toggled);
                    return true;
                }
                if(FunkePhase.instance.hasPermission(sender, "reload") && args[0].equalsIgnoreCase("reload")) {
                    FunkePhase.instance.reloadPhase();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded FunkePhase!"));
                    return true;
                }
                if(FunkePhase.instance.hasPermission(sender, "alerts") && args[0].equalsIgnoreCase("alerts") && sender instanceof Player) {
                    Player player = (Player) sender;

                    if(FunkePhase.instance.hasAlertsOn.contains(player.getUniqueId())) {
                        FunkePhase.instance.hasAlertsOn.remove(player.getUniqueId());
                    } else {
                        FunkePhase.instance.hasAlertsOn.add(player.getUniqueId());
                    }
                    sender.sendMessage(ChatColor.GRAY + "Toggled alerts: " + (!FunkePhase.instance.hasAlertsOn.contains(player.getUniqueId()) ? ChatColor.RED + "false" : ChatColor.GREEN + "true"));
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid argument '" + args[0] + "'!"));
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "No permission.");
        return true;
    }
}
