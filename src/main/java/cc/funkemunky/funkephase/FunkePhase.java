package cc.funkemunky.funkephase;

import cc.funkemunky.api.utils.XMaterial;
import cc.funkemunky.funkephase.commands.PhaseCommand;
import cc.funkemunky.funkephase.data.DataManager;
import cc.funkemunky.funkephase.listener.EnderpearlListener;
import cc.funkemunky.funkephase.listener.PhaseListener;
import cc.funkemunky.funkephase.listener.ConnectionListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Override
    public void onEnable() {
        INSTANCE = this;
        phaseEnabled = true;
        saveDefaultConfig();
        dataManager = new DataManager();
        getServer().getPluginManager().registerEvents(new PhaseListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new EnderpearlListener(), this);
        getCommand("funkephase").setExecutor(new PhaseCommand());
        service = Executors.newSingleThreadExecutor();

        updateExcludedMaterials();

        epStuckProt = getConfig().getBoolean("enderpearl_stuck_protection");
        alertsString = getConfig().getString("alert_message");
    }

    public void reloadPhase() {
        reloadConfig();
        excludedBlocks.clear();
        epStuckProt = getConfig().getBoolean("enderpearl_stuck_protection");
        maxMove = getConfig().getInt("max_move");
        alertsString = getConfig().getString("alert_message");

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

    public boolean hasPermission(Player sender, String permission) {
        return sender.hasPermission("funkephase." + permission) || sender.hasPermission("funkephase.admin");
    }

    public String formatArrayToString(List<String> array) {
        StringBuilder toReturn = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            String string = array.get(i);

            toReturn.append(string).append(array.size() - i > 1 ? ", " : "");
        }
        return toReturn.toString();
    }

    public void alert(Player player) {
        Bukkit.getOnlinePlayers().stream().filter(staff -> playersWithAlerts.contains(staff.getUniqueId()))
                .forEach(staff -> {
                    staff.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            getConfig().getString("alert_message")
                                    .replaceAll("%player%", player.getName())));
                });
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("alert_message")
                .replaceAll("%player%", player.getName())));
    }
}
