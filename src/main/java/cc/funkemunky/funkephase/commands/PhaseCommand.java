package cc.funkemunky.funkephase.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.funkephase.FunkePhase;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("funkephase|phase")
@CommandPermission("funkephase.command|funkephase.admin")
public class PhaseCommand extends BaseCommand {

    @Syntax("")
    public void onCommand(CommandSender sender)  {
        sender.sendMessage(ChatColor.RED + " Usage: /funkephase [toggle/reload/alerts]");
    }

    @Subcommand("alerts")
    @Description("Toggle your phase alerts")
    @CommandPermission("funkephase.command.alerts|funkephase.admin")
    public void onAlerts(Player player) {

        if (FunkePhase.INSTANCE.getPlayersWithAlerts().contains(player.getUniqueId())) {
            FunkePhase.INSTANCE.getPlayersWithAlerts().remove(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "Turned off your AntiPhase alerts.");
        } else {
            FunkePhase.INSTANCE.getPlayersWithAlerts().add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Turned on your AntiPhase alerts.");
        }
    }

    @Subcommand("reload")
    @Description("Reload the FunkePhase plugin config.")
    @CommandPermission("funkephase.command.reload|funkephase.admin")
    public void onReload(CommandSender sender) {
        FunkePhase.INSTANCE.reloadPhase();
        sender.sendMessage(ChatColor.GREEN + "FunkePhase succesfully reloaded!");
    }

    @Subcommand("toggle")
    @Description("Enable or disable the antiphase")
    public void onToggle(CommandSender sender) {
        FunkePhase.INSTANCE.phaseEnabled = !FunkePhase.INSTANCE.phaseEnabled;
        sender.sendMessage(ChatColor.GRAY + "AntiPhase has been set to: "
                + (FunkePhase.INSTANCE.phaseEnabled ? ChatColor.GREEN : ChatColor.RED)
                + FunkePhase.INSTANCE.phaseEnabled);
    }
}
