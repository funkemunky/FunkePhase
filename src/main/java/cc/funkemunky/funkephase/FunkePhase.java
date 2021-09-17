package cc.funkemunky.funkephase;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.XMaterial;
import cc.funkemunky.funkephase.commands.PhaseCommand;
import cc.funkemunky.funkephase.data.DataManager;
import cc.funkemunky.funkephase.listener.EnderpearlListener;
import cc.funkemunky.funkephase.listener.PhaseListener;
import cc.funkemunky.funkephase.listener.ConnectionListener;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Getter
public class FunkePhase extends JavaPlugin {

    public static FunkePhase INSTANCE;
    public boolean phaseEnabled, epStuckProt;
    private final EnumSet<Material> excludedBlocks = EnumSet.noneOf(Material.class);
    private final Set<UUID> playersWithAlerts = new HashSet<>();
    private int maxMove = 10;
    private ExecutorService service;
    private DataManager dataManager;
    private String alertsString;
    private Logger log;

    @Override
    public void onEnable() {
        log = Bukkit.getLogger();
        log.info("Starting initialization...");
        INSTANCE = this;
        phaseEnabled = true;

        log.info("Loading configuration data...");
        {
            saveDefaultConfig();
            updateExcludedMaterials();

            epStuckProt = getConfig().getBoolean("enderpearl_stuck_protection");
            alertsString = ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("alert_message"));
        }

        log.info("Loading services...");
        {
            dataManager = new DataManager();
            service = Executors.newSingleThreadExecutor();
        }

        log.info("Registering listeners...");
        {
            getServer().getPluginManager().registerEvents(new PhaseListener(), this);
            getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
            getServer().getPluginManager().registerEvents(new EnderpearlListener(), this);
        }


        log.info("Register command(s)...");
        {
            Atlas.getInstance().getBukkitCommandManager(this).registerCommand(new PhaseCommand());
        }

        log.info(String.format("Completed initialization of FunkePhase v%s", getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        log.info("Running shutdown sequence...");

        log.info("Disabling FunkePhase services...");
        {
            phaseEnabled = false;
            dataManager.shutdown();
            dataManager = null;
            service.shutdown();
        }

        log.info("Unregistering Bukkit services...");
        {
            HandlerList.unregisterAll(this);
            Bukkit.getScheduler().cancelTasks(this);
        }

        log.info("Clearing more data...");
        {
            excludedBlocks.clear();
            playersWithAlerts.clear();
            INSTANCE = null;
        }

        log.info("Completed shutdown sequence! Removing logger instance...");
        log = null;
    }

    public void reloadPhase() {
        reloadConfig();
        excludedBlocks.clear();
        epStuckProt = getConfig().getBoolean("enderpearl_stuck_protection");
        maxMove = getConfig().getInt("max_move");
        alertsString = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("alert_message"));

        updateExcludedMaterials();
    }

    private void updateExcludedMaterials() {
        excludedBlocks.clear(); //Making sure the Material set is empty so its updated properly
        getConfig().getStringList("excluded_blocks").forEach(string ->
                excludedBlocks.add(XMaterial.matchXMaterial(string)
                        .orElseThrow(() ->
                                new NullPointerException("Material within XMaterial class \""
                                        + string + "\" does not exist!"))
                        .parseMaterial()));
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission("funkephase." + permission) || sender.hasPermission("funkephase.admin");
    }

    public void alert(Player player) {
        //Storing field here for sending to console.
        String stringMsg = alertsString.replace("%player%", player.getName());

        //Converting to BaseComponent for message sending. Ensures formatting is proper
        BaseComponent[] message = TextComponent.fromLegacyText(stringMsg);

        for (UUID uuid : playersWithAlerts) {
            Player toSend = Bukkit.getPlayer(uuid); //Lightweight, better than storing Player objects in memory.

            toSend.spigot().sendMessage(message); //Sending player alert message
        }

        //Printing alert to console
        Bukkit.getConsoleSender().sendMessage(stringMsg);
    }
}
