package cc.funkemunky.funkephase;

import cc.funkemunky.api.utils.BlockUtils;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.api.utils.ReflectionsUtil;
import cc.funkemunky.api.utils.XMaterial;
import cc.funkemunky.funkephase.commands.PhaseCommand;
import cc.funkemunky.funkephase.data.DataManager;
import cc.funkemunky.funkephase.listener.EnderpearlListener;
import cc.funkemunky.funkephase.listener.PhaseListener;
import cc.funkemunky.funkephase.listener.QuitListener;
import com.google.common.collect.Sets;
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

    @Getter
    private static FunkePhase instance;
    public boolean toggled, epStuckProt;
    public EnumSet<Material> excludedBlocks;
    public Set<UUID> hasAlertsOn;
    public int maxMove = 10;
    public ExecutorService service;
    private DataManager dataManager;
    private String serverVersion;

    @Override
    public void onEnable() {
        instance = this;
        toggled = true;
        excludedBlocks = EnumSet.noneOf(Material.class);
        hasAlertsOn = Sets.newHashSet();
        saveDefaultConfig();
        serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        getServer().getPluginManager().registerEvents(new PhaseListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new EnderpearlListener(), this);
        getCommand("funkephase").setExecutor(new PhaseCommand());
        service = Executors.newSingleThreadExecutor();
        Bukkit.getPluginManager().registerEvents(dataManager = new DataManager(), this);

        getConfig().getStringList("excluded_blocks").forEach(string -> {
            try {
                excludedBlocks.add(XMaterial.matchXMaterial(string).orElseThrow(() ->
                        new RuntimeException("Material within XMaterial class \"" + string + "\" does not exist!"))
                        .parseMaterial());
            } catch (NullPointerException e) {
                throw new NullPointerException("The material '" + string + "' in the config does not exist!");
            }
        });

        epStuckProt = getConfig().getBoolean("enderpearl_stuck_protection");
    }

    public void reloadPhase() {
        reloadConfig();
        excludedBlocks.clear();
        epStuckProt = getConfig().getBoolean("enderpearl_stuck_protection");
        maxMove = getConfig().getInt("max_move");
        getConfig().getStringList("excluded_blocks").forEach(string -> {
            try {
                Material material = Material.getMaterial(string);

                excludedBlocks.add(material);
            } catch (NullPointerException e) {
                throw new NullPointerException("The material '" + string + "' in the config does not exist!");
            }
        });
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
        Bukkit.getOnlinePlayers().stream().filter(staff -> hasAlertsOn.contains(staff.getUniqueId())).forEach(staff -> {
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("alert_message")
                    .replaceAll("%player%", player.getName())));
        });
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("alert_message")
                .replaceAll("%player%", player.getName())));
    }
}
