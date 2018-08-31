package cc.funkemunky.funkephase;

import cc.funkemunky.funkephase.commands.PhaseCommand;
import cc.funkemunky.funkephase.listener.PhaseListener;
import cc.funkemunky.funkephase.listener.QuitListener;
import cc.funkemunky.funkephase.util.BlockUtils;
import cc.funkemunky.funkephase.util.ReflectionsUtil;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunkePhase extends JavaPlugin {

    public static FunkePhase instance;
    public boolean toggled;
    public List<Material> excludedBlocks;
    public Set<UUID> hasAlertsOn;
    public int maxMove = 10;
    public ExecutorService service;

    @Override
    public void onEnable() {
        excludedBlocks = new ArrayList<>();
        hasAlertsOn = Sets.newHashSet();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PhaseListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getCommand("funkephase").setExecutor(new PhaseCommand());
        instance = this;
        toggled = true;
        service = Executors.newSingleThreadExecutor();

        getConfig().getStringList("excluded_blocks").forEach(string -> {
            try {
                Material material = Material.getMaterial(string);

                excludedBlocks.add(material);
            } catch(NullPointerException e) {
                throw new NullPointerException("The material '" + string + "' in the config does not exist!");
            }
        });
        new ReflectionsUtil();
        new BlockUtils();
    }

    public void reloadPhase() {
        reloadConfig();
        excludedBlocks.clear();
        maxMove = getConfig().getInt("max_move");
        getConfig().getStringList("excluded_blocks").forEach(string -> {
            try {
                Material material = Material.getMaterial(string);

                excludedBlocks.add(material);
            } catch(NullPointerException e) {
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
        for(int i = 0 ; i < array.size() ; i++) {
            String string = array.get(i);

            toReturn.append(string).append(array.size() - i > 1 ? ", " : "");
        }
        return toReturn.toString();
    }

    public void alert(Player player, List<String> block) {
        Bukkit.getOnlinePlayers().stream().filter(staff -> hasAlertsOn.contains(staff.getUniqueId())).forEach(staff -> {
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("alert_message")
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%block%", formatArrayToString(block))));
        });
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("alert_message")
                .replaceAll("%player%", player.getName())
                .replaceAll("%block%", formatArrayToString(block))));
    }
}
